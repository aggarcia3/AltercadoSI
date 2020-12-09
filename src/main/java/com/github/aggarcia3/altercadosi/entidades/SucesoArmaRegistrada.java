package com.github.aggarcia3.altercadosi.entidades;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Entity
@Data
public final class SucesoArmaRegistrada {
	@Id @EqualsAndHashCode.Exclude
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(unique = true)
	@NonNull @NotNull
	@NotBlank
	@Max(128)
	private String nombre;

	@NonNull @NotNull
	@NotBlank
	@Max(2048)
	private String descripcion;

	@ManyToOne(optional = false, cascade = CascadeType.ALL)
	@NonNull @NotNull
	private ArmaRegistrada armaRegistrada;
}
