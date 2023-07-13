package com.nagarro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class ApplicationSecurity {

    private static final String ADMIN = "admin";
    private static final String USER = "user";

    @Bean
    @SuppressWarnings("deprecation")
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username(ADMIN)
                .password(ADMIN)
                .roles(ADMIN.toUpperCase())
                .build();
        UserDetails user = User.withDefaultPasswordEncoder()
                .username(USER)
                .password(USER)
                .roles(USER.toUpperCase())
                .build();
        return new InMemoryUserDetailsManager(
                admin, user
        );
    }

    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception {
        http.sessionManagement(session -> session.invalidSessionUrl("/login")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .expiredUrl("/login"))
                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .logout(Customizer.withDefaults());
        return http.build();
    }
}
