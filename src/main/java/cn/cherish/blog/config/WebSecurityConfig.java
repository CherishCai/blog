package cn.cherish.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by Cherish on 2017/1/9.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // csrf:Cross-site doQuery forgery跨站请求伪造
        http
            .authorizeRequests()
                .antMatchers("/","/login","/validateCode","/**/favicon.ico",
                        "/druid/**", "/css/**", "/js/**", "/images/**", "/tools/**",
                        "/weui/**","/api/**","/blog/**","/imageDownload*").permitAll()
                .anyRequest().permitAll()//允许所有
                .and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/article")
                .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("cherish").password("cherish").roles("ADMIN");
    }


}
