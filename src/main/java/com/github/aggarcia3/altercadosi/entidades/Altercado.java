package com.github.aggarcia3.altercadosi.entidades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
public final class Altercado {
	@Id @EqualsAndHashCode.Exclude
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(unique = true)
	@NonNull @NotNull
	@NotBlank
	@Max(128)
	private String nombre = null;

	@NonNull @NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha = new Date();

	@Column(nullable = false)
	@Min(1)
	private int numInvolucrados = 1;

	@NonNull @NotNull
	@NotBlank
	@Size(min = 32, max = 2048)
	private String descripcion = null;

	@ManyToOne(optional = false)
	@NonNull @NotNull
	private Lugar lugar = null;

	@OneToMany(mappedBy = "altercado", cascade = CascadeType.ALL)
	@NonNull @NotNull
	private final Collection<Victima> victimas = new ArrayList<>(); 

	@OneToOne(optional = false)
	@NonNull @NotNull
	private Arma arma = null;
}
