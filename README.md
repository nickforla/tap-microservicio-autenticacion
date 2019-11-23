# Microservicio Autenticación - Técnicas Avanzadas de Programación
Este repositorio contiene el código del microservicio de autenticación del sistema de validación crediticia realizado como trabajo práctico para materia Técnicas Avanzadas de Programación. Se encarga de la autenticación del sistema, generación de los JWT y administración de cuota máxima de solicitudes de estado que un usuario puede realizar en un periodo de una hora.
La autorización de los usuarios para los endpoints que no son de autenticación es realizada por un API Gateway, que delega las peticiones a este microservicio.

## Instalación
### Requerimientos
- JDK 8
- Mongo DB
  - Base de datos: **tap-auth-db**
- Variables de entorno  
  - ```JAVA_HOME```
  - ```MONGODB_HOST```: Host donde se encuentra la instancia corriendo
  - ```AUTH_DB_USER```: Usuario de conexión a la base de datos
  - ```AUTH_DB_PWD```: Contraseña del usuario para conexión con base de datos
  - ```TAP_JWT_SECRET```: Secreto compartido para validar los JWT

```bash

git clone https://github.com/nickforla/tap-microservicio-autenticacion.git

cd tap-microservicio-autenticacion/

./mvnw install

java -jar target/tap-microservicio-autenticacion-1.0.0.jar

```
Una vez que el jar fue ejecutado, el servicio corre en el puerto 8083.

## Endpoints
- Autenticación
  - POST
  - /auth
  - Permite a un usuario obtener un JWT a partir de el envío de usuario y contraseña válidos en el cuerpo de la petición en formato JSON.

- Get Cuota Máxima
  - GET
  - /requests/cuotaMaximaPorHora/
  - Permite obtener la cuota máxima de solicitudes de estado que un usuario puede llevar a cabo en un periodo de una hora.
- Put Cuota Máxima.
  - PUT
  - /requests/cuotaMaximaPorHora/{nuevaCuotaMaxima}
  - Permite a un usuario modificar la cuota máxima de solicitudes de estado que puede llevar a cabo en un periodo de una hora.


