package APIRest.model;


import com.example.spa_AdminPanel.model.Cliente;
import com.example.spa_AdminPanel.model.EstadoCita;
import com.example.spa_AdminPanel.model.Sala;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "citas")
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cita;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    @NotNull(message = "Debe seleccionar un cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_servicio", nullable = false)
    @NotNull(message = "Debe seleccionar un servicio")
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "id_terapeuta", nullable = false)
    @NotNull(message = "Debe seleccionar un terapeuta")
    private Terapeuta terapeuta;

    @ManyToOne
    @JoinColumn(name = "id_sala", nullable = false)
    @NotNull(message = "Debe seleccionar una sala")
    private Sala sala;

    @NotNull(message = "La fecha y hora de inicio es obligatoria")
    @Column
    private LocalDateTime fecha_hora_inicio;

    @NotNull(message = "La fecha y hora de fin es obligatoria")
    @Column
    private LocalDateTime fecha_hora_fin;

    @NotNull(message = "Debe indicar el estado de la cita")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoCita estado;

    @Column
    private LocalDateTime fecha_creada;

    @Positive(message = "El precio debe ser mayor a cero")
    @Column
    private Double precio_total;


}
