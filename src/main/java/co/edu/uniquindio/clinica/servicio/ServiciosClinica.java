package co.edu.uniquindio.clinica.servicio;

import co.edu.uniquindio.clinica.modelo.Factura;
import co.edu.uniquindio.clinica.modelo.Paciente;
import co.edu.uniquindio.clinica.modelo.enums.TipoServicio;
import co.edu.uniquindio.clinica.modelo.enums.TipoSuscripcion;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ServiciosClinica {

    void registrarPaciente(String identificacion, String nombre, String telefono, String correo, TipoSuscripcion suscripcion) throws Exception;

    Factura agendarCita(String idPaciente, TipoServicio tipoServicio, LocalDate dia, String hora) throws Exception;

    void cancelarCita(String citaId);

    Paciente obtenerPaciente(String identificacion);

    boolean hayDisponibilidad(LocalDate dia, LocalTime hora, TipoServicio tipoServicio);

    List<String> generarHorarios();

}
