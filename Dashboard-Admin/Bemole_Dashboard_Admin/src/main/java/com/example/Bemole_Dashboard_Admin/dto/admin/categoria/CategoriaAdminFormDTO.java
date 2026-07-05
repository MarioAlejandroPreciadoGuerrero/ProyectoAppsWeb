package com.example.Bemole_Dashboard_Admin.dto.admin.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaAdminFormDTO {
    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres.")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede superar 500 caracteres.")
    private String descripcion;

    public static CategoriaAdminFormDTO desde(CategoriaAdminResponseDTO categoria) {
        CategoriaAdminFormDTO formulario = new CategoriaAdminFormDTO();

        formulario.setNombre(categoria.getNombre());
        formulario.setDescripcion(categoria.getDescripcion());

        return formulario;
    }
}
