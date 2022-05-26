package az.gov.mia.security.config;

import az.gov.mia.security.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JwtAuthFilterConfigAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtFilter jwtFilter;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        log.trace("Added auth request filter");
        builder.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
