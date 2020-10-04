package org.am.web.config;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.am.web.utils.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	public CustomAuthenticationSuccessHandler successHandler;

	@Autowired
	public CustomAuthenticationFailureHandler failureHandler;

	@Autowired
	DataSource dataSource;

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {

		auth.jdbcAuthentication().dataSource(dataSource)
		.usersByUsernameQuery("select username,password, enabled from users where enabled=1 and username=?")
		.authoritiesByUsernameQuery("select username,role from users where username=?").passwordEncoder(PasswordEncoder.getPasswordEncoder());

	} 

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http
		.authorizeRequests()		
		.antMatchers("/assets/css/**").permitAll()
		.antMatchers("/assets/js/core/libraries/jquery.min.js").permitAll()
		.antMatchers("/assets/js/core/libraries/bootstrap.min.js").permitAll()
		.antMatchers("/assets/js/core/app.js").permitAll()
		.antMatchers("/assets/css/icons/icomoon/styles.css").permitAll()
		.antMatchers("/denied").permitAll()
		.antMatchers("/images/timg.png").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.loginPage("/login")
		.usernameParameter("username").passwordParameter("password")
		.defaultSuccessUrl("/index",true)
		.permitAll()
		.successHandler(successHandler)
		.failureHandler(failureHandler) 
		.and()
		.logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
		.permitAll();

		//http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}
}

