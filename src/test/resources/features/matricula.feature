# language: es
@p1
Característica: Matrícula del alumno

  Antecedentes:
    Dado que el alumno "i202610002" inicia sesión con la clave "Cibertec2026"

  Escenario: Cotizar el curso
    Cuando cotiza el curso "BD5410"
    Entonces la API responde con código 200
    Y la cotización muestra 4 créditos
    Y la cotización muestra un subtotal de "480.0"
    Y la cotización muestra un descuento de "10" por ciento
    Y la cotización muestra un total de "432.0"

  Escenario: Matricularse y pagar el curso
    Cuando se matricula en el curso "BD5410"
    Entonces la API responde con código 201
    Y la matrícula queda en estado "REGISTRADA"
    Y la matrícula tiene un monto de "432.0"
    Y la respuesta cumple el esquema JSON "schemas/matricula.json"
    Cuando paga la matrícula con el método "YAPE"
    Entonces la API responde con código 200
    Y la matrícula queda en estado "PAGADA"
    Y el pago registra una fecha de pago
    Y en los pagos existe esa matrícula con método "YAPE" y número de operación con formato OP-000000

  Escenario: Regla de negocio que debe fallar
    Cuando consulta la constancia del alumno "i202610003"
    Entonces la API responde con código 403
    Y la API responde con el código de error "ACCESO_DENEGADO"
    Y la respuesta cumple el esquema JSON "schemas/error.json"