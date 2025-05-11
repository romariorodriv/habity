package com.habity.habity_backend.repository;

import com.habity.habity_backend.entity.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    // Devuelve las publicaciones más recientes primero
    List<Publicacion> findAllByOrderByFechaCreacionDesc();
}
