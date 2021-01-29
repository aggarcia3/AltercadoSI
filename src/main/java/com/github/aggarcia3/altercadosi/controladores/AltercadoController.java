package com.github.aggarcia3.altercadosi.controladores;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.function.Consumer;

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
import com.github.aggarcia3.altercadosi.entidades.Arma;
import com.github.aggarcia3.altercadosi.entidades.Lugar;
import com.github.aggarcia3.altercadosi.servicios.AltercadoService;
import com.github.aggarcia3.altercadosi.servicios.ArmaRegistradaService;
import com.github.aggarcia3.altercadosi.servicios.ArmaService;
import com.github.aggarcia3.altercadosi.servicios.LugarService;
import com.github.aggarcia3.altercadosi.servicios.ResultadoBusquedaEntidadesPorCondicionBinaria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * Controlador de Spring MVC para la gestión de altercados.
 *
 * @author Alejandro González García
 */
@Controller
@RequestMapping("/altercados")
public final class AltercadoController {
	private static final String RAIZ_CONTROLADOR = MethodHandles.lookup().lookupClass()
		.getDeclaredAnnotation(RequestMapping.class).value()[0];

	private static final String REDIRECCION_RAIZ_CONTROLADOR = "redirect:" + RAIZ_CONTROLADOR;

	private static final String MENSAJE_OTRO_CON_ESA_PK = "Ya hay otro altercado con ese nombre";

	@Autowired
	private AltercadoService serviciosAltercados;
	@Autowired
	private ArmaService serviciosArmas;
	@Autowired
	private ArmaRegistradaService serviciosArmasRegistradas;
	@Autowired
	private LugarService serviciosLugares;

	@GetMapping({"", "mostrarTodos"})
	public String mostrarListaAltercados(final Model modeloVista) {
		modeloVista.addAttribute("altercados", serviciosAltercados.todasLasInstancias());
		return "altercados/mostrar_todos";
	}

	@GetMapping({"{nombre}", "{nombre}/ver"})
	public String mostrarAltercado(@PathVariable final String nombre, final Model modeloVista) {
		return ejecutarMuestraVistaAltercado(
			Optional.of(nombre), VistaAltercadoUnica.VER_DETALLE, modeloVista
		);
	}

	@GetMapping("crear")
	public String mostrarCreacionAltercado(final Model modeloVista) {
		final boolean entidadesNecesariasPresentes =
			(serviciosArmas.hayInstanciasRegistradas() || serviciosArmasRegistradas.hayInstanciasRegistradas()) &&
			serviciosLugares.hayInstanciasRegistradas();

		if (entidadesNecesariasPresentes) {
			return ejecutarMuestraVistaAltercado(Optional.empty(), VistaAltercadoUnica.CREAR, modeloVista);
		} else {
			modeloVista.addAttribute("tituloMensaje", "No hay armas o lugares");
			modeloVista.addAttribute(
				"mensaje",
				"No se pueden crear altercados si todavía no hay armas y lugares en el sistema."
			);

			return mostrarListaAltercados(modeloVista);
		}
	}

	@GetMapping("crear/conLugar/{idLugar}")
	public String mostrarCreacionAltercadoConLugar(
		@PathVariable final int idLugar, final Model modeloVista
	) {
		return ejecutarMuestraVistaCreacionConValores(
			modeloVista, (Altercado altercadoVista) -> altercadoVista.getLugar().setId(idLugar)
		);
	}

	@GetMapping("crear/conArma/{codigoPruebaArma}")
	public String mostrarCreacionAltercadoConArma(
		@PathVariable final int codigoPruebaArma, final Model modeloVista
	) {
		return ejecutarMuestraVistaCreacionConValores(
			modeloVista, (Altercado altercadoVista) -> altercadoVista.getArma().setCodigoPrueba(codigoPruebaArma)
		);
	}

	@PostMapping("crear")
	public String crearAltercado(
		@Valid @ModelAttribute final Altercado altercado, final BindingResult resultado,
		final Model modeloVista
	) {
		return ejecutarModificacionAltercado(VistaAltercadoUnica.CREAR, altercado, resultado, modeloVista);
	}

	@GetMapping("{nombre}/editar")
	public String mostrarEdicionAltercado(
		@PathVariable final String nombre, final Model modeloVista
	) {
		return ejecutarMuestraVistaAltercado(
			Optional.of(nombre), VistaAltercadoUnica.EDITAR, modeloVista
		);
	}

	@PostMapping("{nombre}/editar")
	public String editarAltercado(
		@Valid @ModelAttribute final Altercado altercado, final BindingResult resultado,
		final Model modeloVista
	) {
		return ejecutarModificacionAltercado(VistaAltercadoUnica.EDITAR, altercado, resultado, modeloVista);
	}

	@GetMapping("{nombre}/eliminar")
	public String eliminarAltercado(@PathVariable final String nombre, final Model modeloVista) {
		if (serviciosAltercados.eliminarPorClavePrimaria(nombre)) {
			modeloVista.addAttribute("tituloMensaje", "Altercado borrado");
			modeloVista.addAttribute(
				"mensaje",
				"Se ha borrado el altercado seleccionado y su información asociada."
			);
		} else {
			modeloVista.addAttribute("tituloMensaje", "Altercado ya borrado");
			modeloVista.addAttribute(
				"mensaje",
				"El altercado especificado ya ha sido borrado."
			);
		}

		return mostrarListaAltercados(modeloVista);
	}

	// --------------------------
	// MÉTODOS AYUDANTES PRIVADOS
	// --------------------------

	private String ejecutarMuestraVistaCreacionConValores(
		final Model modeloVista, final Consumer<Altercado> consumidor
	) {
		String vistaMostrar = mostrarCreacionAltercado(modeloVista);
		final Object altercadoVistaObj = modeloVista.getAttribute("altercado");

		if (altercadoVistaObj instanceof Altercado) {
			consumidor.accept((Altercado) altercadoVistaObj);
		}

		return vistaMostrar;
	}

	private String ejecutarMuestraVistaAltercado(
		final Optional<String> nombre, final VistaAltercadoUnica vista, final Model modeloVista
	) {
		final Optional<Altercado> altercado = nombre.isPresent() ?
			serviciosAltercados.obtenerPorClavePrimaria(nombre.get()) : Optional.empty();

		final boolean vistaCrear = vista == VistaAltercadoUnica.CREAR;
		final boolean vistaCrearOEditar = vistaCrear || vista == VistaAltercadoUnica.EDITAR;
		String vistaMostrar = vista.getNombreVista();

		if ((!vistaCrear && altercado.isPresent()) || (vistaCrear && !altercado.isPresent())) {
			modeloVista.addAttribute("creando", vistaCrear);
			modeloVista.addAttribute("altercado", altercado.orElseGet(Altercado::new));

			if (vistaCrearOEditar) {
				final ResultadoBusquedaEntidadesPorCondicionBinaria<Lugar> resLugares =
					serviciosLugares.todosClasificadosPorAltercados();
				modeloVista.addAttribute("lugaresSinAltercados", resLugares.getEntidadesSinCondicion());
				modeloVista.addAttribute("lugaresConAltercados", resLugares.getEntidadesConCondicion());

				final ResultadoBusquedaEntidadesPorCondicionBinaria<Arma> resArmas =
					serviciosArmas.todasClasificadasPorAltercados();
				modeloVista.addAttribute("armasSinAltercados", resArmas.getEntidadesSinCondicion());
				modeloVista.addAttribute("armasConAltercados", resArmas.getEntidadesConCondicion());
			}
		} else {
			modeloVista.addAttribute("tituloMensaje", vista.getTituloMensajeError());
			modeloVista.addAttribute("mensaje", "El altercado especificado ya no existe.");
			vistaMostrar = mostrarListaAltercados(modeloVista);
		}

		return vistaMostrar;
	}

	private String ejecutarModificacionAltercado(
		@NonNull final VistaAltercadoUnica vista, @NonNull final Altercado altercado,
		@NonNull final BindingResult resultado, @NonNull final Model modeloVista
	) {
		String vistaMostrar = REDIRECCION_RAIZ_CONTROLADOR;

		if (resultado.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El altercado recibido no es válido");
		}

		if (!serviciosAltercados.guardar(altercado)) {
			modeloVista.addAttribute("creando", false);
			modeloVista.addAttribute("tituloMensaje", vista.getTituloMensajeError());
			modeloVista.addAttribute("mensaje", MENSAJE_OTRO_CON_ESA_PK);
			vistaMostrar = VistaAltercadoUnica.EDITAR.getNombreVista();
		}

		return vistaMostrar;
	}

	// --------------
	// TIPOS DE DATOS
	// --------------

	@AllArgsConstructor
	private static enum VistaAltercadoUnica {
		CREAR("altercados/crear_editar", "Error en la creación"),
		EDITAR("altercados/crear_editar", "Error en la edición"),
		VER_DETALLE("altercados/ver_detalle", "Error en la visualización");

		@Getter @NonNull
		private final String nombreVista;
		@Getter @NonNull
		private final String tituloMensajeError;
	}
}
