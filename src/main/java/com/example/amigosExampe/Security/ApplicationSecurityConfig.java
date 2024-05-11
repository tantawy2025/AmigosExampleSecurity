package com.example.amigosExampe.Security;

import com.example.amigosExampe.auth.ApplicationUserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.concurrent.TimeUnit;

import static com.example.amigosExampe.Security.ApplicationUserPermission.COURSES_WRITE;
import static com.example.amigosExampe.Security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    /*
     when creating new user , password must be encoded bycrpted so i inject this
    because PasswordEncoder is an interface so i created a bean from it and refers to
    BCryptPasswordEncoder implementation
    configuration class and define a bean in it
    */

    private final PasswordEncoder passwordEncoder;

    private final ApplicationUserService applicationUserService;
    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
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
//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//
//        UserDetails annaUser =User.builder()
//                .username("anna")
//                .password(passwordEncoder.encode("password"))
//                .authorities(STUDENT.getGrantedAuthorites())
//               // .roles(STUDENT.name())
//                .build();
//
//        UserDetails lindaUser = User.builder()
//                .username("linda")
//                .password(passwordEncoder.encode("password123"))
//                .authorities(ADMIN.getGrantedAuthorites())
//              //  .roles(ADMIN.name())
//                .build();
//
//        UserDetails tomUser = User.builder()
//                .username("tom")
//                .password(passwordEncoder.encode("password"))
//                .authorities(ADMINTRAINEE.getGrantedAuthorites())
//             //   .roles(ADMINTRAINEE.name())
//                .build();
//
//        return new InMemoryUserDetailsManager(annaUser,lindaUser,tomUser);
//    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);

        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                // any request come like the pattarn
                .antMatchers("/","index","/css/*","/js/*")
                // permit any request coming like pattarn in ant matchers
                .permitAll()
                // any url contains below pattarn must be accessed by a user who has role STUDENT
                .antMatchers("/api/**").hasRole(STUDENT.name())
//                .antMatchers(HttpMethod.DELETE,"/managment/api/**").hasAuthority(COURSES_WRITE.getPermission())
//                .antMatchers(HttpMethod.POST,"/managment/api/**").hasAuthority(COURSES_WRITE.getPermission())
//                .antMatchers(HttpMethod.PUT,"/managment/api/**").hasAuthority(COURSES_WRITE.getPermission())
//                .antMatchers(HttpMethod.GET,"/managment/api/**").hasAnyRole(ADMIN.name(),ADMINTRAINEE.name())

                .anyRequest()
                .authenticated()
                .and()
               // .httpBasic();
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .defaultSuccessUrl("/courses",true)
                    .passwordParameter("password")
                    .usernameParameter("username")
                .and()
                .rememberMe()
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                    .key("somethingverysecure")
                    .rememberMeParameter("remember-me")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID","remember-me")
                    .logoutSuccessUrl("/login");


        ;
    }
}
