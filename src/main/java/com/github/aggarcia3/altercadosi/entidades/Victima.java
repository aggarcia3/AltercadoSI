package com.github.aggarcia3.altercadosi.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Victima implements Serializable {
	private static final long serialVersionUID = 5229518907068023098L;

	@Id
	@NonNull @NotNull
	@NotBlank
	@Size(min = 1, max = 128)
	private String nombreCompleto;

	@NonNull @NotNull
	@Temporal(TemporalType.DATE)
	@PastOrPresent
	@DateTimeFormat(iso = ISO.DATE)
	private Date fechaNacimiento;

	@Enumerated(EnumType.STRING)
	@NonNull @NotNull
	private Genero genero;

	@Enumerated(EnumType.STRING)
	@NonNull @NotNull
	private Raza raza;

	@ManyToOne(optional = false)
	@NonNull @NotNull
	private Altercado altercado = new Altercado();

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
