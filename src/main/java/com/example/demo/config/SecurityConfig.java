package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.Service.impl.UserServiceImpl;
import com.example.demo.security.JwtAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor

public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	UserServiceImpl userService;
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
	    return super.authenticationManagerBean();
	}
	@Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
		@Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	   }
	  @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(userService) 
	            .passwordEncoder(passwordEncoder()); 
	    }
	 
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and()
		.csrf().disable()
		.authorizeRequests()
		.antMatchers(HttpMethod.POST,"/api/auth/login").permitAll()
		.antMatchers(HttpMethod.POST,"/api/uploadfile").permitAll()
		.antMatchers(HttpMethod.GET,"/api/auth/user").permitAll()
		.antMatchers(HttpMethod.GET,"/image/**").permitAll()
		.antMatchers(HttpMethod.POST,"/api/auth/register/**").permitAll()
		.antMatchers(HttpMethod.POST,"/api/auth/forgot/**").permitAll()
		.antMatchers(HttpMethod.GET,"/api/auth/forgot/**").permitAll()
		.antMatchers(HttpMethod.GET,"/api/auth/accountVerification/**").permitAll()
		.antMatchers("/api/products/**").permitAll()
		.antMatchers("/api/detailproducts/**").permitAll()
		.anyRequest()
		.authenticated();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	@Override
	public void configure(WebSecurity web) throws Exception {
		
		super.configure(web);
		web.ignoring().antMatchers("/image/**");
	}
}
