package br.com.contatos.api.config;

import br.com.contatos.api.entities.User;
import br.com.contatos.api.entities.enums.RoleEnum;
import br.com.contatos.api.repository.RoleRepository;
import br.com.contatos.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static br.com.contatos.api.constants.Messages.CREATED_USER_ADMIN;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final String username;
	private final String password;

	public AdminUserConfig(
		RoleRepository roleRepository,
		UserRepository userRepository,
		BCryptPasswordEncoder passwordEncoder,
		@Value("${auth.username}") String username,
		@Value("${auth.password}") String password
	) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.username = username;
		this.password = password;
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		var roleAdmin = roleRepository.findByName(RoleEnum.ADMIN.name().toLowerCase());
		var userAdmin = userRepository.findByUsername("admin");

		if (!userAdmin.isPresent()) {
			var user = new User();
			user.setUsername(this.username);
			user.setPasswordCrypt(this.password, this.passwordEncoder);
			user.setRoles(List.of(roleAdmin.get()));
			userRepository.save(user);
			System.out.println(CREATED_USER_ADMIN);
		}
	}
}
