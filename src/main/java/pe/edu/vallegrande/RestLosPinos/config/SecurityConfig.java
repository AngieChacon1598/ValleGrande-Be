package pe.edu.vallegrande.RestLosPinos.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Configuration
public class SecurityConfig {
    private final String SECRET = "myjwtsecret";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> {})
            .csrf(csrf -> csrf.disable())
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/sales/**", "/api/reservations/**").hasAnyAuthority("Cliente", "Administrador")
                .antMatchers(HttpMethod.GET, "/api/products/**", "/api/categories/**").hasAnyAuthority("Administrador", "Cliente", "Mesero")
                .antMatchers(HttpMethod.GET, "/api/payment-types/**", "/api/order-status-types/**").hasAnyAuthority("Administrador", "Cliente", "Mesero")
                .antMatchers("/api/products/**", "/api/categories/**", "/api/users/**").hasAuthority("Administrador")
                .antMatchers("/api/**").hasAuthority("Administrador")
                .antMatchers(HttpMethod.POST, "/api/reservations").hasAnyAuthority("Administrador", "Cliente")
                .antMatchers(HttpMethod.GET, "/api/reservations/user/**").hasAnyAuthority("Administrador", "Cliente")
                .antMatchers(HttpMethod.GET, "/api/reservations/**").hasAuthority("Administrador")
                .antMatchers(HttpMethod.PUT, "/api/reservations/**").hasAuthority("Administrador")
                .antMatchers(HttpMethod.DELETE, "/api/reservations/**").hasAuthority("Administrador")
                .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    class JwtFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                try {
                    Claims claims = Jwts.parser()
                            .setSigningKey(SECRET.getBytes())
                            .parseClaimsJws(token)
                            .getBody();
                    String role = claims.get("role", String.class);
                    String username = claims.getSubject();
                    Integer userId = claims.get("userId", Integer.class);
                    
                    var auth = new UsernamePasswordAuthenticationToken(
                            username,
                            userId,
                            List.of(() -> role)
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
            filterChain.doFilter(request, response);
        }
    }
} 