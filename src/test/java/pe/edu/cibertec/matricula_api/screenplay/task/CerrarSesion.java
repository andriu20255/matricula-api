package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.rest.interactions.Post;

public class CerrarSesion implements Performable {

    public static CerrarSesion actual() {
        return new CerrarSesion();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("accessToken");
        actor.attemptsTo(
                Post.to("/api/auth/logout")
                        .with(req -> req.header("Authorization", "Bearer " + token)));
    }
}