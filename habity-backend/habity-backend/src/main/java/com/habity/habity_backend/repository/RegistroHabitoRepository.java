package com.habity.habity_backend.repository;

import com.habity.habity_backend.entity.RegistroHabito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository // Indica que esta interfaz es un componente de repositorio de Spring
public interface RegistroHabitoRepository extends JpaRepository<RegistroHabito, Long> {

    // Busca todos los registros de un hábito específico y los ordena por fecha ascendente
    List<RegistroHabito> findByHabitoIdOrderByFechaAsc(Long habitoId);

    // Verifica si ya existe un registro para esa fecha y hábito
    boolean existsByHabitoIdAndFecha(Long habitoId, LocalDate fecha);

    // Elimina el registro de ese día si se quiere desmarcar
    void deleteByHabitoIdAndFecha(Long habitoId, LocalDate fecha);
}

