package com.pwa.project.abyss;

import com.pwa.project.abyss.components.UserManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class AbyssSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
         * http
         * .authorizeRequests().antMatchers("/*").permitAll()
         * .and()
         * .authorizeRequests().antMatchers("/h2-console/**").permitAll()
         * ;
         * http.csrf().ignoringAntMatchers("/h2-console/**");
         * http.headers().frameOptions().sameOrigin();
         */

        http
                .authorizeRequests().antMatchers("/*.css", // Authorize request for everyone
                        "/*.js",
                        "/images/*",
                        "/inscription",
                        "/connexion",
                        "/user/new",
                        "login",
                        "/login",
                        "/font/*",
                        "/css/**",
                        "/thymeleafJS/**",
                        "/vueJS/**",
                        "/message-rest",
                        "/appvue/*")
                .permitAll()
                .and()
                // Authorize request for admin page if you are admin
                // .authorizeRequests().antMatchers("/admin").hasRole("ADMIN")
                // .and()
                // Authorize request if you are authenticate
                .authorizeRequests().antMatchers("/**").authenticated()

                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error=true")
                .and()
                .logout()
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")

        ;

        // Allow all the user to see the console
        http.csrf().ignoringAntMatchers("/h2-console/**");
        http.headers().frameOptions().sameOrigin();

        // Fix the CORS error / Probably need to change it
        http.csrf().disable();
        http.cors().disable();
    }

    @Autowired
    UserManagement userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder)
                .withUser("user").password(passwordEncoder.encode("123456")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder.encode("123456")).roles("USER", "ADMIN");
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
