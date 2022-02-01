package com.decimalcode.qmed.security;

import com.decimalcode.qmed.api.users.service.UserServiceImpl;
import com.decimalcode.qmed.response.ApiSecurityExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.decimalcode.qmed.config.ApiGeneralSettings.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@SuppressWarnings("FieldCanBeLocal")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /*
     * Encode user password
     */
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    /*
     * White-listed end points
     */
    private final String[ ] whiteListedEndPoints = {
        API_ROOT_URL + SIGN_IN_URL + "/**",
        API_ROOT_URL + SIGN_UP_URL + "/**"
    };
    /*
     * User-service
     */
    private final UserServiceImpl userService;

    private final JwtAuthenticationFilter jwtTokenVerifierFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(jwtTokenVerifierFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(whiteListedEndPoints)// white-listed url
                .permitAll()
                .anyRequest().authenticated()
            .and()
            .exceptionHandling()
                .authenticationEntryPoint(new ApiSecurityExceptionResponse())
                .accessDeniedHandler(new ApiSecurityExceptionResponse())
            .and()
            .formLogin().disable()
            .httpBasic().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider providerDao = new DaoAuthenticationProvider();
        providerDao.setPasswordEncoder(bCryptPasswordEncoder); // BCrypt-PasswordEncoder
        providerDao.setUserDetailsService(userService);
        return providerDao;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
