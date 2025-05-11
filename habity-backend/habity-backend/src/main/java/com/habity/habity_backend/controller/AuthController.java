package com.habity.habity_backend.controller;

import com.habity.habity_backend.config.JwtUtil;
import com.habity.habity_backend.dto.LoginRequest;

import com.habity.habity_backend.entity.Usuario;
import com.habity.habity_backend.repository.UsuarioRepository;
import com.habity.habity_backend.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*") // permite todo origen, solo en desarrollo
@RestController
@RequestMapping("/api/auth")

public class AuthController {
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioService usuarioService, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String nombre = body.get("nombre");
        String email = body.get("email");
        String password = passwordEncoder.encode(body.get("password"));
        String imagenUrl = body.get("imagenUrl"); // puede ser una URL base64 o ruta
        int edad = Integer.parseInt(body.get("edad"));
        double peso = Double.parseDouble(body.get("peso"));
        double altura = Double.parseDouble(body.get("altura"));

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setImagenUrl(imagenUrl);
        usuario.setEdad(edad);
        usuario.setPeso(peso);
        usuario.setAltura(altura);

        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Usuario> userOpt = usuarioRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }

        Usuario user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("email", user.getEmail());
        response.put("nombre", user.getNombre());

        return ResponseEntity.ok(response);
    }




}
