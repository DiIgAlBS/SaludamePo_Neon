package com.duoc.saludamepo_neon.controller;

import com.duoc.saludamepo_neon.model.Comuna;
import com.duoc.saludamepo_neon.model.Rol;
import com.duoc.saludamepo_neon.model.Usuario;
import com.duoc.saludamepo_neon.repository.ComunaRepository;
import com.duoc.saludamepo_neon.repository.RolRepository;
import com.duoc.saludamepo_neon.repository.UsuarioRepository;
import com.duoc.saludamepo_neon.service.CustomUserDetailsService;
import com.duoc.saludamepo_neon.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "2. Autenticación (JWT)", description = "Registro de clientes e inicio de sesión con generación de token")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final ComunaRepository comunaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión y obtener Token JWT")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getEmail());
        String token = jwtService.generateToken(
                userDetails,
                usuario.getRol().getNombre(),
                usuario.getId(),
                usuario.getNombre()
        );

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("token", token);
        respuesta.put("id", usuario.getId());
        respuesta.put("nombre", usuario.getNombre());
        respuesta.put("apellido", usuario.getApellido());
        respuesta.put("email", usuario.getEmail());
        respuesta.put("rol", usuario.getRol().getNombre());

        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo usuario cliente")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El correo ya se encuentra registrado");
        }

        Rol rolCliente = rolRepository.findByNombre("ROLE_CLIENTE")
                .orElseThrow(() -> new RuntimeException("Rol ROLE_CLIENTE no encontrado en la base de datos"));

        Usuario nuevo = new Usuario();
        nuevo.setNombre(request.getNombre());
        nuevo.setApellido(request.getApellido());
        nuevo.setEmail(request.getEmail());
        nuevo.setPassword(passwordEncoder.encode(request.getPassword()));
        nuevo.setFechaNacimiento(request.getFechaNacimiento() != null ? request.getFechaNacimiento() : LocalDate.of(2000, 1, 1));
        nuevo.setTelefono(request.getTelefono());
        nuevo.setDireccion(request.getDireccion());
        nuevo.setRol(rolCliente);

        if (request.getComunaId() != null) {
            Comuna comuna = comunaRepository.findById(request.getComunaId()).orElse(null);
            nuevo.setComuna(comuna);
        }

        Usuario guardado = usuarioRepository.save(nuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensaje", "Usuario registrado exitosamente",
                "id", guardado.getId(),
                "email", guardado.getEmail()
        ));
    }

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    public static class RegisterRequest {
        private String nombre;
        private String apellido;
        private String email;
        private String password;
        private LocalDate fechaNacimiento;
        private String telefono;
        private String direccion;
        private Long comunaId;
    }
}