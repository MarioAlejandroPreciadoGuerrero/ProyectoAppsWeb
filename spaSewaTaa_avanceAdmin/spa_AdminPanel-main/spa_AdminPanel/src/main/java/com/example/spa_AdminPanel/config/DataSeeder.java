package com.example.spa_AdminPanel.config;

import com.example.spa_AdminPanel.model.Cita;
import com.example.spa_AdminPanel.model.Cliente;
import com.example.spa_AdminPanel.model.EstadoCita;
import com.example.spa_AdminPanel.model.NombreRol;
import com.example.spa_AdminPanel.model.Rol;
import com.example.spa_AdminPanel.model.Sala;
import com.example.spa_AdminPanel.model.Servicio;
import com.example.spa_AdminPanel.model.Sucursal;
import com.example.spa_AdminPanel.model.Terapeuta;
import com.example.spa_AdminPanel.model.Usuario;
import com.example.spa_AdminPanel.repository.CitaRepository;
import com.example.spa_AdminPanel.repository.ClienteRepository;
import com.example.spa_AdminPanel.repository.RolRepository;
import com.example.spa_AdminPanel.repository.SalaRepository;
import com.example.spa_AdminPanel.repository.ServicioRepository;
import com.example.spa_AdminPanel.repository.SucursalRepository;
import com.example.spa_AdminPanel.repository.TerapeutaRepository;
import com.example.spa_AdminPanel.repository.UsuarioRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Profile("!prod")
public class DataSeeder implements ApplicationRunner {

    private static final String ADMIN_EMAIL = "admin@spasewataa.com";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String DEMO_PASSWORD = "prueba123";

    private static final List<ServicioDemo> SERVICIOS_DEMO = List.of(
            new ServicioDemo("Masaje relajante", 60, "Masaje corporal para liberar tensión", "Masajes", 850.0),
            new ServicioDemo("Masaje con piedras calientes", 90, "Masaje relajante con piedras calientes", "Masajes", 1200.0),
            new ServicioDemo("Facial hidratante", 60, "Limpieza e hidratación facial", "Faciales", 700.0),
            new ServicioDemo("Aromaterapia", 45, "Tratamiento de relajación con aceites esenciales", "Bienestar", 600.0)
    );

    private static final List<ClienteDemo> CLIENTES_DEMO = List.of(
            new ClienteDemo("Mariana López", "mariana.lopez@demo.com", LocalDate.of(1992, 4, 12)),
            new ClienteDemo("Carlos Ruiz", "carlos.ruiz@demo.com", LocalDate.of(1988, 8, 23)),
            new ClienteDemo("Valeria Gómez", "valeria.gomez@demo.com", LocalDate.of(1996, 2, 5)),
            new ClienteDemo("Diego Hernández", "diego.hernandez@demo.com", LocalDate.of(1990, 11, 18))
    );

    private static final List<TerapeutaDemo> TERAPEUTAS_DEMO = List.of(
            new TerapeutaDemo("Ana Torres", "ana.torres@demo.com",
                    List.of("Masaje relajante", "Masaje con piedras calientes")),
            new TerapeutaDemo("Luis Mendoza", "luis.mendoza@demo.com",
                    List.of("Facial hidratante", "Aromaterapia")),
            new TerapeutaDemo("Sofía Ramírez", "sofia.ramirez@demo.com",
                    List.of("Masaje relajante", "Facial hidratante"))
    );

    private static final List<CitaDemo> CITAS_DEMO = List.of(
            new CitaDemo("mariana.lopez@demo.com", "ana.torres@demo.com", "Masaje relajante", 0, 10, 0, 1),
            new CitaDemo("carlos.ruiz@demo.com", "luis.mendoza@demo.com", "Facial hidratante", 1, 11, 0, 2),
            new CitaDemo("valeria.gomez@demo.com", "sofia.ramirez@demo.com", "Masaje relajante", 2, 12, 0, 3),
            new CitaDemo("diego.hernandez@demo.com", "ana.torres@demo.com",
                    "Masaje con piedras calientes", 3, 9, 0, 1)
    );

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClienteRepository clienteRepository;
    private final ServicioRepository servicioRepository;
    private final SucursalRepository sucursalRepository;
    private final SalaRepository salaRepository;
    private final TerapeutaRepository terapeutaRepository;
    private final CitaRepository citaRepository;

    public DataSeeder(RolRepository rolRepository, UsuarioRepository usuarioRepository,
                      PasswordEncoder passwordEncoder, ClienteRepository clienteRepository,
                      ServicioRepository servicioRepository, SucursalRepository sucursalRepository,
                      SalaRepository salaRepository, TerapeutaRepository terapeutaRepository,
                      CitaRepository citaRepository) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.clienteRepository = clienteRepository;
        this.servicioRepository = servicioRepository;
        this.sucursalRepository = sucursalRepository;
        this.salaRepository = salaRepository;
        this.terapeutaRepository = terapeutaRepository;
        this.citaRepository = citaRepository;
    }

    @Override
    @Transactional
    public void run(@NonNull ApplicationArguments args) {
        for (NombreRol nombre : NombreRol.values()) {
            if (rolRepository.findByNombre(nombre).isEmpty()) {
                rolRepository.save(Rol.builder().nombre(nombre).build());
            }
        }

        if (usuarioRepository.findUsuarioByEmail(ADMIN_EMAIL).isEmpty()) {
            usuarioRepository.save(Usuario.builder()
                    .nombre("Administrador de pruebas")
                    .email(ADMIN_EMAIL)
                    .contrasenia(passwordEncoder.encode(ADMIN_PASSWORD))
                    .rol(obtenerRol(NombreRol.ADMIN))
                    .build());
        }

        Rol rolCliente = obtenerRol(NombreRol.CLIENTE);
        Rol rolTerapeuta = obtenerRol(NombreRol.TERAPEUTA);
        Map<String, Servicio> servicios = SERVICIOS_DEMO.stream()
                .map(this::obtenerOCrearServicio)
                .collect(Collectors.toMap(servicio -> normalizar(servicio.getNombre()), servicio -> servicio));

        Sucursal sucursal = obtenerOCrearSucursal();
        List<Sala> salas = List.of(
                obtenerOCrearSala(sucursal, 1),
                obtenerOCrearSala(sucursal, 2),
                obtenerOCrearSala(sucursal, 3)
        );

        Map<String, Cliente> clientes = CLIENTES_DEMO.stream()
                .map(demo -> obtenerOCrearCliente(demo, rolCliente))
                .collect(Collectors.toMap(cliente -> cliente.getUsuario().getEmail(), cliente -> cliente));
        Map<String, Terapeuta> terapeutas = TERAPEUTAS_DEMO.stream()
                .map(demo -> obtenerOCrearTerapeuta(demo, rolTerapeuta, sucursal, servicios))
                .collect(Collectors.toMap(terapeuta -> terapeuta.getUsuario().getEmail(), terapeuta -> terapeuta));

        Set<Long> clientesConCitas = citaRepository.findAll().stream()
                .map(Cita::getCliente)
                .map(Cliente::getId_cliente)
                .collect(Collectors.toSet());
        LocalDate proximoLunes = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        for (CitaDemo demo : CITAS_DEMO) {
            Cliente cliente = clientes.get(demo.clienteEmail());
            if (clientesConCitas.contains(cliente.getId_cliente())) {
                continue;
            }

            Servicio servicio = servicios.get(normalizar(demo.servicioNombre()));
            LocalDateTime inicio = proximoLunes.plusDays(demo.diaOffset())
                    .atTime(demo.hora(), demo.minuto());
            Cita cita = new Cita();
            cita.setCliente(cliente);
            cita.setServicio(servicio);
            cita.setTerapeuta(terapeutas.get(demo.terapeutaEmail()));
            cita.setSala(salas.get(demo.numeroSala() - 1));
            cita.setFecha_hora_inicio(inicio);
            cita.setFecha_hora_fin(inicio.plusMinutes(servicio.getMinutos_duracion()));
            cita.setEstado(EstadoCita.CONFIRMADA);
            cita.setFecha_creada(LocalDateTime.now());
            cita.setPrecio_total(servicio.getPrecio());
            citaRepository.save(cita);
            clientesConCitas.add(cliente.getId_cliente());
        }
    }

    private Rol obtenerRol(NombreRol nombre) {
        return rolRepository.findByNombre(nombre)
                .orElseThrow(() -> new IllegalStateException("No se pudo crear el rol " + nombre));
    }

    private Servicio obtenerOCrearServicio(ServicioDemo demo) {
        return servicioRepository.findAll().stream()
                .filter(servicio -> servicio.getNombre().equalsIgnoreCase(demo.nombre()))
                .findFirst()
                .orElseGet(() -> servicioRepository.save(Servicio.builder()
                        .nombre(demo.nombre())
                        .minutos_duracion(demo.duracion())
                        .descripcion(demo.descripcion())
                        .categoria(demo.categoria())
                        .precio(demo.precio())
                        .build()));
    }

    private Sucursal obtenerOCrearSucursal() {
        return sucursalRepository.findAll().stream()
                .filter(sucursal -> "Sucursal de pruebas".equalsIgnoreCase(sucursal.getNombre()))
                .findFirst()
                .orElseGet(() -> sucursalRepository.save(Sucursal.builder()
                        .nombre("Sucursal de pruebas")
                        .direccion("Av. Siempre Viva 123")
                        .colonia("Centro")
                        .codigo_postal("06000")
                        .telefono("5550000000")
                        .hora_apertura(LocalTime.of(9, 0))
                        .hora_cierre(LocalTime.of(20, 0))
                        .build()));
    }

    private Sala obtenerOCrearSala(Sucursal sucursal, int numero) {
        String numeroSala = "DEMO-" + numero;
        return salaRepository.findAll().stream()
                .filter(sala -> numeroSala.equals(sala.getNumero_sala())
                        && sala.getSucursal().getId_sucursal().equals(sucursal.getId_sucursal()))
                .findFirst()
                .orElseGet(() -> salaRepository.save(Sala.builder()
                        .sucursal(sucursal)
                        .numero_sala(numeroSala)
                        .activa(true)
                        .build()));
    }

    private Cliente obtenerOCrearCliente(ClienteDemo demo, Rol rolCliente) {
        Usuario usuario = obtenerOCrearUsuario(demo.nombre(), demo.email(), rolCliente);
        return clienteRepository.findById(usuario.getId_usuario())
                .orElseGet(() -> clienteRepository.save(Cliente.builder()
                        .usuario(usuario)
                        .fecha_registro(LocalDateTime.now())
                        .fecha_nacimiento(demo.fechaNacimiento())
                        .activo(true)
                        .build()));
    }

    private Terapeuta obtenerOCrearTerapeuta(TerapeutaDemo demo, Rol rolTerapeuta, Sucursal sucursal,
                                              Map<String, Servicio> servicios) {
        Usuario usuario = obtenerOCrearUsuario(demo.nombre(), demo.email(), rolTerapeuta);
        return terapeutaRepository.findById(usuario.getId_usuario())
                .orElseGet(() -> terapeutaRepository.save(Terapeuta.builder()
                        .usuario(usuario)
                        .sucursal(sucursal)
                        .fecha_contratacion(LocalDate.now().minusYears(2))
                        .servicios(demo.servicios().stream()
                                .map(nombre -> servicios.get(normalizar(nombre)))
                                .toList())
                        .build()));
    }

    private Usuario obtenerOCrearUsuario(String nombre, String email, Rol rol) {
        Usuario usuario = usuarioRepository.findUsuarioByEmail(email)
                .orElseGet(() -> usuarioRepository.save(Usuario.builder()
                        .nombre(nombre)
                        .email(email)
                        .contrasenia(passwordEncoder.encode(DEMO_PASSWORD))
                        .rol(rol)
                        .build()));
        if (usuario.getRol() == null || usuario.getRol().getNombre() != rol.getNombre()) {
            throw new IllegalStateException("El correo de prueba ya está registrado con otro rol: " + email);
        }
        return usuario;
    }

    private String normalizar(String value) {
        return value.toLowerCase(Locale.ROOT);
    }

    private record ServicioDemo(String nombre, int duracion, String descripcion, String categoria, double precio) {}

    private record ClienteDemo(String nombre, String email, LocalDate fechaNacimiento) {}

    private record TerapeutaDemo(String nombre, String email, List<String> servicios) {}

    private record CitaDemo(String clienteEmail, String terapeutaEmail, String servicioNombre,
                            int diaOffset, int hora, int minuto, int numeroSala) {}
}
