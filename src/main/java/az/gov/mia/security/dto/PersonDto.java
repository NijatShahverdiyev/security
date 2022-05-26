package az.gov.mia.security.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PersonDto {

    @NotBlank
    private String name;

    @NotNull
    private byte age;
}
