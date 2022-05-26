package az.gov.mia.security.service.impl;

import az.gov.mia.security.auth.domain.RefreshToken;
import az.gov.mia.security.auth.repository.RefreshTokenRepository;
import az.gov.mia.security.auth.repository.UserRepository;
import az.gov.mia.security.dto.RefreshTokenDto;
import az.gov.mia.security.dto.SignInRequestDto;
import az.gov.mia.security.dto.SignInResponseDto;
import az.gov.mia.security.jwt.JwtService;
import az.gov.mia.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public SignInResponseDto signIn(SignInRequestDto requestDto) {
        log.info("Authenticating user {}", requestDto.getUsername());
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(requestDto.getUsername(),
                requestDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("Authentication is {}", authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new SignInResponseDto(jwtService.issueToken(authentication), issueRefreshToken(authentication, null));
    }

    @Override
    public SignInResponseDto authWithRefreshToken(RefreshTokenDto refreshTokenDto) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenDto.getToken())
                .orElseThrow(() -> new RuntimeException("Unauthorized"));
        if (refreshToken.isValid() && refreshToken.getEat().after(new Date())) {
            UserDetails userDetails = userRepository.findByUsername(refreshToken.getUsername());
            if (userDetails.isAccountNonExpired()
                    && userDetails.isAccountNonLocked()
                    && userDetails.isCredentialsNonExpired()
                    && userDetails.isEnabled()) {
                UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(), null, userDetails.getAuthorities());
                refreshToken.setValid(false);
                refreshTokenRepository.save(refreshToken);
                return new SignInResponseDto(jwtService.issueToken(userPassAuthToken),
                        issueRefreshToken(userPassAuthToken, refreshToken.getId()));
            }
        }
        throw new RuntimeException("Unauthorized");
    }

    private RefreshTokenDto issueRefreshToken(Authentication authentication, Long previousRefreshTokenId) {
        Calendar date = Calendar.getInstance();
        long timeInSec = date.getTimeInMillis();
        Date afterAdd10Mins = new Date(timeInSec + (10 * 60 * 1000));
        RefreshToken refreshToken = RefreshToken
                .builder()
                .username(authentication.getName())
                .token(UUID.randomUUID().toString())
                .eat(afterAdd10Mins)
                .valid(true)
                .previousRefreshTokenId(previousRefreshTokenId)
                .build();
        refreshTokenRepository.save(refreshToken);
        return new RefreshTokenDto(refreshToken.getToken(), refreshToken.getEat());
    }

}
