package com.example.Bemole_API.service;

import com.example.Bemole_API.models.Usuario;
import com.example.Bemole_API.repositorys.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UsuarioService {

    // Mínimo 8 caracteres, al menos una mayúscula, una minúscula, un número y un símbolo
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$");

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<Usuario> listarUsuarios() {
        return repository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser un número positivo.");
        }
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado."));
    }

    public Usuario registrarUsuario(Usuario usuario) {
        validarUsuarioCompleto(usuario);

        String emailNormalizado = usuario.getEmail().trim().toLowerCase();
        if (repository.existsByEmail((emailNormalizado))) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + emailNormalizado);
        }

        usuario.setNombre(usuario.getNombre().trim());
        usuario.setEmail(emailNormalizado);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setFechaRegistro(LocalDateTime.now());

        return repository.save(usuario);
    }

    public Usuario actualizarUsuarioParcial(Long id, Usuario usuarioParcial) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser un número positivo.");
        }
        if (usuarioParcial == null) {
            throw new IllegalArgumentException("Los datos del usuario no pueden ser nulos.");
        }

        return repository.findById(id).map(usuario -> {

            if (usuarioParcial.getNombre() != null) {
                String nuevoNombre = usuarioParcial.getNombre().trim();
                if (nuevoNombre.isEmpty()) {
                    throw new IllegalArgumentException("El nombre del usuario no puede estar vacío.");
                }
                if (nuevoNombre.length() > 100) {
                    throw new IllegalArgumentException("El nombre no puede superar los 100 caracteres.");
                }
                usuario.setNombre(nuevoNombre);
            }

            if (usuarioParcial.getEmail() != null) {
                String nuevoEmail = usuarioParcial.getEmail().trim().toLowerCase();
                if (!EMAIL_PATTERN.matcher(nuevoEmail).matches()) {
                    throw new IllegalArgumentException("El formato del email no es válido.");
                }
                if (!nuevoEmail.equals(usuario.getEmail())
                        && repository.existsByEmail(nuevoEmail)) {
                    throw new IllegalArgumentException("Ya existe un usuario con el email: " + nuevoEmail);
                }
                usuario.setEmail(nuevoEmail);
            }

            if (usuarioParcial.getRol() != null) {
                usuario.setRol(usuarioParcial.getRol());
            }

            return repository.save(usuario);

        }).orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado."));
    }

    public Usuario actualizarPassword(Long id, Usuario usuarioParcial) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser un número positivo.");
        }
        if (usuarioParcial == null || usuarioParcial.getPassword() == null
                || usuarioParcial.getPassword().isBlank()) {
            throw new IllegalArgumentException("La nueva contraseña es obligatoria.");
        }

        String nuevaPassword = usuarioParcial.getPassword();
        if (!PASSWORD_PATTERN.matcher(nuevaPassword).matches()) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 8 caracteres e incluir " +
                            "una mayúscula, una minúscula, un número y un símbolo especial.");
        }

        return repository.findById(id).map(usuario -> {
            // Evitar reusar la misma contraseña
            if (passwordEncoder.matches(nuevaPassword, usuario.getPassword())) {
                throw new IllegalArgumentException("La nueva contraseña no puede ser igual a la contraseña actual.");
            }
            usuario.setPassword(passwordEncoder.encode(nuevaPassword));
            return repository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado."));
    }

    public void eliminarUsuario(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser un número positivo.");
        }
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuario con ID " + id + " no encontrado.");
        }
        repository.deleteById(id);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private void validarUsuarioCompleto(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario es obligatorio.");
        }
        if (usuario.getNombre().trim().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede superar los 100 caracteres.");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email del usuario es obligatorio.");
        }
        if (!EMAIL_PATTERN.matcher(usuario.getEmail().trim()).matches()) {
            throw new IllegalArgumentException("El formato del email no es válido.");
        }
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña del usuario es obligatoria.");
        }
        if (!PASSWORD_PATTERN.matcher(usuario.getPassword()).matches()) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 8 caracteres e incluir " +
                            "una mayúscula, una minúscula, un número y un símbolo especial.");
        }
        if (usuario.getRol() == null) {
            throw new IllegalArgumentException("El rol del usuario es obligatorio.");
        }
    }
}
