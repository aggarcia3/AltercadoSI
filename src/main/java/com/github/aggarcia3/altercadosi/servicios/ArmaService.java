package com.github.aggarcia3.altercadosi.servicios;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.aggarcia3.altercadosi.entidades.Arma;

import lombok.NonNull;

@Service
public class ArmaService extends AbstractService<Arma, Integer> {
	@Autowired
	public ArmaService(@NonNull final JpaRepository<Arma, Integer> repositorioEntidad) {
		super(repositorioEntidad);
	}

	public final ResultadoBusquedaEntidadesPorCondicionBinaria<Arma> todasClasificadasPorAltercados() {
		final List<Arma> armasSinAltercados = new ArrayList<>();
		final List<Arma> armasConAltercados = new ArrayList<>();

		final Iterator<Arma> iter = repositorioEntidad.findAll().iterator();
		while (iter.hasNext()) {
			final Arma arma = iter.next();

			if (arma.getAltercados().isEmpty()) {
				armasSinAltercados.add(arma);
			} else {
				armasConAltercados.add(arma);
			}

			try {
				iter.remove();
			} catch (final UnsupportedOperationException ignored) {
				// Hemos intentado ser conservativos con la memoria
			}
		}

		return new ResultadoBusquedaEntidadesPorCondicionBinaria<>(
			Collections.unmodifiableList(armasSinAltercados),
			Collections.unmodifiableList(armasConAltercados)
		);
	}
}
