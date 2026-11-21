package pe.edu.cibertec.matricula_api.screenplay.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class ElHistorial implements Question<Double> {

    private final String campo;

    private ElHistorial(String campo) {
        this.campo = campo;
    }

    public static ElHistorial creditosAprobados() {
        return new ElHistorial("creditosAprobados");
    }

    public static ElHistorial promedioPonderado() {
        return new ElHistorial("promedioPonderado");
    }

    @Override
    public Double answeredBy(Actor actor) {
        return SerenityRest.lastResponse().jsonPath().getDouble(campo);
    }
}