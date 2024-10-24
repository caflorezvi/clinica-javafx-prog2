package co.edu.uniquindio.clinica.modelo;

import co.edu.uniquindio.clinica.modelo.enums.TipoServicio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Builder
@Getter
public class Cita {

    private String id;
    private LocalDate dia;
    private LocalTime hora;
    private Factura factura;
    private Paciente paciente;
    private TipoServicio servicio;
}
