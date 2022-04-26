package az.gov.mia.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInResponseDto {

    private String accessToken;
    private RefreshTokenDto refreshToken;
}
