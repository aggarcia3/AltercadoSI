package com.github.aggarcia3.altercadosi.servicios;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.aggarcia3.altercadosi.entidades.Lugar;

import lombok.NonNull;

@Service
public class LugarService extends AbstractService<Lugar, Integer> {
	@Autowired
	public LugarService(@NonNull final JpaRepository<Lugar, Integer> repositorioEntidad) {
		super(repositorioEntidad);
	}

	public final ResultadoBusquedaEntidadesPorCondicionBinaria<Lugar> todosClasificadosPorAltercados() {
		final List<Lugar> lugaresSinAltercados = new ArrayList<>();
		final List<Lugar> lugaresConAltercados = new ArrayList<>();

		final Iterator<Lugar> iter = repositorioEntidad.findAll().iterator();
		while (iter.hasNext()) {
			final Lugar lugar = iter.next();

			if (lugar.getAltercados().isEmpty()) {
				lugaresSinAltercados.add(lugar);
			} else {
				lugaresConAltercados.add(lugar);
			}

			try {
				iter.remove();
			} catch (final UnsupportedOperationException ignored) {
				// Hemos intentado ser conservativos con la memoria
			}
		}

		return new ResultadoBusquedaEntidadesPorCondicionBinaria<>(
			Collections.unmodifiableList(lugaresSinAltercados),
			Collections.unmodifiableList(lugaresConAltercados)
		);
	}
}
