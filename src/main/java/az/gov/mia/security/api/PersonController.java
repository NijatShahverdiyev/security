package az.gov.mia.security.api;

import az.gov.mia.security.dto.PersonDto;
import az.gov.mia.security.service.impl.DriverLicenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PersonController {

    private final DriverLicenseService driverLicenseService;

    @PostMapping("/driver-license")
//    @PreAuthorize("hasRole('ADMIN')")
    public String getDriverLicense(@RequestBody @Valid PersonDto personDto) {
        return driverLicenseService.getDriverLicense(personDto);
    }
}
