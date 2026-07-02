package com.example.Bemole_API.service;

import com.example.Bemole_API.dto.usuarios.UsuarioRegistroDTO;
import com.example.Bemole_API.dto.usuarios.UsuarioResponseDTO;
import com.example.Bemole_API.enums.RolUsuario;
import com.example.Bemole_API.models.Usuario;
import com.example.Bemole_API.repositorys.UsuarioRepository;
import com.example.Bemole_API.service.mappers.UsuarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.Bemole_API.exception.RecursoNoEncontradoException;
import com.example.Bemole_API.exception.NegocioException;

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

    @Autowired
    private UsuarioMapper usuarioMapper;

    public List<Usuario> listarUsuarios() {
        return repository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        if (id == null || id <= 0) {
            throw new NegocioException("El ID del usuario debe ser un número positivo.");
        }
        return repository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Usuario con ID " + id + " no encontrado."
                        )
                );
    }

    public UsuarioResponseDTO registrarUsuario(UsuarioRegistroDTO usuarioDTO) {

        String nombreNormalizado = usuarioDTO.getNombre().trim();
        String apellidoNormalizado = usuarioDTO.getApellido().trim();
        String emailNormalizado = usuarioDTO.getEmail().trim().toLowerCase();

        if (!usuarioDTO.getPassword().equals(usuarioDTO.getConfirmarPassword())) {
            throw new NegocioException(
                    "Las contraseñas no coinciden."
            );
        }

        if (repository.existsByEmail(emailNormalizado)) {
            throw new NegocioException(
                    "Ya existe un usuario con el correo: "
                            + emailNormalizado
            );
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(nombreNormalizado);
        usuario.setApellido(apellidoNormalizado);
        usuario.setEmail(emailNormalizado);

        if (usuarioDTO.getTelefono() != null
                && !usuarioDTO.getTelefono().isBlank()) {
            usuario.setTelefono(usuarioDTO.getTelefono().trim());
        } else {
            usuario.setTelefono(null);
        }

        usuario.setPassword(
                passwordEncoder.encode(usuarioDTO.getPassword())
        );

        usuario.setRol(RolUsuario.CLIENTE);
        usuario.setFechaRegistro(LocalDateTime.now());

        Usuario usuarioGuardado = repository.save(usuario);

        return usuarioMapper.toResponseDTO(usuarioGuardado);
    }

    public Usuario actualizarUsuarioParcial(Long id, Usuario usuarioParcial) {
        if (id == null || id <= 0) {
            throw new NegocioException("El ID del usuario debe ser un número positivo.");
        }
        if (usuarioParcial == null) {
            throw new NegocioException("Los datos del usuario no pueden ser nulos.");
        }

        return repository.findById(id).map(usuario -> {

            if (usuarioParcial.getNombre() != null) {
                String nuevoNombre = usuarioParcial.getNombre().trim();
                if (nuevoNombre.isEmpty()) {
                    throw new NegocioException("El nombre del usuario no puede estar vacío.");
                }
                if (nuevoNombre.length() > 100) {
                    throw new NegocioException("El nombre no puede superar los 100 caracteres.");
                }
                usuario.setNombre(nuevoNombre);
            }

            if (usuarioParcial.getApellido() != null) {
                String nuevoApellido = usuarioParcial.getApellido().trim();
                if (nuevoApellido.isEmpty()) {
                    throw new NegocioException("El nombre del usuario no puede estar vacío.");
                }
                if (nuevoApellido.length() > 100) {
                    throw new NegocioException("El nombre no puede superar los 100 caracteres.");
                }
                usuario.setApellido(nuevoApellido);
            }

            if (usuarioParcial.getEmail() != null) {
                String nuevoEmail = usuarioParcial.getEmail().trim().toLowerCase();
                if (!EMAIL_PATTERN.matcher(nuevoEmail).matches()) {
                    throw new NegocioException("El formato del email no es válido.");
                }
                if (!nuevoEmail.equals(usuario.getEmail())
                        && repository.existsByEmail(nuevoEmail)) {
                    throw new NegocioException("Ya existe un usuario con el email: " + nuevoEmail);
                }
                usuario.setEmail(nuevoEmail);
            }

            if (usuarioParcial.getRol() != null) {
                usuario.setRol(usuarioParcial.getRol());
            }

            if (usuarioParcial.getTelefono() !=null){
                usuario.setTelefono(usuarioParcial.getTelefono());
            }

            return repository.save(usuario);

        }).orElseThrow(() -> new RecursoNoEncontradoException("Usuario con ID " + id + " no encontrado."));
    }

    public Usuario actualizarPassword(Long id, Usuario usuarioParcial) {
        if (id == null || id <= 0) {
            throw new NegocioException("El ID del usuario debe ser un número positivo.");
        }
        if (usuarioParcial == null || usuarioParcial.getPassword() == null
                || usuarioParcial.getPassword().isBlank()) {
            throw new NegocioException("La nueva contraseña es obligatoria.");
        }

        String nuevaPassword = usuarioParcial.getPassword();
        if (!PASSWORD_PATTERN.matcher(nuevaPassword).matches()) {
            throw new NegocioException(
                    "La contraseña debe tener al menos 8 caracteres e incluir " +
                            "una mayúscula, una minúscula, un número y un símbolo especial.");
        }

        return repository.findById(id).map(usuario -> {
            // Evitar reusar la misma contraseña
            if (passwordEncoder.matches(nuevaPassword, usuario.getPassword())) {
                throw new NegocioException("La nueva contraseña no puede ser igual a la contraseña actual.");
            }
            usuario.setPassword(passwordEncoder.encode(nuevaPassword));
            return repository.save(usuario);
        }).orElseThrow(() -> new RecursoNoEncontradoException("Usuario con ID " + id + " no encontrado."));
    }

    public void eliminarUsuario(Long id) {
        if (id == null || id <= 0) {
            throw new NegocioException("El ID del usuario debe ser un número positivo.");
        }
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Usuario con ID " + id + " no encontrado.");
        }
        repository.deleteById(id);
    }

    //Metodos Auxiliares

    private void validarUsuarioCompleto(UsuarioRegistroDTO usuario) {
        if (usuario == null) {
            throw new NegocioException("El usuario no puede ser nulo.");
        }
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new NegocioException("El nombre del usuario es obligatorio.");
        }
        if (usuario.getNombre().trim().length() > 100) {
            throw new NegocioException("El nombre no puede superar los 100 caracteres.");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new NegocioException("El email del usuario es obligatorio.");
        }
        if (!EMAIL_PATTERN.matcher(usuario.getEmail().trim()).matches()) {
            throw new NegocioException("El formato del email no es válido.");
        }
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new NegocioException("La contraseña del usuario es obligatoria.");
        }
        if (!PASSWORD_PATTERN.matcher(usuario.getPassword()).matches()) {
            throw new NegocioException(
                    "La contraseña debe tener al menos 8 caracteres e incluir " +
                            "una mayúscula, una minúscula, un número y un símbolo especial.");
        }
    }
}
