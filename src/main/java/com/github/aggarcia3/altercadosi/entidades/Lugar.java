package com.github.aggarcia3.altercadosi.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(
	columnNames = { "codigoPostal", "localidad" }
))
public final class Lugar implements Serializable {
	private static final long serialVersionUID = -3975491801510522700L;

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
	private final Collection<Altercado> altercados = new ArrayList<>();

	/**
	 * Crea un nuevo lugar a partir de su código postal y localidad.
	 *
	 * @param codigoPostal El código postal del lugar.
	 * @param localidad    La localidad del lugar.
	 */
	public Lugar(final int codigoPostal, @NonNull final String localidad) {
		this.codigoPostal = codigoPostal;
		this.localidad = localidad;
	}
}
