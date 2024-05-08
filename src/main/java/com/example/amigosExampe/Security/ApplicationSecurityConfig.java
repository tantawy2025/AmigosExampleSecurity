package com.example.amigosExampe.Security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration

public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    /*
     when creating new user , password must be encoded bycrpted so i inject this
    because PasswordEncoder is an interface so i created a bean from it and refers to
    BCryptPasswordEncoder implementation
    configuration class and define a bean in it
    */
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

/*
* You can define custom authentication by exposing a custom UserDetailsService as a bean.
* For example, the following will customize authentication assuming that
* CustomUserDetailsService implements UserDetailsService:
This is only used if the AuthenticationManagerBuilder has not been populated and
* no AuthenticationProviderBean is defined.
*  there are different ways to expose a bean in Spring: @Configuration with factory method
* (@Bean), @ComponentScan with @Component, XML etc.
* */
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {

        UserDetails annaUser =User.builder()
                .username("anna")
                .password(passwordEncoder.encode("password"))
                .roles("STUDENT")
                .build();

        UserDetails lindaUser = User.builder()
                .username("linda")
                .password(passwordEncoder.encode("password123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(annaUser,lindaUser);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // any request come like the pattarn
                .antMatchers("/","/index","/css/**","/js/**")
                // permit any request coming like pattarn in ant matchers
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }
}
