package com.example.E_commerce_Bemole.dto.ordenes.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenCreadaResponseDTO {
    private Long id;
    private String numero;
    private LocalDateTime fecha;
    private String estado;
    private ContactoOrdenResponseDTO contacto;
    private DireccionOrdenResponseDTO direccionEnvio;
    private String metodoEnvio;
    private BigDecimal subtotal;
    private BigDecimal costoEnvio;
    private BigDecimal total;
    private List<ItemOrdenResponseDTO> items = new ArrayList<>();
}
