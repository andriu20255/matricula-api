package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class ConsultarHistorial implements Performable {

    public static ConsultarHistorial propio() {
        return new ConsultarHistorial();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("accessToken");
        String usuario = actor.recall("usuario");
        actor.attemptsTo(
                Get.resource("/api/alumnos/" + usuario + "/historial")
                        .with(req -> req.header("Authorization", "Bearer " + token)));
    }
}