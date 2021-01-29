package com.github.aggarcia3.altercadosi.controladores;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.github.aggarcia3.altercadosi.entidades.Arma;
import com.github.aggarcia3.altercadosi.entidades.ArmaRegistrada;
import com.github.aggarcia3.altercadosi.entidades.SucesoArmaRegistrada;
import com.github.aggarcia3.altercadosi.servicios.ArmaService;
import com.github.aggarcia3.altercadosi.servicios.SucesoArmaRegistradaService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * Controlador de Spring MVC para la gestión de armas.
 *
 * @author Alejandro González García
 */
@Controller
@RequestMapping("/armas")
public final class ArmaController {
	private static final String RAIZ_CONTROLADOR = MethodHandles.lookup().lookupClass()
		.getDeclaredAnnotation(RequestMapping.class).value()[0];

	private static final String REDIRECCION_RAIZ_CONTROLADOR = "redirect:" + RAIZ_CONTROLADOR;

	@Autowired
	private ArmaService serviciosArmas;
	@Autowired
	private SucesoArmaRegistradaService serviciosSucesosArmas;

	// Armas

	@GetMapping({"", "mostrarTodas"})
	public String mostrarListaArmas(final Model modeloVista) {
		modeloVista.addAttribute("armas", serviciosArmas.todasLasInstancias());
		return "armas/mostrar_todas";
	}

	@GetMapping({"{codigoPrueba}", "{codigoPrueba}/ver"})
	public String mostrarArma(@PathVariable final int codigoPrueba, final Model modeloVista) {
		return ejecutarMuestraVistaArma(Optional.of(codigoPrueba), VistaArmaUnica.VER_DETALLE, modeloVista);
	}

	@GetMapping("crear")
	public String mostrarCreacionArma(final Model modeloVista) {
		return ejecutarMuestraVistaArma(Optional.empty(), VistaArmaUnica.CREAR, modeloVista);
	}

	@PostMapping("crear/noRegistrada")
	public String crearArmaNoRegistrada(
		@Valid @ModelAttribute final Arma arma, final BindingResult resultado,
		final Model modeloVista
	) {
		return ejecutarModificacionArma(VistaArmaUnica.CREAR, arma, resultado, modeloVista);
	}

	@PostMapping("crear/registrada")
	public String crearArmaRegistrada(
		@Valid @ModelAttribute final ArmaRegistrada armaRegistrada, final BindingResult resultado,
		final Model modeloVista
	) {
		return ejecutarModificacionArma(VistaArmaUnica.CREAR, armaRegistrada, resultado, modeloVista);
	}

	@GetMapping("{codigoPrueba}/editar")
	public String mostrarEdicionArma(@PathVariable final int codigoPrueba, final Model modeloVista) {
		return ejecutarMuestraVistaArma(Optional.of(codigoPrueba), VistaArmaUnica.EDITAR, modeloVista);
	}

	@PostMapping("{codigoPrueba}/editar")
	public String editarArma(
		@Valid @ModelAttribute final Arma arma, final BindingResult resultado,
		final Model modeloVista
	) {
		return ejecutarModificacionArma(VistaArmaUnica.EDITAR, arma, resultado, modeloVista);
	}

	@GetMapping("{codigoPrueba}/eliminar")
	public String eliminarArma(@PathVariable final int codigoPrueba, final Model modeloVista) {
		if (serviciosArmas.eliminarPorClavePrimaria(codigoPrueba)) {
			modeloVista.addAttribute("tituloMensaje", "Arma borrada");
			modeloVista.addAttribute(
				"mensaje",
				"Se ha borrado el arma seleccionada y su información vinculada."
			);
		} else {
			modeloVista.addAttribute("tituloMensaje", "Arma ya borrada");
			modeloVista.addAttribute(
				"mensaje",
				"El arma especificada ya ha sido borrada."
			);
		}

		return mostrarListaArmas(modeloVista);
	}

	// Sucesos de armas

	@GetMapping({"sucesos/{id}", "sucesos/{id}/ver"})
	public String mostrarSucesoArma(@PathVariable final int id, final Model modeloVista) {
		return ejecutarMuestraVistaSucesoArma(Optional.of(id), VistaSucesoArmaUnica.VER_DETALLE, modeloVista, null);
	}

	@GetMapping("sucesos/crear/{codigoPrueba}")
	public String mostrarCreacionSucesoArma(@PathVariable final int codigoPrueba, final Model modeloVista) {
		return ejecutarMuestraVistaSucesoArma(Optional.empty(), VistaSucesoArmaUnica.CREAR, modeloVista, codigoPrueba);
	}

	@PostMapping("sucesos/crear")
	public String crearSucesoArma(
		@Valid @ModelAttribute final SucesoArmaRegistrada sucesoArma, final BindingResult resultado,
		final Model modeloVista
	) {
		return ejecutarModificacionSucesoArma(VistaSucesoArmaUnica.CREAR, sucesoArma, resultado, modeloVista);
	}

	@GetMapping("sucesos/{id}/editar")
	public String mostrarEdicionSucesoArma(@PathVariable final int id, final Model modeloVista) {
		return ejecutarMuestraVistaSucesoArma(Optional.of(id), VistaSucesoArmaUnica.EDITAR, modeloVista, null);
	}

	@PostMapping("sucesos/{id}/editar")
	public String editarSucesoArma(
		@Valid @ModelAttribute final SucesoArmaRegistrada sucesoArma, final BindingResult resultado,
		final Model modeloVista
	) {
		return ejecutarModificacionSucesoArma(VistaSucesoArmaUnica.EDITAR, sucesoArma, resultado, modeloVista);
	}

	@GetMapping("sucesos/{id}/eliminar")
	public String eliminarSucesoArma(@PathVariable final int id, final Model modeloVista) {
		if (serviciosSucesosArmas.eliminarPorClavePrimaria(id)) {
			modeloVista.addAttribute("tituloMensaje", "Suceso de arma borrado");
			modeloVista.addAttribute(
				"mensaje",
				"Se ha borrado el suceso de arma seleccionado y su información vinculada."
			);
		} else {
			modeloVista.addAttribute("tituloMensaje", "Suceso de arma ya borrada");
			modeloVista.addAttribute(
				"mensaje",
				"El suceso de arma especificado ya ha sido borrado."
			);
		}

		return mostrarListaArmas(modeloVista);
	}

	// --------------------------
	// MÉTODOS AYUDANTES PRIVADOS
	// --------------------------

	private String ejecutarMuestraVistaArma(
		final Optional<Integer> codigoPrueba, final VistaArmaUnica vista, final Model modeloVista
	) {
		final Optional<Arma> arma = codigoPrueba.isPresent() ?
			serviciosArmas.obtenerPorClavePrimaria(codigoPrueba.get()) : Optional.empty();

		final boolean vistaCrear = vista == VistaArmaUnica.CREAR;
		String vistaMostrar = vista.getNombreVista();

		if ((!vistaCrear && arma.isPresent()) || (vistaCrear && !arma.isPresent())) {
			modeloVista.addAttribute("creando", vistaCrear);
			modeloVista.addAttribute("arma", arma.orElseGet(ArmaRegistrada::new));
		} else {
			modeloVista.addAttribute("tituloMensaje", vista.getTituloMensajeError());
			modeloVista.addAttribute("mensaje", "El arma escogida ya no existe.");
			vistaMostrar = mostrarListaArmas(modeloVista);
		}

		return vistaMostrar;
	}

	private String ejecutarModificacionArma(
		final VistaArmaUnica vista, final Arma arma, final BindingResult resultado,
		final Model modeloVista
	) {
		if (resultado.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El arma recibida no es válida");
		}

		serviciosArmas.guardar(arma);

		return REDIRECCION_RAIZ_CONTROLADOR;
	}

	private String ejecutarMuestraVistaSucesoArma(
		final Optional<Integer> id, final VistaSucesoArmaUnica vista, final Model modeloVista,
		final Integer codigoPruebaArma
	) {
		final Optional<SucesoArmaRegistrada> sucesoArma = id.isPresent() ?
			serviciosSucesosArmas.obtenerPorClavePrimaria(id.get()) : Optional.empty();

		final boolean vistaCrear = vista == VistaSucesoArmaUnica.CREAR;
		String vistaMostrar = vista.getNombreVista();
		final SucesoArmaRegistrada sucesoArmaVista = sucesoArma.orElseGet(SucesoArmaRegistrada::new);

		if ((!vistaCrear && sucesoArma.isPresent()) || (vistaCrear && !sucesoArma.isPresent())) {
			modeloVista.addAttribute("creando", vistaCrear);
			modeloVista.addAttribute("sucesoArma", sucesoArmaVista);
		} else {
			modeloVista.addAttribute("tituloMensaje", vista.getTituloMensajeError());
			modeloVista.addAttribute("mensaje", "El suceso de arma dado ya no existe.");
			vistaMostrar = mostrarListaArmas(modeloVista);
		}

		// Si no tenemos un suceso totalmente inicializado, pero sí sabemos el código
		// de prueba del arma al que pertenece, entonces establecerlo
		if (codigoPruebaArma != null && !sucesoArma.isPresent()) {
			sucesoArmaVista.getArmaRegistrada().setCodigoPrueba(codigoPruebaArma);
		}

		return vistaMostrar;
	}

	private String ejecutarModificacionSucesoArma(
		final VistaSucesoArmaUnica vista, final SucesoArmaRegistrada sucesoArma, final BindingResult resultado,
		final Model modeloVista
	) {
		String vistaMostrar = mostrarEdicionArma(sucesoArma.getArmaRegistrada().getCodigoPrueba(), modeloVista);
		final boolean errorEnArmaRegistrada = resultado.hasFieldErrors("armaRegistrada");

		if (resultado.hasErrors() && !errorEnArmaRegistrada) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El suceso de arma recibido no es válido");
		}

		if (!errorEnArmaRegistrada) {
			serviciosSucesosArmas.guardar(sucesoArma);
		} else {
			modeloVista.addAttribute("tituloMensaje", vista.getTituloMensajeError());
			modeloVista.addAttribute("mensaje", "El arma registrada asociada a este suceso ya no existe.");
			vistaMostrar = mostrarListaArmas(modeloVista);
		}

		return vistaMostrar;
	}

	// --------------
	// TIPOS DE DATOS
	// --------------

	@AllArgsConstructor
	private static enum VistaArmaUnica {
		CREAR("armas/crear_editar", "Error en la creación"),
		EDITAR("armas/crear_editar", "Error en la edición"),
		VER_DETALLE("armas/ver_detalle", "Error en la visualización");

		@Getter @NonNull
		private final String nombreVista;
		@Getter @NonNull
		private final String tituloMensajeError;
	}

	@AllArgsConstructor
	private static enum VistaSucesoArmaUnica {
		CREAR("armas/crear_editar_suceso", "Error en la creación"),
		EDITAR("armas/crear_editar_suceso", "Error en la edición"),
		VER_DETALLE("armas/ver_detalle_suceso", "Error en la visualización");

		@Getter @NonNull
		private final String nombreVista;
		@Getter @NonNull
		private final String tituloMensajeError;
	}
}
