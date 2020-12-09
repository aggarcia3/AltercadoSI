package com.github.aggarcia3.altercadosi.entidades;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class Victima {
	@Id
	@NonNull @NotNull
	@NotBlank
	@Size(min = 1, max = 128)
	private String nombreCompleto;

	@Temporal(TemporalType.DATE)
	@NonNull @NotNull
	private Date fechaNacimiento;

	@Enumerated(EnumType.STRING)
	@NonNull @NotNull
	private Genero genero;

	@Enumerated(EnumType.STRING)
	@NonNull @NotNull
	private Raza raza;

	@ManyToOne(optional = false, cascade = CascadeType.ALL)
	@NonNull @NotNull
	private Altercado altercado;

	public static enum Genero {
		HOMBRE,
		MUJER,
		OTRO
	}

	public static enum Raza {
		BLANCO,
		ORIENTAL,
		NEGRO,
		AUSTRALOIDE,
		OTRA
	}
}
