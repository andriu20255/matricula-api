# language: es
@p3
Característica: Calificación de matrículas

  Esquema del escenario: el docente registra la nota y el historial se actualiza
    Dado que "Rosa" inicia sesión con el usuario "i202610005" y la clave "Cibertec2026"
    Y que "Marco" inicia sesión con el usuario "D0001" y la clave "Cibertec2026"
    Cuando el docente "Marco" califica el curso "PI5620" de la alumna "Rosa" con nota <nota>
    Entonces la calificación responde con código 200
    Y la condición registrada es "<condicion>"
    Cuando la alumna "Rosa" consulta su historial
    Entonces el historial tiene <creditos> créditos aprobados y promedio ponderado <promedio>
    Y el historial cumple el esquema "schemas/historial.json"

    Ejemplos:
      | nota | condicion   | creditos | promedio |
      | 18   | APROBADO    | 13       | 16,62    |
      | 12,5 | DESAPROBADO | 7        | 14,08    |

  Escenario: Un docente que no dicta el curso intenta registrar la nota
    Dado que "Rosa" inicia sesión con el usuario "i202610005" y la clave "Cibertec2026"
    Y que "Lucía" inicia sesión con el usuario "D0002" y la clave "Cibertec2026"
    Cuando el docente "Lucía" califica el curso "PI5620" de la alumna "Rosa" con nota 15
    Entonces la calificación responde con código 403
    Y el error de calificación es "ACCESO_DENEGADO"

  Escenario: No se puede calificar una matrícula que no está pagada
    Dado que "Rosa" inicia sesión con el usuario "i202610005" y la clave "Cibertec2026"
    Y que "Marco" inicia sesión con el usuario "D0001" y la clave "Cibertec2026"
    Cuando el docente "Marco" califica el curso "PS5511" de la alumna "Rosa" con nota 15
    Entonces la calificación responde con código 409
    Y el error de calificación es "MATRICULA_NO_PAGADA"