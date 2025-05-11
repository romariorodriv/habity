package com.habity.habity_backend.service;

import com.habity.habity_backend.config.JwtUtil;
import com.habity.habity_backend.entity.Usuario;
import com.habity.habity_backend.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class UsuarioPerfilService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    public UsuarioPerfilService(UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }

    public Usuario obtenerPerfil(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String email = jwtUtil.extractUsername(token);
        return usuarioRepository.findByEmail(email).orElseThrow();
    }

    public Usuario actualizarPerfil(Usuario datos, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String email = jwtUtil.extractUsername(token);

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();

        usuario.setNombre(datos.getNombre());
        usuario.setEdad(datos.getEdad());
        usuario.setPeso(datos.getPeso());
        usuario.setAltura(datos.getAltura());
        usuario.setImagenUrl(datos.getImagenUrl());

        return usuarioRepository.save(usuario);
    }
}