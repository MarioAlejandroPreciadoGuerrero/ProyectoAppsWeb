package com.example.Bemole_API.dto.pago;

public record MercadoPagoRetornoDTO(String paymentId,
                                    String status,
                                    String preferenceId,
                                    String externalReference) {
}
