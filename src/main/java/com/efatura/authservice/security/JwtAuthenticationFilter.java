package com.efatura.authservice.security;


import com.mongodb.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Header kontrolü: Authorization başlığı yoksa veya Bearer ile başlamıyorsa devam et
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Token'ı "Bearer " kısmından sonraki metin olarak al
        jwt = authHeader.substring(7);

        // 3. Token içinden email'i çıkar
        userEmail = jwtProvider.getEmailFromToken(jwt);

        // 4. Email doluysa ve kullanıcı henüz sisteme authenticate edilmemişse (SecurityContext boşsa)
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 5. Token geçerli mi kontrol et
            if (jwtProvider.validateToken(jwt)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Kullanıcıyı sisteme "onaylı" olarak kaydet
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 7. Bir sonraki filtreye geç
        filterChain.doFilter(request, response);
    }


}
