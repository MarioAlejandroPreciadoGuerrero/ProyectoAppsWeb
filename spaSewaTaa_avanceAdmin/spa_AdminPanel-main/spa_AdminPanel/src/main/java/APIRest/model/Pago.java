package APIRest.model;

import com.example.spa_AdminPanel.model.EstadoPago;
import com.example.spa_AdminPanel.model.MetodoPago;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pago;

    @NotNull(message = "Debe indicar el estado del pago")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", nullable = false)
    private EstadoPago estado_pago;

    @NotNull(message = "La fecha y hora del pago es obligatoria")
    @PastOrPresent(message = "La fecha del pago no puede ser futura")
    @Column(nullable = false)
    private LocalDateTime fecha_pago;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    @Column(nullable = false)
    private Double monto;

    @NotNull(message = "Debe seleccionar una cita")
    @OneToOne
    @JoinColumn(name = "id_cita", nullable = false, unique = true)
    private Cita cita;

    @NotNull(message = "Debe seleccionar un método de pago")
    @ManyToOne
    @JoinColumn(name = "id_metodo_pago", nullable = false)
    private MetodoPago metodoPago;
}
