package com.kg.projects.homework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
// http://docs.spring.io/spring-boot/docs/current/reference/html/howto-security.html
// Switch off the Spring Boot security configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {   	
    	http
		.authorizeRequests()
			.antMatchers("/login.html").permitAll()
			.antMatchers("/h2-console/**").permitAll()
			.anyRequest().authenticated()
			.and().csrf().ignoringAntMatchers("/h2-console/**")
			.and().headers().frameOptions().sameOrigin()
			.and().csrf().ignoringAntMatchers("/logout")
			.and().csrf().ignoringAntMatchers("/delete/**")
			.and().csrf().ignoringAntMatchers("/edit/**")
			.and().csrf().ignoringAntMatchers("/update/**")
			.and().csrf().ignoringAntMatchers("/new/**")
			.and().csrf().ignoringAntMatchers("/create/**")
			.and()
		.formLogin()
			.loginPage("/login.html")
			.failureForwardUrl("/login-error.html")
			.and()
		.logout().clearAuthentication(true)
			.logoutUrl("/logout")
			.logoutSuccessUrl("/login.html")
			.and()
		.httpBasic();   
    }

    @SuppressWarnings("deprecation")
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
        		.passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser("Kristīne").password("admin").roles("USER");
    }

}
