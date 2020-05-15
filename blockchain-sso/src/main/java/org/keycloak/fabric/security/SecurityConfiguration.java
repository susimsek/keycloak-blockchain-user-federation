package org.keycloak.fabric.security;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Value("${security.username}")
    private String securityUsername;

    @Value("${security.password}")
    private String securityPassword;

    @Value("${security.role}")
    private String securityRole;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser(securityUsername)
                .password(passwordEncoder.encode(securityPassword))
                .roles(securityRole);
    }

    //Http security alan sınıfı override ettik.WebAdapterdan geliyor
    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.httpBasic().authenticationEntryPoint(new AuthEntryPoint());
        http.
                authorizeRequests().
                antMatchers("/api/v1/**").authenticated()
                .and()
                .authorizeRequests().anyRequest().permitAll();


    }




}
