package az.gov.mia.security.service.impl;

import az.gov.mia.security.dto.PersonDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DriverLicenseServiceImpl implements DriverLicenseService{
    @Override
    @PreAuthorize("#personDto.age >= 18")
    public String getDriverLicense(PersonDto personDto) {
        log.info("Getting driver license service");
        return "Driver License";
    }
}
