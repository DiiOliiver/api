package br.com.contatos.api.entities;

import br.com.contatos.api.dto.LoginRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_users")
public class User implements Serializable, UserDetails {
	@Id
	@NotNull
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
	private UUID id;

	private String username;
	private String password;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(
		name = "tb_users_roles",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private List<Role> roles;

	@JsonIgnore
	@CreationTimestamp
	protected Date createdAt;

	@JsonIgnore
	@UpdateTimestamp
	protected Date updatedAt;

	public void setPasswordCrypt(String password, BCryptPasswordEncoder passwordEncoder) {
		this.password = passwordEncoder.encode(password);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public boolean isLoginCorrect(LoginRequestDTO loginRequest, PasswordEncoder passwordEncoder) {
		return passwordEncoder.matches(loginRequest.getPassword(), this.password);
	}
}
