package com.github.aggarcia3.altercadosi.servicios;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.aggarcia3.altercadosi.entidades.Altercado;

import lombok.NonNull;

@Service
public class AltercadoService extends AbstractService<Altercado, String> {
	@Autowired
	public AltercadoService(@NonNull final JpaRepository<Altercado, String> repositorioEntidad) {
		super(repositorioEntidad);
	}

	public final ResultadoBusquedaEntidadesPorCondicionBinaria<Altercado> todosClasificadosPorVictimas() {
		final List<Altercado> altercadosSinVictimas = new ArrayList<>();
		final List<Altercado> altercadosConVictimas = new ArrayList<>();

		final Iterator<Altercado> iter = repositorioEntidad.findAll().iterator();
		while (iter.hasNext()) {
			final Altercado altercado = iter.next();

			if (altercado.getVictimas().isEmpty()) {
				altercadosSinVictimas.add(altercado);
			} else {
				altercadosConVictimas.add(altercado);
			}

			try {
				iter.remove();
			} catch (final UnsupportedOperationException ignored) {
				// Hemos intentado ser conservativos con la memoria
			}
		}

		return new ResultadoBusquedaEntidadesPorCondicionBinaria<>(
			Collections.unmodifiableList(altercadosSinVictimas),
			Collections.unmodifiableList(altercadosConVictimas)
		);
	}
}
