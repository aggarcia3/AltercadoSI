package com.github.aggarcia3.altercadosi.entidades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.JOINED)
public final class ArmaRegistrada extends Arma {
	@Column(unique = true, nullable = false)
	@Positive
	private int numSerie;

	@Column(unique = true, nullable = false)
	@Positive
	private int numLicencia;

	@NonNull @NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaExpiracionRevista = new Date();

	@OneToMany(mappedBy = "armaRegistrada", cascade = CascadeType.ALL)
	@NonNull @NotNull
	private final Collection<SucesoArmaRegistrada> sucesos = new ArrayList<>();
}
