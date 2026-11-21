package pe.edu.cibertec.matricula_api.screenplay.task;

import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.Map;

public class IniciarSesion implements Performable {

    private final String usuario;
    private final String clave;

    private IniciarSesion(String usuario, String clave) {
        this.usuario = usuario;
        this.clave = clave;
    }

    public static IniciarSesion como(String usuario, String clave) {
        return new IniciarSesion(usuario, clave);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Post.to("/api/auth/login").with(req -> req
                        .contentType(ContentType.JSON)
                        .body(Map.of("username", usuario, "password", clave))));
        if (SerenityRest.lastResponse().statusCode() == 200) {
            String access = SerenityRest.lastResponse().jsonPath().getString("accessToken");
            String refresh = SerenityRest.lastResponse().jsonPath().getString("refreshToken");
            actor.remember("accessToken", access);
            actor.remember("refreshToken", refresh);
            actor.remember("refreshOriginal", refresh);
            actor.remember("usuario", usuario);
        }
    }
}
