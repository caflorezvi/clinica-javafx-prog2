package co.edu.uniquindio.clinica.controladores;

import co.edu.uniquindio.clinica.modelo.Clinica;
import co.edu.uniquindio.clinica.modelo.Factura;
import co.edu.uniquindio.clinica.modelo.enums.TipoServicio;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AgendarCitaControlador implements Initializable{

    @FXML
    private TextField txtCedulaPaciente;

    @FXML
    private DatePicker dpDiaCita;

    @FXML
    private ComboBox<String> cbHoraCita;

    @FXML
    private ComboBox<TipoServicio> cbServicio;

    private final Clinica clinica;

    public AgendarCitaControlador(){
        clinica = Clinica.getInstancia();
    }

    public void registrarCita() {

        try {

            String cedulaPaciente = txtCedulaPaciente.getText();
            LocalDate fecha = dpDiaCita.getValue();
            String hora = cbHoraCita.getValue();
            TipoServicio servicio = cbServicio.getValue();

            Factura factura = clinica.agendarCita(cedulaPaciente, servicio, fecha, hora);
            limpiarFormularioCita();
            mostrarAlerta(
                    "Cita agendada correctamente.\n\n" +
                    "Detalle factura:\n" +
                    "ID: "+factura.getId()+"\n" +
                    "Subtotal: $"+factura.getSubtotal()+"\n" +
                    "Total: $"+factura.getTotal(),
                    Alert.AlertType.INFORMATION
            );
        } catch (Exception ex) {
            mostrarAlerta(ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void limpiarFormularioCita() {
        txtCedulaPaciente.setText("");
        dpDiaCita.setValue(null);
        cbHoraCita.setValue(null);
        cbServicio.setValue(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbHoraCita.setItems(FXCollections.observableArrayList(clinica.generarHorarios()));
        cbServicio.setItems(FXCollections.observableArrayList(TipoServicio.values()));
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo){
        Alert alert = new Alert(tipo);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
}
