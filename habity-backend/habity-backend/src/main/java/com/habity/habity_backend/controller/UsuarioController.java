package com.habity.habity_backend.controller;

import com.habity.habity_backend.entity.Usuario;
import com.habity.habity_backend.service.UsuarioPerfilService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioPerfilService usuarioPerfilService;

    public UsuarioController(UsuarioPerfilService usuarioPerfilService) {
        this.usuarioPerfilService = usuarioPerfilService;
    }

    @GetMapping("/perfilObtener")
    public ResponseEntity<Usuario> obtenerPerfil(HttpServletRequest request) {
        Usuario usuario = usuarioPerfilService.obtenerPerfil(request);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/perfilActualizar")
    public ResponseEntity<Usuario> actualizarPerfil(@RequestBody Usuario datos, HttpServletRequest request) {
        Usuario usuarioActualizado = usuarioPerfilService.actualizarPerfil(datos, request);
        return ResponseEntity.ok(usuarioActualizado);
    }


}