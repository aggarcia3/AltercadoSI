package com.github.aggarcia3.altercadosi.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.aggarcia3.altercadosi.entidades.ArmaRegistrada;

import lombok.NonNull;

@Service
public class ArmaRegistradaService extends AbstractService<ArmaRegistrada, Integer> {
	@Autowired
	public ArmaRegistradaService(@NonNull final JpaRepository<ArmaRegistrada, Integer> repositorioEntidad) {
		super(repositorioEntidad);
	}
}
