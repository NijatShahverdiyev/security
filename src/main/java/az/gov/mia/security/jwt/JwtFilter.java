package az.gov.mia.security.jwt;

import az.gov.mia.security.jwt.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final String AUTH_HEADER = "Authorization";
    private final String BEARER_AUTH_HEADER = "Bearer";
    private final String TOKEN_AUTHORITIES = "authorities";
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("Auth header:  " + request.getHeader(AUTH_HEADER));
        Optional<Authentication> authentication = getAuthentication(request);
        if (authentication.isPresent()) {
            log.info("Authentication is : {}", authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication.get());
        } else {
            log.info("No Bearer Authentication");
        }
        filterChain.doFilter(request, response);
    }

    public Optional<Authentication> getAuthentication(HttpServletRequest req) {
        return Optional.ofNullable(req.getHeader(AUTH_HEADER))
                .filter(this::isBearerAuth)
                .flatMap(this::getAuthenticationBearer);
    }

    private boolean isBearerAuth(String header) {
        return header.toLowerCase().startsWith(BEARER_AUTH_HEADER.toLowerCase());
    }

    private Optional<Authentication> getAuthenticationBearer(String header) {
        String token = header.substring(BEARER_AUTH_HEADER.length()).trim();
        log.info("Token is :" + token);
        Claims claims = jwtService.parseToken(token);
        log.trace("The claims parsed {}", claims);
        return Optional.of(getAuthenticationBearer(claims));
    }

    private Authentication getAuthenticationBearer(Claims claims) {
        List<?> authorities = claims.get(TOKEN_AUTHORITIES, List.class);
        List<GrantedAuthority> authorityList;
        if (authorities != null) {
            authorityList = authorities
                    .stream()
                    .map(a -> new SimpleGrantedAuthority(a.toString()))
                    .collect(Collectors.toList());
        } else {
            authorityList = List.of();
        }
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorityList);
    }

}
