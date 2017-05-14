package cn.cherish.blog.config;

import cn.cherish.blog.security.CustomUserDetailsService;
import cn.cherish.blog.security.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 安全框架配置
 * Created by Cherish on 2017/4/13.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();// csrf:Cross-site doQuery forgery跨站请求伪造
        http
            .authorizeRequests()
                .antMatchers("/","/login","/logout","/validateCode","/**/favicon.ico",
                        "/druid/**","/static/**", "/css/**", "/js/**", "/images/**", "/tools/**",
                        "/api/**","/imageDownload*").permitAll()//允许所有
                .anyRequest().permitAll()
                .and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/article")
                .successHandler(loginSuccessHandler())
                .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
                .invalidateHttpSession(true)  ;
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler(){
        return new LoginSuccessHandler();
    }

    @Bean
    public UserDetailsService customUserService() {
        return new CustomUserDetailsService();
    }

    /**
     * 设置用户密码的加密方式为SHA1加密
     */
    @Bean
    public ShaPasswordEncoder passwordEncoder() {
        return new ShaPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(customUserService())
            .passwordEncoder(passwordEncoder());
    }


}
