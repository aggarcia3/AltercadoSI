package com.github.aggarcia3.altercadosi.entidades;

import java.util.ArrayList;
import java.util.Arrays;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
	private String nombre;

	@NonNull @NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha = new Date();

	@Column(nullable = false)
	@Positive
	private int numInvolucrados;

	@NonNull @NotNull
	@NotBlank
	@Max(2048)
	private String descripcion;

	@ManyToOne(optional = false, cascade = CascadeType.ALL)
	@NonNull @NotNull
	private Lugar lugar;

	@OneToMany(mappedBy = "altercado", cascade = CascadeType.ALL)
	@NonNull @NotNull
	private final Collection<Victima> victimas = new ArrayList<>(); 

	@OneToOne(optional = false, cascade = CascadeType.ALL)
	@NonNull @NotNull
	private Arma arma;

	public void addVictimas(@NonNull Collection<Victima> victimas) {
		for (Victima v : victimas) {
			if (v != null) {
				this.victimas.add(v);
			}
		}
	}

	public void addVictimas(@NonNull Victima... victimas) {
		addVictimas(Arrays.asList(victimas));
	}
}
