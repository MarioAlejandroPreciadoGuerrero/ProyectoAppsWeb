package com.example.Bemole_Dashboard_Admin.dto.admin.producto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoAdminResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private Boolean activo;
    private Long categoriaId;
    private String categoriaNombre;
    private String imagenUrl;

    public boolean disponible() {
        return Boolean.TRUE.equals(activo)
                && stock != null
                && stock > 0;
    }
}
