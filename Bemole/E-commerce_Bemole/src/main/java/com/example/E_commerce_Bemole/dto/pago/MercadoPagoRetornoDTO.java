package com.example.E_commerce_Bemole.dto.pago;

public record MercadoPagoRetornoDTO(String paymentId,
                                    String status,
                                    String preferenceId,
                                    String externalReference) {
}
