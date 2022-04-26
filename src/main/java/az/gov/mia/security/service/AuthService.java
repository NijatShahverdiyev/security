package az.gov.mia.security.service;

import az.gov.mia.security.dto.RefreshTokenDto;
import az.gov.mia.security.dto.SignInRequestDto;
import az.gov.mia.security.dto.SignInResponseDto;

public interface AuthService {

    SignInResponseDto signIn(SignInRequestDto requestDto);
    SignInResponseDto authWithRefreshToken(RefreshTokenDto refreshTokenDto);
}
