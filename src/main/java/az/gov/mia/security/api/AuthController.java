package az.gov.mia.security.api;

import az.gov.mia.security.dto.RefreshTokenDto;
import az.gov.mia.security.dto.SignInRequestDto;
import az.gov.mia.security.dto.SignInResponseDto;
import az.gov.mia.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/sign-in")
    public SignInResponseDto signIn(@RequestBody @Valid SignInRequestDto requestDto){
        return authService.signIn(requestDto);
    }

    @PostMapping("/auth/refresh-token")
    public SignInResponseDto signIn(@RequestBody @Valid RefreshTokenDto refreshTokenDto){
        return authService.authWithRefreshToken(refreshTokenDto);
    }
}
