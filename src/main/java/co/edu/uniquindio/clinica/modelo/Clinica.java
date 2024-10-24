package co.edu.uniquindio.clinica.modelo;

import co.edu.uniquindio.clinica.modelo.enums.TipoServicio;
import co.edu.uniquindio.clinica.modelo.enums.TipoSuscripcion;
import co.edu.uniquindio.clinica.modelo.factory.Suscripcion;
import co.edu.uniquindio.clinica.modelo.factory.SuscripcionBasica;
import co.edu.uniquindio.clinica.modelo.factory.SuscripcionPremium;
import co.edu.uniquindio.clinica.servicio.ServiciosClinica;
import co.edu.uniquindio.clinica.utils.EnvioEmail;
import lombok.Getter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Clinica implements ServiciosClinica {

    private final List<Paciente> pacientes;
    private final List<Cita> citas;
    private static Clinica INSTANCIA;

    private Clinica() {
        this.pacientes = new ArrayList<>();
        this.citas = new ArrayList<>();
    }

    public static Clinica getInstancia(){
        if(INSTANCIA == null){
            INSTANCIA = new Clinica();
        }
        return INSTANCIA;
    }

    @Override
    public void registrarPaciente(String identificacion, String nombre, String telefono, String correo, TipoSuscripcion suscripcion) throws Exception{

        String mensajesValidacion = "";

        if(identificacion == null || identificacion.isEmpty()){
            mensajesValidacion += "Debe ingresar la identificación del paciente\n";
        }

        if(nombre == null || nombre.isEmpty()){
            mensajesValidacion += "Debe ingresar el nombre del paciente\n";
        }

        if(telefono == null || telefono.isEmpty()){
            mensajesValidacion += "Debe ingresar el teléfono del paciente\n";
        }

        if(correo == null || correo.isEmpty()){
            mensajesValidacion += "Debe ingresar el correo del paciente\n";
        }

        if(suscripcion == null){
            mensajesValidacion += "Debe seleccionar una suscripción\n";
        }

        if(!mensajesValidacion.isEmpty()){
            throw new Exception(mensajesValidacion);
        }

        if(obtenerPaciente(identificacion)!=null){
            throw new Exception("Ya existe un paciente con la identificación ingresada");
        }

        Suscripcion suscripcionPaciente = switch (suscripcion) {
            case BASICA -> new SuscripcionBasica();
            case PREMIUM -> new SuscripcionPremium();
        };

        Paciente paciente = Paciente.builder()
                .suscripcion(suscripcionPaciente)
                .cedula(identificacion)
                .nombre(nombre)
                .telefono(telefono)
                .email(correo)
                .build();

        pacientes.add(paciente);
    }

    @Override
    public Factura agendarCita(String idPaciente, TipoServicio tipoServicio, LocalDate dia, String hora) throws Exception{

        String mensajesValidacion = "";

        if(idPaciente == null || idPaciente.isEmpty()){
            mensajesValidacion += "Debe ingresar la identificación del paciente\n";
        }

        if(tipoServicio == null){
            mensajesValidacion += "Debe seleccionar un servicio\n";
        }

        if(dia == null){
            mensajesValidacion += "Debe seleccionar el día de la cita\n";
        }

        if(hora == null){
            mensajesValidacion += "Debe seleccionar la hora de la cita\n";
        }

        if(!mensajesValidacion.isEmpty()){
            throw new Exception(mensajesValidacion);
        }

        if(dia.isBefore(LocalDate.now())){
            throw new Exception("La fecha de la cita no puede ser anterior a la fecha actual");
        }

        LocalTime horaCita = LocalTime.parse(hora);

        if(!hayDisponibilidad(dia, horaCita, tipoServicio)){
            throw new Exception("Ya existe una cita agendada para el día y hora seleccionados");
        }

        Paciente paciente = obtenerPaciente(idPaciente);
        Factura factura = paciente.getSuscripcion().generarFacturaCobro(tipoServicio);

        Cita cita = Cita.builder()
                .paciente(paciente)
                .id(String.valueOf(UUID.randomUUID()))
                .dia(dia)
                .hora(horaCita)
                .servicio(tipoServicio)
                .factura(factura)
                .build();

        citas.add(cita);

        String mensaje = paciente
                + "\nServicio: " + tipoServicio.getNombre()
                + "\nDía: " + cita.getDia()
                + "\nHora: " + cita.getHora()
                + "\nSubtotal: $" + factura.getSubtotal()
                + "\nTotal: $" + factura.getTotal();

        //EnvioEmail.enviarNotificacion(paciente.getEmail(), "Se ha agendado una consulta médica", mensaje);

        return factura;
    }

    @Override
    public void cancelarCita(String citaId) {
        for (Cita cita: citas) {
            if(cita.getId().equals(citaId)){
                citas.remove(cita);
                break;
            }
        }
    }

    @Override
    public Paciente obtenerPaciente(String identificacion) {
        for (Paciente paciente: pacientes) {
            if(paciente.getCedula().equals(identificacion)){
                return paciente;
            }
        }
        return null;
    }

    @Override
    public boolean hayDisponibilidad(LocalDate dia, LocalTime hora, TipoServicio tipoServicio) {
        for (Cita cita: citas) {
            if(cita.getDia().equals(dia) && cita.getHora().equals(hora) && cita.getServicio() == tipoServicio){
                return false;
            }
        }
        return true;
    }

    @Override
    public List<String> generarHorarios() {
        List<String> horarios = new ArrayList<>();
        for (int i = 8; i < 18; i++) {
            if(i < 10){
                horarios.add("0" + i + ":00");
                horarios.add("0" + i + ":30");
            }else{
                horarios.add(i + ":00");
                horarios.add(i + ":30");
            }
        }
        return horarios;
    }

}
