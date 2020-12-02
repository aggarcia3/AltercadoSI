package com.github.aggarcia3.altercadosi.entidades;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(
	columnNames = { "codigoPostal", "localidad", "comunidadAutonoma" }
))
public final class Lugar {
	@Id @EqualsAndHashCode.Exclude
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false)
	@Min(1000) @Max(52000)
	private int codigoPostal;
	@Column(nullable = false, length = 128)
	@NonNull @NotNull
	@NotBlank
	@Size(min = 1, max = 128)
	private String localidad;

	@OneToMany(mappedBy = "lugar", cascade = CascadeType.ALL)
	@NonNull @NotNull
	private final Set<Altercado> altercadosOcurridos = new HashSet<>();
}
