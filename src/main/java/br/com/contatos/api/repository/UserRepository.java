package br.com.contatos.api.repository;

import br.com.contatos.api.entities.Contact;
import br.com.contatos.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByUsername(String username);
	Optional<User> findById(UUID userId);
}
