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

import com.github.aggarcia3.altercadosi.entidades.Altercado;
import com.github.aggarcia3.altercadosi.entidades.Victima;
import com.github.aggarcia3.altercadosi.servicios.AltercadoService;
import com.github.aggarcia3.altercadosi.servicios.ResultadoBusquedaEntidadesPorCondicionBinaria;
import com.github.aggarcia3.altercadosi.servicios.VictimaService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * Controlador de Spring MVC para la gestión de víctimas.
 *
 * @author Alejandro González García
 */
@Controller
@RequestMapping("/victimas")
public final class VictimaController {
	private static final String RAIZ_CONTROLADOR = MethodHandles.lookup().lookupClass()
		.getDeclaredAnnotation(RequestMapping.class).value()[0];

	private static final String REDIRECCION_RAIZ_CONTROLADOR = "redirect:" + RAIZ_CONTROLADOR;

	private static final String MENSAJE_OTRA_CON_ESA_PK =
		"Ya hay otra víctima con ese nombre completo.";

	@Autowired
	private VictimaService serviciosVictimas;
	@Autowired
	private AltercadoService serviciosAltercados;

	@GetMapping({"", "mostrarTodas"})
	public String mostrarListaVictimas(final Model modeloVista) {
		modeloVista.addAttribute("victimas", serviciosVictimas.todasLasInstancias());
		return "victimas/mostrar_todas";
	}

	@GetMapping({"{nombreCompleto}", "{nombreCompleto}/ver"})
	public String mostrarVictima(@PathVariable final String nombreCompleto, final Model modeloVista) {
		return ejecutarMuestraVistaVictima(
			Optional.of(nombreCompleto), VistaVictimaUnica.VER_DETALLE, modeloVista
		);
	}

	@GetMapping("crear")
	public String mostrarCreacionVictima(final Model modeloVista) {
		if (serviciosAltercados.hayInstanciasRegistradas()) {
			return ejecutarMuestraVistaVictima(Optional.empty(), VistaVictimaUnica.CREAR, modeloVista);
		} else {
			modeloVista.addAttribute("tituloMensaje", "No hay altercados");
			modeloVista.addAttribute(
				"mensaje",
				"No se pueden crear víctimas si todavía no hay altercados en el sistema."
			);

			return mostrarListaVictimas(modeloVista);
		}
	}

	@PostMapping("crear")
	public String crearVictima(
		@Valid @ModelAttribute final Victima victima, final BindingResult resultado,
		final Model modeloVista
	) {
		return ejecutarModificacionVictima(VistaVictimaUnica.CREAR, victima, resultado, modeloVista);
	}

	@GetMapping("{nombreCompleto}/editar")
	public String mostrarEdicionVictima(
		@PathVariable final String nombreCompleto, final Model modeloVista
	) {
		return ejecutarMuestraVistaVictima(
			Optional.of(nombreCompleto), VistaVictimaUnica.EDITAR, modeloVista
		);
	}

	@PostMapping("{nombreCompleto}/editar")
	public String editarVictima(
		@Valid @ModelAttribute final Victima victima, final BindingResult resultado,
		final Model modeloVista
	) {
		return ejecutarModificacionVictima(VistaVictimaUnica.EDITAR, victima, resultado, modeloVista);
	}

	@GetMapping("{nombreCompleto}/eliminar")
	public String eliminarVictima(@PathVariable final String nombreCompleto, final Model modeloVista) {
		if (serviciosVictimas.eliminarPorClavePrimaria(nombreCompleto)) {
			modeloVista.addAttribute("tituloMensaje", "Víctima borrada");
			modeloVista.addAttribute(
				"mensaje",
				"Se ha borrado la víctima seleccionada y su información asociada."
			);
		} else {
			modeloVista.addAttribute("tituloMensaje", "Víctima ya borrado");
			modeloVista.addAttribute(
				"mensaje",
				"La víctima especificada ya ha sido borrada."
			);
		}

		return mostrarListaVictimas(modeloVista);
	}

	// --------------------------
	// MÉTODOS AYUDANTES PRIVADOS
	// --------------------------

	private String ejecutarMuestraVistaVictima(
		final Optional<String> nombreCompleto, final VistaVictimaUnica vista, final Model modeloVista
	) {
		final Optional<Victima> victima = nombreCompleto.isPresent() ?
			serviciosVictimas.obtenerPorClavePrimaria(nombreCompleto.get()) : Optional.empty();

		final boolean vistaCrear = vista == VistaVictimaUnica.CREAR;
		final boolean vistaCrearOEditar = vistaCrear || vista == VistaVictimaUnica.EDITAR;
		String vistaMostrar = vista.getNombreVista();

		if ((!vistaCrear && victima.isPresent()) || (vistaCrear && !victima.isPresent())) {
			modeloVista.addAttribute("creando", vistaCrear);
			modeloVista.addAttribute("victima", victima.orElseGet(Victima::new));

			if (vistaCrearOEditar) {
				final ResultadoBusquedaEntidadesPorCondicionBinaria<Altercado> resAltercados =
					serviciosAltercados.todosClasificadosPorVictimas();
				modeloVista.addAttribute("altercadosSinVictimas", resAltercados.getEntidadesSinCondicion());
				modeloVista.addAttribute("altercadosConVictimas", resAltercados.getEntidadesConCondicion());
			}
		} else {
			modeloVista.addAttribute("tituloMensaje", vista.getTituloMensajeError());
			modeloVista.addAttribute("mensaje", "La víctima especificada ya no existe.");
			vistaMostrar = mostrarListaVictimas(modeloVista);
		}

		return vistaMostrar;
	}

	private String ejecutarModificacionVictima(
		@NonNull final VistaVictimaUnica vista, @NonNull final Victima victima,
		@NonNull final BindingResult resultado, @NonNull final Model modeloVista
	) {
		String vistaMostrar = REDIRECCION_RAIZ_CONTROLADOR;

		if (resultado.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La víctima recibida no es válida");
		}

		if (!serviciosVictimas.guardar(victima)) {
			modeloVista.addAttribute("creando", false);
			modeloVista.addAttribute("tituloMensaje", vista.getTituloMensajeError());
			modeloVista.addAttribute("mensaje", MENSAJE_OTRA_CON_ESA_PK);
			vistaMostrar = VistaVictimaUnica.EDITAR.getNombreVista();
		}

		return vistaMostrar;
	}

	// --------------
	// TIPOS DE DATOS
	// --------------

	@AllArgsConstructor
	private static enum VistaVictimaUnica {
		CREAR("victimas/crear_editar", "Error en la creación"),
		EDITAR("victimas/crear_editar", "Error en la edición"),
		VER_DETALLE("victimas/ver_detalle", "Error en la visualización");

		@Getter @NonNull
		private final String nombreVista;
		@Getter @NonNull
		private final String tituloMensajeError;
	}
}
