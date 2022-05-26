package az.gov.mia.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthFilterConfigAdapter authFilterConfigAdapter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.apply(authFilterConfigAdapter);
        http.authorizeRequests()
                .antMatchers("/driver-license").permitAll()
                .antMatchers("/demo").permitAll()
                .antMatchers("/auth/sign-in").permitAll()
                .antMatchers("/auth/refresh-token").permitAll()
                .antMatchers("/demo-user").hasAnyRole("USER", "ADMIN")
                .antMatchers("/demo-admin").hasAnyRole("ADMIN")
                .and()
                .csrf().disable()
                .httpBasic();
        super.configure(http);
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
