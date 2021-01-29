package com.github.aggarcia3.altercadosi.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.aggarcia3.altercadosi.entidades.SucesoArmaRegistrada;

import lombok.NonNull;

@Service
public class SucesoArmaRegistradaService extends AbstractService<SucesoArmaRegistrada, Integer> {
	@Autowired
	public SucesoArmaRegistradaService(@NonNull final JpaRepository<SucesoArmaRegistrada, Integer> repositorioEntidad) {
		super(repositorioEntidad);
	}
}
