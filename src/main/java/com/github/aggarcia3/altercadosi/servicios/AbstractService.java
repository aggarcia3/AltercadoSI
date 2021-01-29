package com.github.aggarcia3.altercadosi.servicios;

import java.util.Collection;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * Proporciona funcionalidad común para diversos servicios.
 *
 * @author Alejandro González García
 *
 * @param <E>  El tipo de entidad a la que proporcionar servicios.
 * @param <ID> El tipo de clave primaria de la entidad.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractService<E, ID> {
	@NonNull
	protected final JpaRepository<E, ID> repositorioEntidad;

	public final Collection<E> todasLasInstancias() {
		return repositorioEntidad.findAll();
	}

	public final Optional<E> obtenerPorClavePrimaria(final ID clave) {
		return repositorioEntidad.findById(clave);
	}

	public final boolean guardar(final E entidad) {
		boolean noRestringidoPorIntegridad = true;

		try {
			repositorioEntidad.save(entidad);
		} catch (final DataIntegrityViolationException exc) {
			noRestringidoPorIntegridad = false;
		}

		return noRestringidoPorIntegridad;
	}

	public final boolean eliminarPorClavePrimaria(final ID clave) {
		boolean lugarBorrado = true;

		try {
			repositorioEntidad.deleteById(clave);
		} catch (final EmptyResultDataAccessException exc) {
			lugarBorrado = false;
		}

		return lugarBorrado;
	}

	public final boolean hayInstanciasRegistradas() {
		return repositorioEntidad.count() > 0;
	}
}
