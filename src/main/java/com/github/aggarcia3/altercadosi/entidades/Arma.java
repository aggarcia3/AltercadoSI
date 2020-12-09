package com.github.aggarcia3.altercadosi.entidades;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

import com.sun.istack.NotNull;

import lombok.Data;
import lombok.NonNull;

@Entity
@Data
public class Arma {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int codigoPrueba;

	@NonNull @NotNull
	@PositiveOrZero
	private BigDecimal costeEstimado;

	@NonNull @NotNull
	@NotBlank @Max(128)
	private String nombre;
}
