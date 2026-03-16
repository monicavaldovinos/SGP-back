package utez.edu.mx.services.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class MainSecurity {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtFilter jwtFilter;

    public MainSecurity(UserDetailsServiceImpl userDetailsService, JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of(
                            "http://localhost:5173",
                            "http://localhost:5174",
                            "http://localhost:3000"
                    ));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        // AUTH
                        .requestMatchers("/sgp-api/auth/login").permitAll()
                        .requestMatchers("/sgp-api/auth/registro").permitAll()
                        .requestMatchers("/sgp-api/auth/forgot-password").permitAll()
                        .requestMatchers("/sgp-api/auth/reset-password").permitAll()
                        .requestMatchers("/sgp-api/auth/validate-reset-token").permitAll()
                        .requestMatchers("/sgp-api/auth/me").authenticated()

                        // DASHBOARD
                        .requestMatchers(HttpMethod.GET, "/sgp-api/dashboard/mi-dashboard")
                        .hasAnyRole("SUPERADMIN", "LIDER", "INTEGRANTE")
                        .requestMatchers(HttpMethod.GET, "/sgp-api/dashboard/integrante/**")
                        .hasAnyRole("SUPERADMIN", "LIDER", "INTEGRANTE")
                        .requestMatchers(HttpMethod.GET, "/sgp-api/dashboard/lider/**")
                        .hasAnyRole("SUPERADMIN", "LIDER")
                        .requestMatchers(HttpMethod.GET, "/sgp-api/dashboard/admin")
                        .hasRole("SUPERADMIN")

                        // USUARIOS
                        .requestMatchers(HttpMethod.GET, "/sgp-api/usuarios/mi-perfil")
                        .hasAnyRole("SUPERADMIN", "LIDER", "INTEGRANTE")
                        .requestMatchers(HttpMethod.GET, "/sgp-api/usuarios/**")
                        .hasAnyRole("SUPERADMIN", "LIDER")
                        .requestMatchers(HttpMethod.POST, "/sgp-api/usuarios")
                        .hasRole("SUPERADMIN")
                        .requestMatchers(HttpMethod.PUT, "/sgp-api/usuarios/**")
                        .hasRole("SUPERADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/sgp-api/usuarios/**")
                        .authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/sgp-api/usuarios/**")
                        .hasRole("SUPERADMIN")

                        // EQUIPOS
                        .requestMatchers(HttpMethod.GET, "/sgp-api/equipos/mi-equipo")
                        .hasAnyRole("SUPERADMIN", "LIDER", "INTEGRANTE")
                        .requestMatchers(HttpMethod.GET, "/sgp-api/equipos/mis-integrantes")
                        .hasAnyRole("SUPERADMIN", "LIDER")
                        .requestMatchers(HttpMethod.GET, "/sgp-api/equipos/**")
                        .hasAnyRole("SUPERADMIN", "LIDER", "INTEGRANTE")
                        .requestMatchers(HttpMethod.POST, "/sgp-api/equipos")
                        .hasAnyRole("SUPERADMIN", "LIDER")
                        .requestMatchers(HttpMethod.PUT, "/sgp-api/equipos/**")
                        .hasAnyRole("SUPERADMIN", "LIDER")
                        .requestMatchers(HttpMethod.DELETE, "/sgp-api/equipos/**")
                        .hasRole("SUPERADMIN")

                        // EQUIPO-USUARIO
                        .requestMatchers(HttpMethod.POST, "/sgp-api/equipo-usuario/asignar")
                        .hasAnyRole("SUPERADMIN", "LIDER")
                        .requestMatchers(HttpMethod.DELETE, "/sgp-api/equipo-usuario/quitar")
                        .hasAnyRole("SUPERADMIN", "LIDER")

                        // PROYECTOS
                        .requestMatchers(HttpMethod.GET, "/sgp-api/proyectos/mis-proyectos")
                        .hasAnyRole("SUPERADMIN", "LIDER", "INTEGRANTE")
                        .requestMatchers(HttpMethod.GET, "/sgp-api/proyectos/**")
                        .hasAnyRole("SUPERADMIN", "LIDER", "INTEGRANTE")
                        .requestMatchers(HttpMethod.POST, "/sgp-api/proyectos")
                        .hasAnyRole("SUPERADMIN", "LIDER")
                        .requestMatchers(HttpMethod.PUT, "/sgp-api/proyectos/**")
                        .hasAnyRole("SUPERADMIN", "LIDER")
                        .requestMatchers(HttpMethod.PATCH, "/sgp-api/proyectos/**")
                        .hasAnyRole("SUPERADMIN", "LIDER")
                        .requestMatchers(HttpMethod.DELETE, "/sgp-api/proyectos/**")
                        .hasRole("SUPERADMIN")

                        // TAREAS
                        .requestMatchers(HttpMethod.GET, "/sgp-api/tareas/mis-tareas")
                        .hasAnyRole("SUPERADMIN", "LIDER", "INTEGRANTE")
                        .requestMatchers(HttpMethod.GET, "/sgp-api/tareas/**")
                        .hasAnyRole("SUPERADMIN", "LIDER", "INTEGRANTE")
                        .requestMatchers(HttpMethod.POST, "/sgp-api/tareas")
                        .hasAnyRole("SUPERADMIN", "LIDER")
                        .requestMatchers(HttpMethod.PUT, "/sgp-api/tareas/**")
                        .hasAnyRole("SUPERADMIN", "LIDER")
                        .requestMatchers(HttpMethod.PATCH, "/sgp-api/tareas/**")
                        .hasAnyRole("SUPERADMIN", "LIDER", "INTEGRANTE")
                        .requestMatchers(HttpMethod.DELETE, "/sgp-api/tareas/**")
                        .hasAnyRole("SUPERADMIN", "LIDER")

                        // PAGOS
                        .requestMatchers(HttpMethod.GET, "/sgp-api/pagos/mis-pagos")
                        .hasAnyRole("SUPERADMIN", "LIDER", "INTEGRANTE")
                        .requestMatchers(HttpMethod.POST, "/sgp-api/pagos/generar-periodo")
                        .hasAnyRole("SUPERADMIN", "LIDER")
                        .requestMatchers(HttpMethod.PATCH, "/sgp-api/pagos/*/realizar")
                        .hasAnyRole("SUPERADMIN", "LIDER")
                        .requestMatchers(HttpMethod.GET, "/sgp-api/pagos/**")
                        .hasAnyRole("SUPERADMIN", "LIDER", "INTEGRANTE")
                        .requestMatchers(HttpMethod.POST, "/sgp-api/pagos")
                        .hasAnyRole("SUPERADMIN", "LIDER")
                        .requestMatchers(HttpMethod.PUT, "/sgp-api/pagos/**")
                        .hasAnyRole("SUPERADMIN", "LIDER")
                        .requestMatchers(HttpMethod.PATCH, "/sgp-api/pagos/**")
                        .hasAnyRole("SUPERADMIN", "LIDER")
                        .requestMatchers(HttpMethod.DELETE, "/sgp-api/pagos/**")
                        .hasAnyRole("SUPERADMIN", "LIDER")

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}