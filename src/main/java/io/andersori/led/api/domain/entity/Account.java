package io.andersori.led.api.domain.entity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "account")
public class Account extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
	private Long accountId;

	@Column(name = "name", nullable = false, length = 250)
	@NotNull(message = "An account must have a name.")
	@Size(min = 1, max = 250, message = "Name must be at least 1 character and at most 250.")
	private String name;

	@Column(name = "username", nullable = false, unique = true, length = 150)
	@NotNull(message = "An account must have a username.")
	@Size(min = 1, max = 150, message = "Username must be at least 1 character and at most 250.")
	private String username;

	@Column(name = "password", nullable = false)
	@NotNull(message = "An account must have a password.")
	private String password;

	@Column(name = "last_login", nullable = true)
	private LocalDateTime lastLogin;

	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.EAGER, targetClass = RoleLed.class)
	@CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role", length = 10, nullable = false)
	private Set<RoleLed> roles;

	public Account() {
		this.roles = new HashSet<RoleLed>(Arrays.asList(RoleLed.DEFAULT));
	}

}
