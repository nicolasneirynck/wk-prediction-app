package com.example.wk_prognose.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login**", "/register**", "/css/**", "/home", "/teams/ranking").permitAll()

                        .requestMatchers(HttpMethod.GET, "/matches/manage", "/matches/add", "/matches/edit/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/matches/add", "/matches/edit/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/matches/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/matches/*/prediction").hasAnyRole("USER", "ADMIN")

                        .requestMatchers("/teams/**", "/predictions/**").hasAnyRole("USER", "ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                )
//                .exceptionHandling(ex -> ex
//                        .accessDeniedPage("/403")
//                )
                .logout(logout -> logout.permitAll());
        //DEFAULT IS .logoutUrl("/logout")
        //                  .logoutSuccessUrl("/login?logout")
        return http.build();
    }
}
