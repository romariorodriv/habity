package com.habity.habity_backend.controller;

import com.habity.habity_backend.config.JwtUtil;
import com.habity.habity_backend.entity.Habito;
import com.habity.habity_backend.entity.RegistroHabito;
import com.habity.habity_backend.entity.Usuario;
import com.habity.habity_backend.repository.HabitoRepository;
import com.habity.habity_backend.repository.RegistroHabitoRepository;
import com.habity.habity_backend.repository.UsuarioRepository;
import com.habity.habity_backend.service.HabitoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/habitos")
@CrossOrigin(origins = "*")
public class HabitoController {

    private final HabitoService habitoService;
    private final JwtUtil jwtUtil;

    public HabitoController(HabitoService habitoService, JwtUtil jwtUtil) {
        this.habitoService = habitoService;
        this.jwtUtil = jwtUtil;
    }

    private String getEmailFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return jwtUtil.extractUsername(token);
    }

    @GetMapping
    public List<Habito> obtenerHabitosDelUsuarioAutenticado(Authentication authentication) {
        return habitoService.obtenerHabitosDelUsuario(authentication.getName());
    }

    @PostMapping
    public Habito crearHabito(@RequestBody Habito habito, HttpServletRequest request) {
        String email = getEmailFromToken(request);
        return habitoService.crearHabito(habito, email);
    }

    @PutMapping("/{id}")
    public Habito editarHabito(@PathVariable Long id, @RequestBody Habito datos, HttpServletRequest request) {
        String email = getEmailFromToken(request);
        return habitoService.editarHabito(id, datos, email);
    }

    @DeleteMapping("/{id}")
    public void eliminarHabito(@PathVariable Long id, HttpServletRequest request) {
        String email = getEmailFromToken(request);
        habitoService.eliminarHabito(id, email);
    }

    @PutMapping("/{id}/registro/{fecha}")
    public ResponseEntity<?> toggleDiaCumplido(@PathVariable Long id, @PathVariable String fecha, HttpServletRequest request) {
        String email = getEmailFromToken(request);
        habitoService.toggleDiaCumplido(id, LocalDate.parse(fecha), email);
        return ResponseEntity.ok().build();
    }
}