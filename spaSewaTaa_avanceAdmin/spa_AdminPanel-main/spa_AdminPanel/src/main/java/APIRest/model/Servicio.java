package APIRest.model;

import com.example.spa_AdminPanel.model.Terapeuta;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@Table(name = "servicios")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotNull(message = "La duración es obligatoria")
    @Min(value = 1, message = "La duración debe ser mayor a 0")
    @Column(nullable = false)
    private Integer duracion;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    @Column(nullable = false)
    private String descripcion;

    @NotNull(message = "La categoría es obligatoria")
    @Column(nullable = false)
    private String categoria;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    @Column(nullable = false)
    private Double precio;

    @Column
    private String url_imagen;

    @ManyToMany(mappedBy = "servicios")
    private List<Terapeuta> terapeutas;


}
