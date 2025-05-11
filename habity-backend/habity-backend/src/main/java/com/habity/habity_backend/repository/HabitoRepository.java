package com.habity.habity_backend.repository;

import com.habity.habity_backend.entity.Habito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitoRepository extends JpaRepository<Habito, Long> {
    List<Habito> findByUsuarioEmail(String email);
}
