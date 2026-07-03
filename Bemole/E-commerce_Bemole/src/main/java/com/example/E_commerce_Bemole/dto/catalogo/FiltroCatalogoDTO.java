package com.example.E_commerce_Bemole.dto.catalogo;

import com.example.E_commerce_Bemole.enums.OrdenProducto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroCatalogoDTO {
    private String q;
    private Long categoriaId;
    private BigDecimal precioMin;
    private BigDecimal precioMax;
    private boolean soloConStock;
    private OrdenProducto orden;
    private int pagina = 0;
    private int tamano = 12;
}
