package com.github.aggarcia3.altercadosi.entidades;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DuplicateKeyException;

import com.github.aggarcia3.altercadosi.entidades.repositorios.AltercadoRepository;

import static com.github.aggarcia3.altercadosi.entidades.Victima.Genero;
import static com.github.aggarcia3.altercadosi.entidades.Victima.Raza;

/**
 * Comprueba que la capa de persistencia de la aplicación funcione
 * correctamente.
 *
 * @author Alejandro González García
 */
@DataJpaTest
public class TestsPersistencia {
	private static final Random PRNG = new Random();

	@Autowired
	private AltercadoRepository repositorioAltercados;

	/**
	 * Crea y persiste el número especificado de altercados, aproximadamente.
	 *
	 * @param numAltercados El número de altercados a persistir.
	 */
	private void crearAltercados(int numAltercados) {
		Calendar fechaIns = Calendar.getInstance();

		for (int i = 0; i < numAltercados; ++i) {
			fechaIns.add(Calendar.MONTH, PRNG.nextInt(25));

			float costeEstimado = Math.round(PRNG.nextFloat() / 20) * 100.0f;

			final Altercado altercado = new Altercado(
				"Altercado " + i, Date.from(Instant.now()),
				1 + PRNG.nextInt(100), "Descripción de altercado " + i,
				new Lugar(1000 + PRNG.nextInt(51001), "Aquí"), new ArmaRegistrada(
					costeEstimado, "Arma de altercado " + i, i + 50000, i + 100000,
					Date.from(fechaIns.toInstant())
				)
			);

			fechaIns = Calendar.getInstance();

			final ArmaRegistrada armaRegistrada = (ArmaRegistrada) altercado.getArma();

			fechaIns.add(Calendar.YEAR, -18 - PRNG.nextInt(21));

			altercado.getVictimas().add(new Victima(
				"Nombre de víctima " + i, Date.from(fechaIns.toInstant()),
				Genero.OTRO, Raza.OTRA, altercado
			));

			fechaIns = Calendar.getInstance();

			armaRegistrada.getAltercados().add(altercado);

			armaRegistrada.getSucesos().add(new SucesoArmaRegistrada(
				"Suceso de arma registrada " + i, "Descripción de suceso",
				armaRegistrada
			));

			try {
				repositorioAltercados.save(altercado);
			} catch (final DuplicateKeyException exc) {
				// Ignorar claves primarias duplicadas, que podrían pasar si se generan dos
				// pares de datos aleatorios iguales
			}
		}

		repositorioAltercados.flush();
	}

	/**
	 * Comprueba que las entidades se han definido correctamente y es posible
	 * crearlas en la BD.
	 */
	@Test
	void creacionAltercados() {
		crearAltercados(250);
		assertTrue(true);
	}
}
