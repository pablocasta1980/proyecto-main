package co.edu.uniquindio.proyecto.config;

import co.edu.uniquindio.proyecto.seguridad.AutenticacionEntryPoint;
import co.edu.uniquindio.proyecto.seguridad.JWTFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors() // Deja que el CorsFilter se encargue
                .and()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> req
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/imagenes").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/recuperarPassword").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/cambiar-password").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/registro").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/activar/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/reenviar-token").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/reportes/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/reportes").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/reportes/cercanos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/reportes/buscar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categorias/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categorias").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/comentarios/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/{id}").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/usuarios/notificaciones/suscribirse").hasAuthority("ROLE_CLIENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/actualizar-password").hasAuthority("ROLE_CLIENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").hasAuthority("ROLE_CLIENTE")
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasAuthority("ROLE_CLIENTE")

                        .requestMatchers(HttpMethod.POST, "/api/reportes").hasAuthority("ROLE_CLIENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/reportes/{id}").hasAuthority("ROLE_CLIENTE")
                        .requestMatchers(HttpMethod.DELETE, "/api/reportes/{id}").hasAuthority("ROLE_CLIENTE")
                        .requestMatchers(HttpMethod.POST, "/api/reportes/{id}/importante").hasAuthority("ROLE_CLIENTE")
                        .requestMatchers(HttpMethod.POST, "/api/reportes/{id}/calificacion").hasAuthority("ROLE_CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/reportes/mis-reportes").hasAuthority("ROLE_CLIENTE")
                        .requestMatchers(HttpMethod.POST, "/api/comentarios/**").hasAuthority("ROLE_CLIENTE")

                        .requestMatchers(HttpMethod.POST, "/api/categorias").hasAuthority("ROLE_ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/categorias/{id}").hasAuthority("ROLE_ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/categorias/{id}").hasAuthority("ROLE_ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/api/reportes/informe").hasAuthority("ROLE_ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/api/reportes/filtrar").hasAuthority("ROLE_ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/api/usuarios").hasAuthority("ROLE_ADMINISTRADOR")

                        .requestMatchers(HttpMethod.PUT, "/api/reportes/{id}/estado").permitAll()

                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new AutenticacionEntryPoint()))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Filtro global de CORS: asegúrate de incluir tu frontend aquí
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "https://alertascomunitariasapp.web.app",
                "http://localhost:4200"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
