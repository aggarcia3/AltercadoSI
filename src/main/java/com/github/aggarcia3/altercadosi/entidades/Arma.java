package com.github.aggarcia3.altercadosi.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public class Arma implements Serializable {
	private static final long serialVersionUID = -4821312394700203579L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int codigoPrueba;

	@NotNull
	@PositiveOrZero
	@Digits(fraction = 2, integer = 4)
	private float costeEstimado;

	@NonNull @NotNull
	@NotBlank @Size(max = 128)
	private String nombre = "N/D";

	@OneToMany(mappedBy = "arma")
	@NonNull @NotNull
	private final Collection<Altercado> altercados = new ArrayList<>();

	/**
	 * Crea un nuevo arma.
	 *
	 * @param costeEstimado El coste estimado del arma.
	 * @param nombre        El nombre del arma.
	 */
	public Arma(final float costeEstimado, @NonNull final String nombre) {
		this.costeEstimado = costeEstimado;
		this.nombre = nombre;
	}
}
