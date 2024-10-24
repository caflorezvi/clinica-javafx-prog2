package co.edu.uniquindio.clinica.modelo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class Factura {

    private String id;
    private LocalDateTime fecha;
    private double subtotal;
    private double total;

}
