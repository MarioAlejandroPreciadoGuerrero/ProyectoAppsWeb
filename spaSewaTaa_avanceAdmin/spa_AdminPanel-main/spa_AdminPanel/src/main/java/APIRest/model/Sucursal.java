package APIRest.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "sucursales")
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_sucursal;

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    @Column(nullable = false)
    private String direccion;

    @NotBlank(message = "La colonia es obligatoria")
    @Column(nullable = false)
    private String colonia;

    @Pattern(
            regexp = "^\\d{5}$",
            message = "El código postal debe tener 5 dígitos"
    )
    @Column(nullable = false)
    private String codigo_postal;

    @Pattern(
            regexp = "^\\d{10}$",
            message = "El telefono dene contener 10 dígitos"
    )
    @Column
    private String telefono;

    @NotNull(message = "La hora de apertura es obligatoria")
    @Column(nullable = false)
    private String hora_apertura;

    @NotNull(message = "La hora de cierre es obligaotia")
    @Column(nullable = false)
    private String hora_cierre;

    @OneToMany(
            mappedBy = "sucursal",
            cascade = CascadeType.ALL
    )
    private List<Terapeuta> terapeutas;


}
