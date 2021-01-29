package com.github.aggarcia3.altercadosi.controladores.conversores;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.github.aggarcia3.altercadosi.entidades.Arma;
import com.github.aggarcia3.altercadosi.entidades.repositorios.ArmaRepository;

/**
 * Convierte un código de prueba de arma al arma registrada correspondiente.
 *
 * @author Alejandro González García
 */
@Component
public class ConversorArma implements Converter<Integer, Arma> {
	@Autowired
	private ArmaRepository repositorioArmas;

	@Override
	public Arma convert(final Integer codigoPrueba) {
		final Optional<Arma> arma;
		try {
			arma = repositorioArmas.findById(codigoPrueba);
		} catch (final Exception exc) {
			throw new IllegalArgumentException(exc);
		}

		if (arma.isPresent()) {
			return arma.get();
		} else {
			throw new IllegalArgumentException("No existe un arma registrada con ese código de prueba");
		}
	}
}
