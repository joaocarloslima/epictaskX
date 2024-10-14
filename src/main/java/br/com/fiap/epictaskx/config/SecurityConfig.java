package br.com.fiap.epictaskx.config;

import br.com.fiap.epictaskx.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, UserService userService) throws Exception {
        http

                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2Login(login -> login
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo.userService(userService))
                        .permitAll()
                )
                //.formLogin(Customizer.withDefaults())
        ;
        return http.build();
    }

}
