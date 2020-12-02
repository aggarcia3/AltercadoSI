package com.github.aggarcia3.altercadosi.entidades;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
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
public final class Altercado {
	@Id @EqualsAndHashCode.Exclude
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(unique = true, nullable = false, length = 128)
	@NonNull @NotNull
	@Size(min = 32, max = 128)
	private String nombre;
	@Column(nullable = false)
	@NonNull @NotNull
	private Date fecha;
	@Column(nullable = false)
	@Min(0)
	private int numInvolucrados;
	@Column(unique = true, nullable = false, length = 2048)
	@NonNull @NotNull
	@Size(min = 32, max = 2048)
	private String descripcion;

	@OneToOne(optional = false)
	@NonNull @NotNull
	private Lugar lugar;
}
