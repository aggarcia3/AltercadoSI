package com.github.aggarcia3.altercadosi.servicios;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
public final class ResultadoBusquedaEntidadesPorCondicionBinaria<T> {
	@Getter @NonNull
	private final List<T> entidadesSinCondicion;
	@Getter @NonNull
	private final List<T> entidadesConCondicion;
}
