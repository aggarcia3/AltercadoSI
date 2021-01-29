package com.github.aggarcia3.altercadosi.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.aggarcia3.altercadosi.entidades.Victima;

import lombok.NonNull;

@Service
public class VictimaService extends AbstractService<Victima, String> {
	@Autowired
	public VictimaService(@NonNull final JpaRepository<Victima, String> repositorioEntidad) {
		super(repositorioEntidad);
	}
}
