package com.example.smartplantbuddy.security;

import com.example.smartplantbuddy.security.details.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfiguration(JwtTokenProvider jwtTokenProvider, UserDetailsServiceImpl userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/swagger-ui/**", "/swagger-resources/**", "/v2/api-docs", "/v3/api-docs/**", "/webjars/**", "/swagger-ui.html")
                .permitAll()

                .antMatchers(HttpMethod.POST, "/api/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/signup").permitAll()

                //TODO: Check if these endpoints are correct
                .antMatchers("/api/user/isUser").hasAuthority("USER")
                .antMatchers("/api/user/send-invitation").hasAuthority("USER")
                .antMatchers("/api/user/accept-invitation/{invitationId}").hasAuthority("USER")
                .antMatchers("/api/user/decline-invitation/{invitationId}").hasAuthority("USER")
                .antMatchers("/error").permitAll()

                .antMatchers(HttpMethod.POST, "/api/gallery/add").hasAuthority("USER")
                .antMatchers(HttpMethod.DELETE, "/api/gallery/delete/{galleryId}").hasAuthority("USER")
                .antMatchers(HttpMethod.GET, "/api/gallery/plant/{plantId}").hasAuthority("USER")

                .antMatchers(HttpMethod.GET, "/api/plant/friends-plants/{userId}").hasAuthority("USER")
                .antMatchers(HttpMethod.GET, "/api/plant/friends-plants-details/{userId}").hasAuthority("USER")
                .antMatchers(HttpMethod.POST, "/api/plant/upload").hasAuthority("USER")
                .antMatchers(HttpMethod.GET, "/api/plant/all").hasAuthority("USER")
                .antMatchers(HttpMethod.DELETE, "/api/plant/delete/{plantId}").hasAuthority("USER")
                .antMatchers(HttpMethod.PUT, "/api/plant/update/{plantId}").hasAuthority("USER")
                .antMatchers(HttpMethod.PATCH, "/api/plant/update-times/{plantId}").hasAuthority("USER")

                .antMatchers(HttpMethod.POST, "/api/note/addNote").hasAuthority("USER")
                .antMatchers(HttpMethod.GET, "/api/note/{plantId}/notes").hasAuthority("USER")
                .antMatchers(HttpMethod.PUT, "/api/note/{noteId}").hasAuthority("USER")
                .antMatchers(HttpMethod.DELETE, "/api/note/{noteId}").hasAuthority("USER")

                .antMatchers(HttpMethod.POST, "/api/comment/add").hasAuthority("USER")
                .antMatchers(HttpMethod.GET, "/api/comment/plant/{plantId}").hasAuthority("USER")
                .antMatchers(HttpMethod.PUT, "/api/comment/update/{commentId}").hasAuthority("USER")
                .antMatchers(HttpMethod.DELETE, "/api/comment/delete/{commentId}").hasAuthority("USER")


                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
