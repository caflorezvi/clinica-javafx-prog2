package co.edu.uniquindio.clinica.modelo;

import co.edu.uniquindio.clinica.modelo.enums.TipoServicio;
import lombok.*;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class Servicio {

    private TipoServicio nombre;
    private double precio;

}
