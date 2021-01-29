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

import com.github.aggarcia3.altercadosi.entidades.Lugar;
import com.github.aggarcia3.altercadosi.servicios.LugarService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * Controlador de Spring MVC para la gestión de lugares.
 *
 * @author Alejandro González García
 */
@Controller
@RequestMapping("/lugares")
public final class LugarController {
	private static final String RAIZ_CONTROLADOR = MethodHandles.lookup().lookupClass()
		.getDeclaredAnnotation(RequestMapping.class).value()[0];

	private static final String REDIRECCION_RAIZ_CONTROLADOR = "redirect:" + RAIZ_CONTROLADOR;

	private static final String MENSAJE_OTRO_CON_ESA_PK =
		"Ya hay otro lugar con esa localidad y código postal.";

	@Autowired
	private LugarService serviciosLugares;

	@GetMapping({"", "mostrarTodos"})
	public String mostrarListaLugares(final Model modeloVista) {
		modeloVista.addAttribute("lugares", serviciosLugares.todasLasInstancias());
		return "lugares/mostrar_todos";
	}

	@GetMapping({"{id}", "{id}/ver"})
	public String mostrarLugar(@PathVariable final int id, final Model modeloVista) {
		return ejecutarMuestraVistaLugar(Optional.of(id), VistaLugarUnica.VER_DETALLE, modeloVista);
	}

	@GetMapping("crear")
	public String mostrarCreacionLugar(final Model modeloVista) {
		return ejecutarMuestraVistaLugar(Optional.empty(), VistaLugarUnica.CREAR, modeloVista);
	}

	@PostMapping("crear")
	public String crearLugar(
		@Valid @ModelAttribute final Lugar lugar, final BindingResult resultado,
		final Model modeloVista
	) {
		return ejecutarModificacionLugar(VistaLugarUnica.CREAR, lugar, resultado, modeloVista);
	}

	@GetMapping("{id}/editar")
	public String mostrarEdicionLugar(@PathVariable final int id, final Model modeloVista) {
		return ejecutarMuestraVistaLugar(Optional.of(id), VistaLugarUnica.EDITAR, modeloVista);
	}

	@PostMapping("{id}/editar")
	public String editarLugar(
		@Valid @ModelAttribute final Lugar lugar, final BindingResult resultado,
		final Model modeloVista
	) {
		return ejecutarModificacionLugar(VistaLugarUnica.EDITAR, lugar, resultado, modeloVista);
	}

	@GetMapping("{id}/eliminar")
	public String eliminarLugar(@PathVariable final int id, final Model modeloVista) {
		if (serviciosLugares.eliminarPorClavePrimaria(id)) {
			modeloVista.addAttribute("tituloMensaje", "Lugar borrado");
			modeloVista.addAttribute(
				"mensaje",
				"Se ha borrado el lugar seleccionado y su información asociada."
			);
		} else {
			modeloVista.addAttribute("tituloMensaje", "Lugar ya borrado");
			modeloVista.addAttribute(
				"mensaje",
				"El lugar especificado ya ha sido borrado."
			);
		}

		return mostrarListaLugares(modeloVista);
	}

	// --------------------------
	// MÉTODOS AYUDANTES PRIVADOS
	// --------------------------

	private String ejecutarMuestraVistaLugar(
		final Optional<Integer> id, final VistaLugarUnica vista, final Model modeloVista
	) {
		final Optional<Lugar> lugar = id.isPresent() ?
			serviciosLugares.obtenerPorClavePrimaria(id.get()) : Optional.empty();

		final boolean vistaCrear = vista == VistaLugarUnica.CREAR;
		String vistaMostrar = vista.getNombreVista();

		if ((!vistaCrear && lugar.isPresent()) || (vistaCrear && !lugar.isPresent())) {
			modeloVista.addAttribute("creando", vistaCrear);
			modeloVista.addAttribute("lugar", lugar.orElseGet(Lugar::new));
		} else {
			modeloVista.addAttribute("tituloMensaje", vista.getTituloMensajeError());
			modeloVista.addAttribute("mensaje", "El lugar escogido ya no existe.");
			vistaMostrar = mostrarListaLugares(modeloVista);
		}

		return vistaMostrar;
	}

	private String ejecutarModificacionLugar(
		final VistaLugarUnica vista, final Lugar lugar, final BindingResult resultado,
		final Model modeloVista
	) {
		String vistaMostrar = REDIRECCION_RAIZ_CONTROLADOR;

		if (resultado.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El lugar recibido no es válido");
		}

		if (!serviciosLugares.guardar(lugar)) {
			modeloVista.addAttribute("creando", false);
			modeloVista.addAttribute("tituloMensaje", vista.getTituloMensajeError());
			modeloVista.addAttribute("mensaje", MENSAJE_OTRO_CON_ESA_PK);
			vistaMostrar = VistaLugarUnica.EDITAR.getNombreVista();
		}

		return vistaMostrar;
	}

	// --------------
	// TIPOS DE DATOS
	// --------------

	@AllArgsConstructor
	private static enum VistaLugarUnica {
		CREAR("lugares/crear_editar", "Error en la creación"),
		EDITAR("lugares/crear_editar", "Error en la edición"),
		VER_DETALLE("lugares/ver_detalle", "Error en la visualización");

		@Getter @NonNull
		private final String nombreVista;
		@Getter @NonNull
		private final String tituloMensajeError;
	}
}
