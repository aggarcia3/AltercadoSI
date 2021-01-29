package com.github.aggarcia3.altercadosi.controladores.conversores;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.github.aggarcia3.altercadosi.entidades.ArmaRegistrada;
import com.github.aggarcia3.altercadosi.entidades.repositorios.ArmaRegistradaRepository;

/**
 * Convierte un código de prueba de arma registrada al arma registrada correspondiente.
 *
 * @author Alejandro González García
 */
@Component
public class ConversorArmaRegistrada implements Converter<Integer, ArmaRegistrada> {
	@Autowired
	private ArmaRegistradaRepository repositorioArmasRegistradas;

	@Override
	public ArmaRegistrada convert(final Integer codigoPrueba) {
		final Optional<ArmaRegistrada> armaRegistrada;
		try {
			armaRegistrada = repositorioArmasRegistradas.findById(codigoPrueba);
		} catch (final Exception exc) {
			throw new IllegalArgumentException(exc);
		}

		if (armaRegistrada.isPresent()) {
			return armaRegistrada.get();
		} else {
			throw new IllegalArgumentException("No existe un arma registrada con ese código de prueba");
		}
	}
}
