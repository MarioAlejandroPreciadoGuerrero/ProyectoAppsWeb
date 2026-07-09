package com.example.Bemole_API.service;

import com.example.Bemole_API.dto.pago.CrearPagoResponseDTO;
import com.example.Bemole_API.enums.EstadoOrden;
import com.example.Bemole_API.enums.EstadoPago;
import com.example.Bemole_API.exception.NegocioException;
import com.example.Bemole_API.exception.RecursoNoEncontradoException;
import com.example.Bemole_API.models.ItemOrden;
import com.example.Bemole_API.models.Orden;
import com.example.Bemole_API.models.Pago;
import com.example.Bemole_API.models.Producto;
import com.example.Bemole_API.repositorys.OrdenRepository;
import com.example.Bemole_API.repositorys.PagoRepository;
import com.example.Bemole_API.repositorys.ProductoRepository;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagoService {

    private final OrdenRepository ordenRepository;
    private final PagoRepository pagoRepository;
    private final ProductoRepository productoRepository;

    @Value("${mercadopago.access-token}")
    private String mercadoPagoAccessToken;

    @Value("${app.frontend.cliente.url}")
    private String frontendClienteUrl;

    @Value("${app.api.url}")
    private String apiUrl;

    public PagoService(OrdenRepository ordenRepository,PagoRepository pagoRepository,ProductoRepository productoRepository){
        this.ordenRepository=ordenRepository;
        this.pagoRepository=pagoRepository;
        this.productoRepository=productoRepository;
    }

    @Transactional
    public CrearPagoResponseDTO crearPago(
            Long ordenId
    ) throws Exception {

        Orden orden =
                ordenRepository.findById(ordenId)
                        .orElseThrow(() ->
                                new RecursoNoEncontradoException(
                                        "Orden no encontrada"
                                )
                        );

        if (orden.getEstadoPago() == EstadoPago.APROBADO) {
            throw new NegocioException(
                    "La orden ya fue pagada."
            );
        }

        if (orden.getTotal() == null
                || orden.getTotal().compareTo(BigDecimal.ZERO) <= 0) {

            throw new NegocioException(
                    "El total de la orden debe ser mayor que cero."
            );
        }

        if (frontendClienteUrl == null
                || frontendClienteUrl.isBlank()) {

            throw new NegocioException(
                    "No está configurada la URL pública del proyecto cliente."
            );
        }

        MercadoPagoConfig.setAccessToken(
                mercadoPagoAccessToken
        );

        PreferenceItemRequest item =
                PreferenceItemRequest.builder()
                        .title("Orden Bemole #" + orden.getId())
                        .quantity(1)
                        .unitPrice(orden.getTotal())
                        .currencyId("MXN")
                        .build();

        PreferenceBackUrlsRequest backUrls =
                PreferenceBackUrlsRequest.builder()
                        .success(frontendClienteUrl + "/pago/exito")
                        .pending(frontendClienteUrl + "/pago/pendiente")
                        .failure(frontendClienteUrl + "/pago/error")
                        .build();

        PreferenceRequest.PreferenceRequestBuilder builder =
                PreferenceRequest.builder()
                        .items(List.of(item))
                        .backUrls(backUrls)
                        .externalReference(String.valueOf(orden.getId()));

        if (apiUrl != null
                && apiUrl.startsWith("https://")
                && !apiUrl.contains("localhost")
                && !apiUrl.contains("127.0.0.1")) {

            builder.notificationUrl(
                    apiUrl + "/api/pagos/webhook"
            );
        }

        PreferenceRequest preferenceRequest =
                builder.build();

        try {
            PreferenceClient client = new PreferenceClient();

            Preference preference = client.create(preferenceRequest);

            Pago pago = pagoRepository.findByOrdenId(orden.getId()).orElse(new Pago());

            pago.setOrden(orden);
            pago.setPreferenceId(preference.getId());
            pago.setEstado(EstadoPago.PENDIENTE);
            pago.setMonto(orden.getTotal());
            pago.setFechaActualizacion(LocalDateTime.now());

            if (pago.getFechaCreacion() == null) {
                pago.setFechaCreacion(LocalDateTime.now());
            }

            pagoRepository.save(pago);

            orden.setEstadoPago(EstadoPago.PENDIENTE);
            ordenRepository.save(orden);

            return new CrearPagoResponseDTO(
                    orden.getId(),
                    preference.getId(),
                    preference.getInitPoint()
            );

        } catch (MPApiException exception) {
            System.out.println("ERROR MERCADO PAGO");
            System.out.println("Status: " + exception.getStatusCode());

            if (exception.getApiResponse() != null) {
                System.out.println(
                        "Content: " + exception.getApiResponse().getContent()
                );
            }

            throw new NegocioException(
                    "Mercado Pago rechazó la creación de la preferencia."
            );

        } catch (MPException exception) {
            throw new NegocioException(
                    "No fue posible comunicarse con Mercado Pago."
            );
        }
    }

    @Transactional
    public void procesarPagoMercadoPago(String paymentId, String preferenceId, String externalReference) {
        if (paymentId == null || paymentId.isBlank()) {
            throw new IllegalArgumentException(
                    "No se recibió el ID del pago de Mercado Pago."
            );
        }

        MercadoPagoConfig.setAccessToken(
                mercadoPagoAccessToken
        );

        try {
            PaymentClient paymentClient =
                    new PaymentClient();

            Payment payment =
                    paymentClient.get(
                            Long.valueOf(paymentId)
                    );

            String estadoMercadoPago =
                    payment.getStatus();

            String referenciaExterna =
                    payment.getExternalReference() != null
                            ? payment.getExternalReference()
                            : externalReference;

            if (referenciaExterna == null
                    || referenciaExterna.isBlank()) {
                throw new IllegalArgumentException(
                        "No se recibió la referencia externa de la orden."
                );
            }

            Long ordenId =
                    Long.valueOf(referenciaExterna);

            Orden orden =
                    ordenRepository.findById(ordenId)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "La orden asociada al pago no existe."
                                    )
                            );

            Pago pago =
                    pagoRepository
                            .findByOrdenId(orden.getId())
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "No existe un pago registrado para la orden."
                                    )
                            );

            pago.setMercadoPagoPaymentId(
                    String.valueOf(payment.getId())
            );

            pago.setMetodoPago(
                    payment.getPaymentMethodId()
            );

            pago.setFechaActualizacion(
                    LocalDateTime.now()
            );

            if (preferenceId != null && !preferenceId.isBlank()) {
                pago.setPreferenceId(preferenceId);
            }

            if ("approved".equalsIgnoreCase(estadoMercadoPago)) {
                pago.setEstado(EstadoPago.APROBADO);

                orden.setEstadoPago(EstadoPago.APROBADO);
                orden.setEstado(EstadoOrden.PROCESANDO);

            } else if ("pending".equalsIgnoreCase(estadoMercadoPago)
                    || "in_process".equalsIgnoreCase(estadoMercadoPago)) {

                pago.setEstado(EstadoPago.PENDIENTE);

                orden.setEstadoPago(EstadoPago.PENDIENTE);
                orden.setEstado(EstadoOrden.PENDIENTE);

            } else {
                pago.setEstado(EstadoPago.RECHAZADO);

                orden.setEstadoPago(EstadoPago.RECHAZADO);
                orden.setEstado(EstadoOrden.CANCELADA);
            }

            System.out.println("RETORNO MERCADO PAGO");
            System.out.println("paymentId recibido: " + paymentId);
            System.out.println("preferenceId recibido: " + preferenceId);
            System.out.println("externalReference recibido: " + externalReference);
            System.out.println("payment.status: " + payment.getStatus());
            System.out.println("payment.externalReference: " + payment.getExternalReference());

            pagoRepository.save(pago);
            ordenRepository.save(orden);

        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "El ID del pago o de la orden no tiene un formato válido."
            );

        } catch (MPException | MPApiException exception) {
            throw new IllegalStateException(
                    "No fue posible consultar el pago en Mercado Pago.",
                    exception
            );
        }
    }

    public void procesarWebhook(String paymentId) throws Exception {
        MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);

        PaymentClient paymentClient = new PaymentClient();
        Payment payment = paymentClient.get(Long.valueOf(paymentId));

        String externalReference = payment.getExternalReference();
        Long ordenId = Long.valueOf(externalReference);

        Orden orden = ordenRepository.findConItemsById(ordenId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Orden no encontrada."));

        Pago pago = pagoRepository.findByOrdenId(ordenId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        pago.setMercadoPagoPaymentId(String.valueOf(payment.getId()));
        pago.setMetodoPago(payment.getPaymentMethodId());
        pago.setFechaActualizacion(LocalDateTime.now());

        String status = payment.getStatus();

        if ("approved".equalsIgnoreCase(status)) {
            pago.setEstado(EstadoPago.APROBADO);
            manejarPagoAprobado(orden);

        } else if ("pending".equalsIgnoreCase(status)
                || "in_process".equalsIgnoreCase(status)) {

            pago.setEstado(EstadoPago.PENDIENTE);
            manejarPagoPendiente(orden);

        } else if ("rejected".equalsIgnoreCase(status)) {
            pago.setEstado(EstadoPago.RECHAZADO);
            manejarPagoRechazado(orden);

        } else {
            pago.setEstado(EstadoPago.PENDIENTE);
            manejarPagoPendiente(orden);
        }

        pagoRepository.save(pago);
        ordenRepository.save(orden);
    }

    private void manejarPagoAprobado(Orden orden) {
        orden.setEstadoPago(EstadoPago.APROBADO);

        orden.setEstado(EstadoOrden.PROCESANDO);
    }

    private void manejarPagoPendiente(Orden orden) {
        if (orden.getEstadoPago() == EstadoPago.APROBADO) {
            return;
        }

        orden.setEstadoPago(EstadoPago.PENDIENTE);

        // La orden puede seguir como PENDIENTE.
        orden.setEstado(EstadoOrden.PENDIENTE);
    }

    private void manejarPagoRechazado(Orden orden) {
        if (orden.getEstadoPago() == EstadoPago.APROBADO) {
            return;
        }

        if (orden.getEstadoPago() != EstadoPago.RECHAZADO) {
            devolverStockDeOrden(orden);
        }

        orden.setEstadoPago(EstadoPago.RECHAZADO);
        orden.setEstado(EstadoOrden.CANCELADA);
    }

    private void devolverStockDeOrden(Orden orden) {
        for (ItemOrden item : orden.getItems()) {
            Producto producto = productoRepository.findByIdForUpdate(item.getProducto().getId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Producto no encontrado al devolver stock."
                    ));

            producto.setStock(producto.getStock() + item.getCantidad());

            productoRepository.save(producto);
        }
    }
}
