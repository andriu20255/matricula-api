package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions.bdd;
import io.cucumber.java.Before;
import java.time.LocalDateTime;

public class IdentificacionHook {
    @Before(order = 0)
    public void identificar() {
        System.out.println("Alumno: ANDREA ANTUANE ESPINOZA YATO | Código: i202503078 | " + LocalDateTime.now());
    }
}
