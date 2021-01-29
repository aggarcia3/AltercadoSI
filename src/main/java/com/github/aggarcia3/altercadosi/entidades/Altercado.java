package com.github.aggarcia3.altercadosi.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Altercado implements Serializable {
	private static final long serialVersionUID = -1030103623631322395L;

	@Id
	@NonNull @NotNull
	@NotBlank
	@Size(max = 128)
	private String nombre = "N/D";

	@NonNull @NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@PastOrPresent
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecha = new Date();

	@Column(nullable = false)
	@Positive
	private int numInvolucrados;

	@NonNull @NotNull
	@Size(max = 8192)
	private String descripcion = "N/D";

	@ManyToOne(optional = false)
	@NonNull @NotNull
	private Lugar lugar = new Lugar();

	@OneToMany(mappedBy = "altercado", cascade = CascadeType.ALL)
	@NonNull @NotNull
	private final Collection<Victima> victimas = new ArrayList<>(); 

	@ManyToOne(optional = false)
	@NonNull @NotNull
	private Arma arma = new Arma();
}
