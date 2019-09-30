package com.example.demo;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CompositeFilter;

@SpringBootApplication
@EnableOAuth2Client
@RestController
public class PlayMusicApplication extends WebSecurityConfigurerAdapter{

	@Autowired
	 OAuth2ClientContext oauth2ClientContext;
	
	@RequestMapping("/user")
	  public Principal user(Principal principal) {
	    return principal;
	  }

	public static void main(String[] args) {
		SpringApplication.run(PlayMusicApplication.class, args);
	}
	
	@Override
	  protected void configure(HttpSecurity http) throws Exception {
	    http
	      .antMatcher("/**")
	      .authorizeRequests()
	        .antMatchers("/", "/login**", "/error**")
	        .permitAll()
	      .anyRequest()
	        .authenticated()
	    	.and().logout().logoutSuccessUrl("/").permitAll()
	    	.and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
	    	.and().addFilterBefore(addFiltro(), BasicAuthenticationFilter.class);

	}
	
	
	private Filter addFiltro() {

		  CompositeFilter filter = new CompositeFilter();
		  List<Filter> filters = new ArrayList<>();

		  OAuth2ClientAuthenticationProcessingFilter facebookFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/facebook");
		  OAuth2RestTemplate facebookTemplate = new OAuth2RestTemplate(facebook(), oauth2ClientContext);
		  facebookFilter.setRestTemplate(facebookTemplate);
		  UserInfoTokenServices tokenServices = new UserInfoTokenServices(facebookResource().getUserInfoUri(), facebook().getClientId());
		  tokenServices.setRestTemplate(facebookTemplate);
		  facebookFilter.setTokenServices(tokenServices);
		  filters.add(facebookFilter);

		  OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/google");
		  OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google(), oauth2ClientContext);
		  googleFilter.setRestTemplate(googleTemplate);
		  tokenServices = new UserInfoTokenServices(googleResource().getUserInfoUri(), google().getClientId());
		  tokenServices.setRestTemplate(googleTemplate);
		  googleFilter.setTokenServices(tokenServices);
		  filters.add(googleFilter);

		  filter.setFilters(filters);
		  return filter;

		}
	
	@Bean
	  @ConfigurationProperties("facebook.client")
	  public AuthorizationCodeResourceDetails facebook() {
	    return new AuthorizationCodeResourceDetails();
	  }
	@Bean
	  @ConfigurationProperties("google.client")
	  public AuthorizationCodeResourceDetails google() {
	    return new AuthorizationCodeResourceDetails();
	  }
	
	
	@Bean
	  @ConfigurationProperties("facebook.resource")
	  public ResourceServerProperties facebookResource() {
	    return new ResourceServerProperties();
	  }
	@Bean
	  @ConfigurationProperties("google.resource")
	  public ResourceServerProperties googleResource() {
	    return new ResourceServerProperties();
	  }


	@Bean
	public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
		FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<OAuth2ClientContextFilter>();
		registration.setFilter(filter);
		registration.setOrder(-100);
		return registration;
	}
}
