package com.habity.habity_backend.repository;


import com.habity.habity_backend.entity.ReaccionPublicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReaccionPublicacionRepository extends JpaRepository<ReaccionPublicacion, Long> {
    Optional<ReaccionPublicacion> findByUsuarioIdAndPublicacionId(Long usuarioId, Long publicacionId);
    Long countByPublicacionIdAndEsLikeTrue(Long publicacionId);
    Long countByPublicacionIdAndEsLikeFalse(Long publicacionId);
}