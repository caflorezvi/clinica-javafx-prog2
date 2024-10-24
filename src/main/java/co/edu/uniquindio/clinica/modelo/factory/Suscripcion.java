package co.edu.uniquindio.clinica.modelo.factory;

import co.edu.uniquindio.clinica.modelo.Factura;
import co.edu.uniquindio.clinica.modelo.Servicio;
import co.edu.uniquindio.clinica.modelo.enums.TipoServicio;
import co.edu.uniquindio.clinica.modelo.enums.TipoSuscripcion;

import java.util.List;

public interface Suscripcion {

    TipoSuscripcion getNombreSuscripcion();

    List<Servicio> getServiciosDisponibles();

    Factura generarFacturaCobro(TipoServicio servicio);

}
