package com.github.aggarcia3.altercadosi.controladores.conversores;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.github.aggarcia3.altercadosi.entidades.Lugar;
import com.github.aggarcia3.altercadosi.entidades.repositorios.LugarRepository;

/**
 * Convierte un identificador de lugar al lugar correspondiente.
 *
 * @author Alejandro González García
 */
@Component
public class ConversorLugar implements Converter<Integer, Lugar> {
	@Autowired
	private LugarRepository repositorioLugares;

	@Override
	public Lugar convert(final Integer id) {
		final Optional<Lugar> lugar;
		try {
			lugar = repositorioLugares.findById(id);
		} catch (final Exception exc) {
			throw new IllegalArgumentException(exc);
		}

		if (lugar.isPresent()) {
			return lugar.get();
		} else {
			throw new IllegalArgumentException("No existe un lugar con esos datos");
		}
	}
}
