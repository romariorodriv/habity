package com.habity.habity_backend.service;

import com.habity.habity_backend.config.JwtUtil;
import com.habity.habity_backend.entity.Publicacion;
import com.habity.habity_backend.entity.ReaccionPublicacion;
import com.habity.habity_backend.entity.Usuario;
import com.habity.habity_backend.repository.PublicacionRepository;
import com.habity.habity_backend.repository.ReaccionPublicacionRepository;
import com.habity.habity_backend.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final ReaccionPublicacionRepository reaccionRepository;

    public PublicacionService(PublicacionRepository publicacionRepository, UsuarioRepository usuarioRepository, JwtUtil jwtUtil, ReaccionPublicacionRepository reaccionRepository) {
        this.publicacionRepository = publicacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
        this.reaccionRepository = reaccionRepository;
    }

    public List<Publicacion> obtenerPublicaciones() {
        return publicacionRepository.findAllByOrderByFechaCreacionDesc();
    }

    public void crearPublicacion(Publicacion publicacion, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String email = jwtUtil.extractUsername(token);

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        publicacion.setUsuario(usuario);
        publicacion.setFechaCreacion(LocalDateTime.now());

        publicacionRepository.save(publicacion);
    }

    public void reaccionarPublicacion(Long id, boolean esLike, HttpServletRequest request) {
        String email = jwtUtil.extractUsername(request.getHeader("Authorization").replace("Bearer ", ""));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();
        Publicacion publicacion = publicacionRepository.findById(id).orElseThrow();

        Optional<ReaccionPublicacion> reaccionOpt = reaccionRepository.findByUsuarioIdAndPublicacionId(usuario.getId(), id);

        if (reaccionOpt.isPresent()) {
            ReaccionPublicacion reaccion = reaccionOpt.get();
            if (reaccion.isEsLike() == esLike) {
                reaccionRepository.delete(reaccion);
            } else {
                reaccion.setEsLike(esLike);
                reaccionRepository.save(reaccion);
            }
        } else {
            ReaccionPublicacion nueva = new ReaccionPublicacion();
            nueva.setUsuario(usuario);
            nueva.setPublicacion(publicacion);
            nueva.setEsLike(esLike);
            reaccionRepository.save(nueva);
        }
    }

    public Long contarLikes(Long id) {
        return reaccionRepository.countByPublicacionIdAndEsLikeTrue(id);
    }

    public Long contarDislikes(Long id) {
        return reaccionRepository.countByPublicacionIdAndEsLikeFalse(id);
    }
}