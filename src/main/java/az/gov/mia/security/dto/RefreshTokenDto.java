package az.gov.mia.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class RefreshTokenDto {
    private String token;
    private Date eat;
}
