package pe.edu.cibertec.matricula_api.screenplay.task;

import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.Map;

public class RenovarToken implements Performable {

    private final String clave;

    private RenovarToken(String clave) {
        this.clave = clave;
    }

    public static RenovarToken conElRefreshActual() {
        return new RenovarToken("refreshToken");
    }

    public static RenovarToken conElRefreshOriginal() {
        return new RenovarToken("refreshOriginal");
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String refresh = actor.recall(clave);
        actor.attemptsTo(
                Post.to("/api/auth/refresh").with(req -> req
                        .contentType(ContentType.JSON)
                        .body(Map.of("refreshToken", refresh))));
        if (SerenityRest.lastResponse().statusCode() == 200) {
            String access = SerenityRest.lastResponse().jsonPath().getString("accessToken");
            String refreshNuevo = SerenityRest.lastResponse().jsonPath().getString("refreshToken");
            actor.remember("accessToken", access);
            actor.remember("refreshToken", refreshNuevo);

        }
    }
}