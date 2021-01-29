package com.github.aggarcia3.altercadosi.entidades.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.aggarcia3.altercadosi.entidades.Victima;

@Repository
public interface VictimaRepository extends JpaRepository<Victima, String> {

}
