# language: es
@p2
Característica: Seguridad de la sesión

  Escenario: Rotación del refresh token
    Dado que "Luis" inicia sesión con el usuario "i202610002" y la clave "Cibertec2026"
    Cuando renueva su sesión con el refresh token
    Entonces el refresh responde con código 200 y tokens nuevos
    Cuando intenta renovar otra vez con el refresh token original
    Entonces el servicio de seguridad responde con código 401
    Y el código de error de seguridad es "REFRESH_REUTILIZADO"
    Cuando consulta su perfil con su access token actual
    Entonces el servicio de seguridad responde con código 401
    Y el código de error de seguridad es "TOKEN_INVALIDO"

  Escenario: Cierre de sesión
    Dado que "Luis" inicia sesión con el usuario "i202610002" y la clave "Cibertec2026"
    Cuando cierra su sesión
    Entonces el servicio de seguridad responde con código 204
    Cuando consulta su perfil con su access token actual
    Entonces el servicio de seguridad responde con código 401
    Y el código de error de seguridad es "TOKEN_INVALIDO"

  Esquema del escenario: Bloqueo de cuenta por intentos fallidos
    Dado que "Jorge" intenta iniciar sesión con el usuario "i202610004" y la clave "<clave>"
    Entonces el servicio de seguridad responde con código <status>
    Y el código de error de seguridad es "<codigo>"

    Ejemplos:
      | intento | clave        | status | codigo                 |
      | 1       | Mala1        | 401    | CREDENCIALES_INVALIDAS |
      | 2       | Mala2        | 401    | CREDENCIALES_INVALIDAS |
      | 3       | Mala3        | 401    | CREDENCIALES_INVALIDAS |
      | 4       | Mala4        | 401    | CREDENCIALES_INVALIDAS |
      | 5       | Mala5        | 423    | CUENTA_BLOQUEADA       |
      | 6       | Cibertec2026 | 423    | CUENTA_BLOQUEADA       |

  Escenario: Alumno sin permiso de administrador
    Dado que "Luis" inicia sesión con el usuario "i202610002" y la clave "Cibertec2026"
    Cuando intenta listar todos los alumnos
    Entonces el servicio de seguridad responde con código 403
    Y el código de error de seguridad es "ACCESO_DENEGADO"