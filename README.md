# simple user auth service

Este proyecto registra usuarios con telefonos en la base de datos en memoria (h2) y realiza validaciones de email y contraseña.

## ¿Qué contiene este proyecto?
- Seguridad con spring security
- Generación de tokens de acceso
- Uso de base de dato en memoria (h2)
- Pruebas unitarias (controller, services, y capa se seguridad)
- **Open API** (Documentación en swagger)

## Requisitos
- Java 17
- Spring boot 3.3.4
- Spring security 6

## Probar el proyecto
Puedes clonar el proyecto asegurandote de cumplir con los requisitos mencionados.

### Instalación en tu máquina

1. Clonar el proyecto
```bash
git clone https://github.com/berracode/simple-users-auth-service.git
```

2. Ejecutarlo
```bash
$ ./gradlew build # en Linux o Mac
$ .\gradlew.bat build # en windows
```

### Ejecutar test unitarios
```bash
$  .\gradlew.bat test
```

## Diagrama de solución

![Alt text](docs/ds_ms-user.png
)

## Endpoints

### user/register
Permite registrar un usuario y posteriormente autenticarse usando el endpoint *users/login*

#### Curl
```bash
curl --location 'http://localhost:8080/ms-user/users/register' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "Juan",
  "email": "juan@mail.com",
  "password": "1Assssss20*",
  "phones": [
    {
      "number": "2",
      "cityCode": "string",
      "countryCode": "string"
    }
  ]
}'
```

#### Response

```json
{
"id": "39f40ea1-5611-4b24-8168-a7e880fd9c46",
"name": "Juan",
"email": "juan@mail.com",
"password": "$2a$10$woQsL4lgYTNJqSf/evgg7es3IJeoYo/7Xas1brEwyKFqx.1f5YDh.",
"token": null,
"isActive": true,
"createdDate": "2024-10-03T23:16:35.9855063",
"modifiedDate": "2024-10-03T23:16:35.9855063",
"lastLogin": "2024-10-03T23:16:35.9855063"
}
```


### user/login
Este endpoint genera un JWT para poder consumir el/los endpoints privados

#### Curl
```bash
curl --location 'http://localhost:8080/ms-user/users/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "juan@mail.com",
    "password": "1Assssss20*"
}'
```

#### Response

```json
{
  "description": null,
  "message": null,
  "body": {
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWFuQG1haWwuY29tIiwiaWF0IjoxNzI4MDE0NTI4LCJleHAiOjE3MjgwMTUxMjh9.6In1PLp_ftAs6ixw0RndtCjwbHSyNH3bEYBhHrWZ2WRZtHh0wlwdnvr74Jhrx9f-pfTz2CzoiBNvFgMvWN6Ldw",
    "tokenType": "Bearer "
  }
}
```

### users/test-private-endpoint
Este endpoint es privado, se necesita un token de acceso valido para consumirlo

#### Curl
```bash
curl --location --request POST 'http://localhost:8080/ms-user/users/test-private-endpoint' \
--header 'Authorization: Bearer <insert-your-token>'
```

#### Response

```json
{
  "description": "If you can see this text, you have a valid access token",
  "message": "User authenticated",
  "body": null
}
```