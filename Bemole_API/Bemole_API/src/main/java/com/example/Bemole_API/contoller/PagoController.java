package com.example.Bemole_API.contoller;

import com.example.Bemole_API.dto.pago.CrearPagoRequestDTO;
import com.example.Bemole_API.dto.pago.CrearPagoResponseDTO;
import com.example.Bemole_API.service.PagoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/pagos")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class PagoController {
    private final PagoService service;

    @PostMapping()
    public ResponseEntity<CrearPagoResponseDTO> crearPago(@RequestBody CrearPagoRequestDTO request) throws Exception {
        CrearPagoResponseDTO response = service.crearPago(request.getOrdenId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> recibirWebhook(
            @RequestParam(required = false, name = "data.id") String dataId,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String type,
            @RequestBody(required = false) String body
    ) {
        try {
            System.out.println("WEBHOOK MERCADO PAGO");
            System.out.println("data.id: " + dataId);
            System.out.println("id: " + id);
            System.out.println("topic: " + topic);
            System.out.println("type: " + type);
            System.out.println("body: " + body);

            String paymentId = dataId != null ? dataId : id;

            if (paymentId != null && !paymentId.isBlank()) {
                service.procesarWebhook(paymentId);
            }

            return ResponseEntity.ok("OK");

        } catch (Exception e) {
            e.printStackTrace();

            // Importante:
            // Responde 200 para evitar reintentos infinitos mientras estás desarrollando.
            return ResponseEntity.ok("OK");
        }
    }
}
