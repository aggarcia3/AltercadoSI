package com.github.aggarcia3.altercadosi.entidades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public final class ArmaRegistrada extends Arma {
	private static final long serialVersionUID = -8256051745419361279L;

	@Column(unique = true, nullable = false)
	@Positive
	private int numSerie;

	@Column(unique = true, nullable = false)
	@Positive
	private int numLicencia;

	@NonNull @NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fechaExpiracionRevista = new Date();

	@OneToMany(mappedBy = "armaRegistrada", cascade = CascadeType.ALL)
	@NonNull @NotNull
	private final Collection<SucesoArmaRegistrada> sucesos = new ArrayList<>();

	/**
	 * Crea una nueva arma registrada.
	 *
	 * @param costeEstimado          El coste estimado del arma.
	 * @param nombre                 El nombre del arma.
	 * @param numSerie               El número de serie del arma.
	 * @param numLicencia            El número de licencia del arma.
	 * @param fechaExpiracionRevista La fecha de expiración de la revista (revisión)
	 *                               del arma.
	 */
	public ArmaRegistrada(
		final float costeEstimado, @NonNull final String nombre, final int numSerie,
		final int numLicencia, @NonNull final Date fechaExpiracionRevista
	) {
		super(costeEstimado, nombre);
		this.numSerie = numSerie;
		this.numLicencia = numLicencia;
		this.fechaExpiracionRevista = fechaExpiracionRevista;
	}
}
