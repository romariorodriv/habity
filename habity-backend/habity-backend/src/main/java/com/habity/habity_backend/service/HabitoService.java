package com.habity.habity_backend.service;

import com.habity.habity_backend.config.JwtUtil;
import com.habity.habity_backend.entity.Habito;
import com.habity.habity_backend.entity.RegistroHabito;
import com.habity.habity_backend.entity.Usuario;
import com.habity.habity_backend.repository.HabitoRepository;
import com.habity.habity_backend.repository.RegistroHabitoRepository;
import com.habity.habity_backend.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HabitoService {

    private final HabitoRepository habitoRepository;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final RegistroHabitoRepository registroHabitoRepository;

    public HabitoService(HabitoRepository habitoRepository, UsuarioRepository usuarioRepository, JwtUtil jwtUtil, RegistroHabitoRepository registroHabitoRepository) {
        this.habitoRepository = habitoRepository;
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
        this.registroHabitoRepository = registroHabitoRepository;
    }

    public List<Habito> obtenerHabitosDelUsuario(String email) {
        return habitoRepository.findByUsuarioEmail(email);
    }

    public Habito crearHabito(Habito habito, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();
        habito.setUsuario(usuario);
        return habitoRepository.save(habito);
    }

    public Habito editarHabito(Long id, Habito datos, String email) {
        Habito habito = habitoRepository.findById(id).orElseThrow();
        if (!habito.getUsuario().getEmail().equals(email)) {
            throw new RuntimeException("No autorizado");
        }
        habito.setNombre(datos.getNombre());
        habito.setDescripcion(datos.getDescripcion());
        habito.setCompletado(datos.getCompletado());
        habito.setFrecuencia(datos.getFrecuencia());
        return habitoRepository.save(habito);
    }

    public void eliminarHabito(Long id, String email) {
        Habito habito = habitoRepository.findById(id).orElseThrow();
        if (!habito.getUsuario().getEmail().equals(email)) {
            throw new RuntimeException("No autorizado");
        }
        habitoRepository.delete(habito);
    }

    public void toggleDiaCumplido(Long id, LocalDate fecha, String email) {
        Habito habito = habitoRepository.findById(id).orElseThrow();
        if (!habito.getUsuario().getEmail().equals(email)) {
            throw new RuntimeException("No autorizado");
        }
        boolean existe = registroHabitoRepository.existsByHabitoIdAndFecha(id, fecha);
        if (existe) {
            registroHabitoRepository.deleteByHabitoIdAndFecha(id, fecha);
        } else {
            RegistroHabito registro = new RegistroHabito();
            registro.setHabito(habito);
            registro.setFecha(fecha);
            registroHabitoRepository.save(registro);
        }
    }
}