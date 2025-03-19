package br.com.contatos.api.repository;

import br.com.contatos.api.entities.Role;
import br.com.contatos.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(String name);
}
