package com.example.Bemole_Dashboard_Admin.dto.admin.producto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoAdminFormDTO {
    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 150, message = "El nombre no puede superar 150 caracteres.")
    private String nombre;

    @Size(max = 1000, message = "La descripción no puede superar 1000 caracteres.")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio.")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que cero.")
    private BigDecimal precio;

    @NotNull(message = "El stock es obligatorio.")
    @Min(value = 0, message = "El stock no puede ser negativo.")
    private Integer stock;

    @NotNull(message = "La categoría es obligatoria.")
    private Long categoriaId;

    @NotNull(message = "Debes indicar si el producto está activo.")
    private Boolean activo = true;

    public static ProductoAdminFormDTO desde(ProductoAdminResponseDTO producto) {
        ProductoAdminFormDTO formulario = new ProductoAdminFormDTO();

        formulario.setNombre(producto.getNombre());
        formulario.setDescripcion(producto.getDescripcion());
        formulario.setPrecio(producto.getPrecio());
        formulario.setStock(producto.getStock());
        formulario.setCategoriaId(producto.getCategoriaId());
        formulario.setActivo(producto.getActivo());

        return formulario;
    }
}
