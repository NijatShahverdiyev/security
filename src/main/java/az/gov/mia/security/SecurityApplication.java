package az.gov.mia.security;

import az.gov.mia.security.auth.domain.Authority;
import az.gov.mia.security.auth.domain.User;
import az.gov.mia.security.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class SecurityApplication implements CommandLineRunner {

	private final PasswordEncoder encoder;
	private final UserRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user  =new User();
		user.setUsername("admin");
		user.setPassword(encoder.encode("12345"));
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setEnabled(true);
		user.setCredentialsNonExpired(true);

		Authority adminRole = new Authority();
		adminRole.setAuthority("ROLE_ADMIN");

		Authority userRole = new Authority();
		userRole.setAuthority("ROLE_USER");

		user.setAuthorities(List.of(adminRole, userRole));
		repository.save(user);
	}
}
