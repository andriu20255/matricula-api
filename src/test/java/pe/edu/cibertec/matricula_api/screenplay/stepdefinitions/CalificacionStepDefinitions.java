package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.restassured.module.jsv.JsonSchemaValidator;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import pe.edu.cibertec.matricula_api.screenplay.questions.CodigoDeError;
import pe.edu.cibertec.matricula_api.screenplay.questions.ElHistorial;
import pe.edu.cibertec.matricula_api.screenplay.task.CalificarMatricula;
import pe.edu.cibertec.matricula_api.screenplay.task.ConsultarHistorial;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalificacionStepDefinitions {

    @Cuando("el docente {string} califica el curso {string} de la alumna {string} con nota {double}")
    public void califica(String docente, String curso, String alumna, double nota) {
        String tokenAlumna = OnStage.theActorCalled(alumna).recall("accessToken");
        Actor profesor = OnStage.theActorCalled(docente);
        profesor.attemptsTo(CalificarMatricula.deCurso(curso, tokenAlumna, nota));
    }

    @Entonces("la calificación responde con código {int}")
    public void codigo(int esperado) {
        assertEquals(esperado, SerenityRest.lastResponse().statusCode());
    }

    @Entonces("la condición registrada es {string}")
    public void condicion(String esperada) {
        assertEquals(esperada, SerenityRest.lastResponse().jsonPath().getString("condicion"));
    }

    @Cuando("la alumna {string} consulta su historial")
    public void consultaHistorial(String alumna) {
        OnStage.theActorCalled(alumna).attemptsTo(ConsultarHistorial.propio());
    }

    @Entonces("el historial tiene {int} créditos aprobados y promedio ponderado {double}")
    public void verificaHistorial(int creditos, double promedio) {
        Actor actor = OnStage.theActorInTheSpotlight();
        assertEquals(creditos, actor.asksFor(ElHistorial.creditosAprobados()), 0.001);
        assertEquals(promedio, actor.asksFor(ElHistorial.promedioPonderado()), 0.01);
    }

    @Entonces("el historial cumple el esquema {string}")
    public void esquemaHistorial(String ruta) {
        SerenityRest.lastResponse().then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(ruta));
    }

    @Entonces("el error de calificación es {string}")
    public void errorCalificacion(String esperado) {
        String real = OnStage.theActorInTheSpotlight().asksFor(CodigoDeError.delUltimoIntento());
        assertEquals(esperado, real);
        SerenityRest.lastResponse().then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/error.json"));
    }
}