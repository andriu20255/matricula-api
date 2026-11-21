package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions.bdd;

import io.cucumber.java.Before;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import pe.edu.cibertec.matricula_api.screenplay.questions.CodigoDeError;
import pe.edu.cibertec.matricula_api.screenplay.questions.CodigoHttp;
import pe.edu.cibertec.matricula_api.screenplay.task.CerrarSesion;
import pe.edu.cibertec.matricula_api.screenplay.task.ConsultarPerfil;
import pe.edu.cibertec.matricula_api.screenplay.task.IniciarSesion;
import pe.edu.cibertec.matricula_api.screenplay.task.ListarAlumnos;
import pe.edu.cibertec.matricula_api.screenplay.task.RenovarToken;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

public class SeguridadStepDefinitions {

    private static final String BASE = "http://localhost:8080";

    @Before("@p2")
    public void prepararEscenario() {
        OnStage.setTheStage(new OnlineCast());
    }

    private Actor actor(String nombre) {
        return OnStage.theActorCalled(nombre).whoCan(CallAnApi.at(BASE));
    }

    @Dado("que {string} inicia sesión con el usuario {string} y la clave {string}")
    public void iniciaSesion(String nombre, String usuario, String clave) {
        actor(nombre).attemptsTo(IniciarSesion.como(usuario, clave));
    }

    @Dado("que {string} intenta iniciar sesión con el usuario {string} y la clave {string}")
    public void intentaIniciarSesion(String nombre, String usuario, String clave) {
        actor(nombre).attemptsTo(IniciarSesion.como(usuario, clave));
    }

    @Cuando("renueva su sesión con el refresh token")
    public void renueva() {
        theActorInTheSpotlight().attemptsTo(RenovarToken.conElRefreshActual());
    }

    @Entonces("el refresh responde con código 200 y tokens nuevos")
    public void refreshOk() {
        Actor a = theActorInTheSpotlight();
        a.should(seeThat(CodigoHttp.delUltimoIntento(), equalTo(200)));
        assertThat(SerenityRest.lastResponse().path("accessToken"), notNullValue());
        assertThat(SerenityRest.lastResponse().path("refreshToken"),
                not(equalTo(a.recall("refreshOriginal"))));
    }

    @Cuando("intenta renovar otra vez con el refresh token original")
    public void renuevaConElOriginal() {
        theActorInTheSpotlight().attemptsTo(RenovarToken.conElRefreshOriginal());
    }

    @Cuando("consulta su perfil con su access token actual")
    public void consultaPerfil() {
        theActorInTheSpotlight().attemptsTo(ConsultarPerfil.conElTokenGuardado());
    }

    @Cuando("cierra su sesión")
    public void cierraSesion() {
        theActorInTheSpotlight().attemptsTo(CerrarSesion.actual());
    }

    @Cuando("intenta listar todos los alumnos")
    public void listaAlumnos() {
        theActorInTheSpotlight().attemptsTo(ListarAlumnos.todos());
    }

    @Entonces("el servicio de seguridad responde con código {int}")
    public void codigoHttp(int esperado) {
        theActorInTheSpotlight().should(
                seeThat(CodigoHttp.delUltimoIntento(), equalTo(esperado)));
    }

    @Y("el código de error de seguridad es {string}")
    public void codigoError(String esperado) {
        theActorInTheSpotlight().should(
                seeThat(CodigoDeError.delUltimoIntento(), equalTo(esperado)));
        SerenityRest.lastResponse().then()
                .body(matchesJsonSchemaInClasspath("schemas/error.json"));
    }
}