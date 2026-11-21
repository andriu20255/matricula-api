package pe.edu.cibertec.matricula_api.screenplay.task;

import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.serenitybdd.screenplay.rest.interactions.Patch;

import java.util.Map;

public class CalificarMatricula implements Performable {

    private final String curso;
    private final String tokenAlumna;
    private final double nota;

    private CalificarMatricula(String curso, String tokenAlumna, double nota) {
        this.curso = curso;
        this.tokenAlumna = tokenAlumna;
        this.nota = nota;
    }

    public static CalificarMatricula deCurso(String curso, String tokenAlumna, double nota) {
        return new CalificarMatricula(curso, tokenAlumna, nota);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        actor.attemptsTo(
                Get.resource("/api/matriculas").with(req -> req
                        .header("Authorization", "Bearer " + tokenAlumna)
                        .queryParam("curso", curso)));
        long id = SerenityRest.lastResponse().jsonPath()
                .getLong("find { it.codigoCurso == '" + curso + "' }.id");


        String tokenDocente = actor.recall("accessToken");
        actor.attemptsTo(
                Patch.to("/api/matriculas/" + id + "/nota").with(req -> req
                        .header("Authorization", "Bearer " + tokenDocente)
                        .contentType(ContentType.JSON)
                        .body(Map.of("nota", nota))));
    }
}