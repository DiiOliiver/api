package br.com.contatos.api.repository;

import br.com.contatos.api.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
	boolean existsByEmail(String email);
}
