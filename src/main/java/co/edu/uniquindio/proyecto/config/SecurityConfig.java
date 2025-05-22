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
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> req
                        // Configuración de permisos para endpoints públicos
                        .requestMatchers(
                                "/api/auth",
                                "/api/imagenes",
                                "/api/usuarios/registro",
                                "/api/usuarios/activar/**",
                                "/api/usuarios/reenviar-token",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api/reportes/**",
                                "/api/categorias/**",
                                "/api/comentarios/**"
                        ).permitAll()

                        // Configuración de roles y permisos específicos
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/notificaciones/suscribirse").hasAuthority("CLIENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/actualizar-password").hasAuthority("CLIENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").hasAuthority("CLIENTE")
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasAuthority("CLIENTE")
                        .requestMatchers(HttpMethod.POST, "/api/reportes").hasAuthority("CLIENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/reportes/{id}").hasAuthority("CLIENTE")
                        .requestMatchers(HttpMethod.DELETE, "/api/reportes/{id}").hasAuthority("CLIENTE")
                        .requestMatchers(HttpMethod.POST, "/api/reportes/{id}/importante").hasAuthority("CLIENTE")
                        .requestMatchers(HttpMethod.POST, "/api/reportes/{id}/calificacion").hasAuthority("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/reportes/mis-reportes").hasAuthority("CLIENTE")
                        .requestMatchers(HttpMethod.POST, "/api/comentarios/**").hasAuthority("CLIENTE")

                        .requestMatchers(HttpMethod.POST, "/api/categorias").hasAuthority("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/categorias/{id}").hasAuthority("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/categorias/{id}").hasAuthority("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/api/reportes/informe").hasAuthority("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/api/reportes/filtrar").hasAuthority("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/api/usuarios").hasAuthority("ADMINISTRADOR")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new AutenticacionEntryPoint()))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Configuración CORS mejorada
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200",
                "https://alertascomunitariasapp.firebaseapp.com",
                "https://alertascomunitariasapp.web.app",
                "https://proyecto-main-3vcz.onrender.com"
        ));

        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Cache-Control",
                "Content-Type",
                "Access-Control-Allow-Origin",
                "X-Requested-With"
        ));
        config.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Disposition"
        ));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
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