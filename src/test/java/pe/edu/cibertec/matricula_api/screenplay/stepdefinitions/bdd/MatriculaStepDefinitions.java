package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions.bdd;

import io.cucumber.java.es.*;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class MatriculaStepDefinitions {

    private static final String BASE = "http://localhost:8080";
    private String alumno;
    private String token;
    private Response respuesta;
    private long idMatricula;

    @Dado("que el alumno {string} inicia sesión con la clave {string}")
    public void login(String usuario, String clave) {
        alumno = usuario;
        token = given().baseUri(BASE).contentType(ContentType.JSON)
                .body(Map.of("username", usuario, "password", clave))
                .post("/api/auth/login")
                .then().statusCode(200).extract().path("accessToken");
    }

    @Cuando("cotiza el curso {string}")
    public void cotizar(String curso) {
        respuesta = given().baseUri(BASE).auth().oauth2(token).contentType(ContentType.JSON)
                .body(Map.of("codigoAlumno", alumno, "cursos", List.of(curso)))
                .post("/api/matriculas/cotizacion");
    }

    @Cuando("se matricula en el curso {string}")
    public void matricular(String curso) {
        respuesta = given().baseUri(BASE).auth().oauth2(token).contentType(ContentType.JSON)
                .body(Map.of("codigoAlumno", alumno, "codigoCurso", curso))
                .post("/api/matriculas");
        if (respuesta.statusCode() == 201) {
            idMatricula = respuesta.jsonPath().getLong("id");
        }
    }

    @Cuando("paga la matrícula con el método {string}")
    public void pagar(String metodo) {
        respuesta = given().baseUri(BASE).auth().oauth2(token).contentType(ContentType.JSON)
                .body(Map.of("metodo", metodo))
                .patch("/api/matriculas/" + idMatricula + "/pagar");
    }

    @Cuando("consulta la constancia del alumno {string}")
    public void constancia(String codigo) {
        respuesta = given().baseUri(BASE).auth().oauth2(token)
                .get("/api/alumnos/" + codigo + "/constancia");
    }

    @Entonces("la API responde con código {int}")
    public void codigo(int esperado) {
        assertEquals(esperado, respuesta.statusCode());
    }

    @Entonces("la cotización muestra {int} créditos")
    public void creditos(int esperado) {
        assertEquals(esperado, respuesta.jsonPath().getInt("creditos"));
    }

    @Entonces("la cotización muestra un subtotal de {string}")
    public void subtotal(String esperado) {
        assertEquals(Double.parseDouble(esperado), respuesta.jsonPath().getDouble("subtotal"), 0.001);
    }

    @Entonces("la cotización muestra un descuento de {string} por ciento")
    public void descuento(String esperado) {
        assertEquals(Double.parseDouble(esperado), respuesta.jsonPath().getDouble("descuento"), 0.001);
    }

    @Entonces("la cotización muestra un total de {string}")
    public void total(String esperado) {
        assertEquals(Double.parseDouble(esperado), respuesta.jsonPath().getDouble("total"), 0.001);
    }

    @Entonces("la matrícula queda en estado {string}")
    public void estado(String esperado) {
        assertEquals(esperado, respuesta.jsonPath().getString("estado"));
    }

    @Entonces("la matrícula tiene un monto de {string}")
    public void monto(String esperado) {
        assertEquals(Double.parseDouble(esperado), respuesta.jsonPath().getDouble("monto"), 0.001);
    }

    @Entonces("la respuesta cumple el esquema JSON {string}")
    public void esquema(String ruta) {
        respuesta.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(ruta));
    }

    @Entonces("el pago registra una fecha de pago")
    public void fechaPago() {
        assertNotNull(respuesta.jsonPath().getString("fechaPago"));
    }

    @Entonces("en los pagos existe esa matrícula con método {string} y número de operación con formato OP-000000")
    public void pagos(String metodo) {
        Response r = given().baseUri(BASE).auth().oauth2(token).get("/api/pagos");
        assertEquals(200, r.statusCode());
        List<Map<String, Object>> lista = r.jsonPath().getList("$");
        Map<String, Object> pago = lista.stream()
                .filter(p -> ((Number) p.get("idMatricula")).longValue() == idMatricula)
                .findFirst().orElseThrow(() -> new AssertionError("No existe el pago de la matrícula " + idMatricula));
        assertEquals(metodo, pago.get("metodo"));
        assertTrue(pago.get("numeroOperacion").toString().matches("OP-\\d{6}"));
    }

    @Entonces("la API responde con el código de error {string}")
    public void codigoError(String esperado) {
        assertEquals(esperado, respuesta.jsonPath().getString("codigo"));
    }
}