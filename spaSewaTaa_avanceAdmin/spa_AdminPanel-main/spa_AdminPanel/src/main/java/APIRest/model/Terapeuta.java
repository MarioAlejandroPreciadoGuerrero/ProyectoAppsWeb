package APIRest.model;


import com.example.spa_AdminPanel.model.Sucursal;
import com.example.spa_AdminPanel.model.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "terapeutas")
public class Terapeuta {

    @Id
    @Column(name = "id_usuario")
    private Long id_terapeuta;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_sucursal")
    private Sucursal sucursal;

    @PastOrPresent(message = "La fecha de contratación no " +
            "puede ser futura")
    @Column
    private LocalDate fecha_contratacion;

    @ManyToMany
    @JoinTable(name = "terapeutas_servicios",
    joinColumns = @JoinColumn(name = "id_terapeuta"),
            inverseJoinColumns = @JoinColumn(name = "id_servicio")
    )
    private List<Servicio> servicios;

}
