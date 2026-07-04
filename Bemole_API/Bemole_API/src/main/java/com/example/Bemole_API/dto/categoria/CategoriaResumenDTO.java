package com.example.Bemole_API.dto.categoria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaResumenDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private long cantidadProductos;
}
