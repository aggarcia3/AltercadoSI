package com.github.aggarcia3.altercadosi.entidades.repositorios;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.github.aggarcia3.altercadosi.entidades.Altercado;

@DataJpaTest
class RepositoriosTests {
	@Autowired
	private AltercadoRepositorio repositorioAltercados;

	@Test
	void creacionAltercados() {
		final Altercado altercado = new Altercado();
		repositorioAltercados.save(altercado);
		assertTrue(true);
	}
}
