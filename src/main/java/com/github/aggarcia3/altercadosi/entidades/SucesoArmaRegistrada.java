package com.github.aggarcia3.altercadosi.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
public final class SucesoArmaRegistrada implements Serializable {
	private static final long serialVersionUID = -3919808552164528882L;

	@Id @EqualsAndHashCode.Exclude
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(unique = true)
	@NonNull @NotNull
	@NotBlank
	@Size(max = 128)
	private String nombre;

	@NonNull @NotNull
	@Size(max = 8192)
	private String descripcion;

	@ManyToOne(optional = false)
	@NonNull @NotNull
	private ArmaRegistrada armaRegistrada = new ArmaRegistrada();

	/**
	 * Crea un nuevo suceso a nombre de un arma registrada.
	 *
	 * @param nombre         El nombre del suceso.
	 * @param descripcion    La descripción del suceso.
	 * @param armaRegistrada El arma registrada vinculada con este suceso.
	 */
	public SucesoArmaRegistrada(
		@NonNull final String nombre, @NonNull final String descripcion,
		@NonNull final ArmaRegistrada armaRegistrada
	) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.armaRegistrada = armaRegistrada;
	}
}
