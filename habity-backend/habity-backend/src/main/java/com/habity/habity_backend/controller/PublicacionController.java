package com.habity.habity_backend.controller;

import com.habity.habity_backend.config.JwtUtil;
import com.habity.habity_backend.entity.Publicacion;
import com.habity.habity_backend.entity.ReaccionPublicacion;
import com.habity.habity_backend.entity.Usuario;
import com.habity.habity_backend.repository.PublicacionRepository;
import com.habity.habity_backend.repository.ReaccionPublicacionRepository;
import com.habity.habity_backend.repository.UsuarioRepository;
import com.habity.habity_backend.service.PublicacionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/publicaciones")
@CrossOrigin(origins = "*")
public class PublicacionController {

    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @GetMapping
    public List<Publicacion> obtenerPublicaciones() {
        return publicacionService.obtenerPublicaciones();
    }

    @PostMapping
    public ResponseEntity<?> crearPublicacion(@RequestBody Publicacion publicacion, HttpServletRequest request) {
        publicacionService.crearPublicacion(publicacion, request);
        return ResponseEntity.ok("Publicación creada");
    }

    @PutMapping("/{id}/reaccion")
    public ResponseEntity<?> reaccionarPublicacion(
            @PathVariable Long id,
            @RequestParam("like") boolean esLike,
            HttpServletRequest request) {
        publicacionService.reaccionarPublicacion(id, esLike, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/likes")
    public Long contarLikes(@PathVariable Long id) {
        return publicacionService.contarLikes(id);
    }

    @GetMapping("/{id}/dislikes")
    public Long contarDislikes(@PathVariable Long id) {
        return publicacionService.contarDislikes(id);
    }
}