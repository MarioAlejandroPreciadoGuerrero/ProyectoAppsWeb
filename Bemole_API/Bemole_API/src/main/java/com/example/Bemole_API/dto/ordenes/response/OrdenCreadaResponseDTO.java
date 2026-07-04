package com.example.Bemole_API.dto.ordenes.response;

import com.example.Bemole_API.enums.EstadoOrden;
import com.example.Bemole_API.enums.MetodoEnvio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenCreadaResponseDTO {

    private Long id;
    private String numero;
    private LocalDateTime fecha;
    private EstadoOrden estado;
    private ContactoOrdenResponseDTO contacto;
    private DireccionOrdenResponseDTO direccionEnvio;
    private MetodoEnvio metodoEnvio;
    private BigDecimal subtotal;
    private BigDecimal costoEnvio;
    private BigDecimal total;
    private List<ItemOrdenResponseDTO> items;

}
