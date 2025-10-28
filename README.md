# Explicacion del codigo Bank
## Pasos para ejecutar el banco
1. Abre el código en Visual Studio Code.

3. Buscar el archivo que hace de "arrancador" (AppbankApplication.java).
4. Dale al botón de "Ejecutar"
5. La aplicación se inicia y te dice que está lista en el puerto 8080.
   
## Explicación del SRC
Appbank: este bloque de código sirve como un pequeño arrancador del Spring este define donde esta 
como se llama y le indica al spring boot que configure y ejecute.

Perfecto 👍 Aquí tienes tu texto organizado y formateado para un **README.md** de GitHub, usando **Markdown** correctamente con títulos, subtítulos, sangrías y estilo profesional:

---

# 🏦 Proyecto: Aplicación Bancaria con Spring Boot

## 📘 Descripción General

Este proyecto consiste en una **aplicación bancaria digital** desarrollada con **Spring Boot**, estructurada bajo una **arquitectura por capas** (Controlador, Servicio, Modelo, Repositorio) y aplicando principios de **Programación Orientada a Objetos (POO)** y **patrones de diseño (Strategy)**.

El flujo general de la aplicación sigue esta secuencia:

1. El **usuario** envía una petición HTTP al **Controller**.
2. El **Controller** llama al **Service**, que contiene la lógica de negocio.
3. El **Service** manipula los objetos del **Model**, aplicando herencia y estrategias.
4. El **Repository** (en una implementación completa) guardaría los cambios en archivos JSON o una base de datos.
5. Finalmente, el resultado vuelve al **Controller**, que lo envía como respuesta al usuario.

---

## 🧭 Arquitectura de la Aplicación

### 1. **BankController**

Este componente actúa como **traductor entre peticiones externas (HTTP)** y las llamadas al **BankService**, permitiendo que la aplicación responda correctamente con los datos solicitados.

#### 🔹 Manejo de Datos

Extrae datos de:

* **@PathVariable** y **@RequestParam** → desde la URL.
* **@RequestBody** → desde el cuerpo de la petición.

#### 🔹 Endpoints Principales

* `@PostMapping("/customers")` → **Crear Cliente**
  Maneja peticiones POST. Recibe los datos del cliente en el cuerpo de la petición.

* `@GetMapping("/customers")` → **Listar Clientes**
  Devuelve una lista de todos los clientes existentes.

* `@GetMapping("/customers/{customerId}")` → **Buscar Cliente**
  Extrae `{customerId}` de la URL. Devuelve `200 OK` si lo encuentra o `404 NOT FOUND` si no existe.

---

### 2. **Exception**

#### 🧩 DomainException

Clase usada para indicar **fallas específicas de la lógica del banco**, permitiendo que la API REST devuelva mensajes de error **claros y controlados**.

---

### 3. **Model**

Representa la **base de datos conceptual** de la aplicación bancaria.

#### 💳 Account

Clase base que define los atributos y comportamientos comunes de todas las cuentas.
Ejemplos de métodos:

* `deposit(Money amount)` → Deposita dinero y registra la transacción.
* `withdraw(Money amount)` → Retira dinero si hay saldo suficiente.

#### 🏦 CheckingAccount

Subclase de `Account` que añade la lógica específica de **cuentas corrientes**.

* Atributo principal: `overdraftLimit` (límite de sobregiro).
* Métodos sobrescritos:

  * `@Override applyInterest()` → No genera intereses.
  * `@Override withdraw(Money amount)` → Permite sobregiros hasta el límite.

#### 👤 Customer

Clase que representa al cliente.

* Atributos: `id`, `name`, `email`.
* Contiene **constructores, getters y setters** para el manejo de datos encapsulados.

#### 💰 Money

Objeto de valor que representa una **cantidad monetaria específica**.

* Constructores: vacío y con parámetros (`amount`, `currency`).

#### 💸 SavingsAccount

Clase concreta que **hereda de `Account`**.
Define la lógica específica de las **cuentas de ahorro**, incluyendo la aplicación de intereses.

#### 📄 Transaction

Registra cada **movimiento de cuenta**.

* Atributos clave: `type`, `amount`, `accountId`, `timestamp`.
* Cada transacción se registra automáticamente con la hora actual.

---

### 4. **Repository**

#### 📚 JsonRepository

Interfaz genérica que define el **contrato de operaciones CRUD** (Crear, Leer, Actualizar, Eliminar) para manejar entidades como `Customer` o `Account`.

* Usa el tipo genérico `<T>` para poder aplicarse a cualquier clase de entidad.

#### 📂 FileManager

Componente encargado de **leer y escribir archivos JSON**.

* Utiliza la librería **Jackson (ObjectMapper)**.
* Obtiene la ruta base desde la configuración de Spring (`@Value("${bank.data.path}")`).
* Métodos principales:

  * `read()` → Lee archivos JSON y los convierte en listas de objetos Java.
* Rol en la arquitectura: Componente **@Component** reutilizable que aísla la lógica de acceso a archivos.

---

### 5. **Service**

#### ⚙️ BankService

Interfaz que define el **contrato de funcionalidades del banco**.
Métodos definidos:

* `createCustomer`
* `deposit`
* `withdraw`
* `transfer`
* `applyInterest`

El objetivo es que el Controller conozca **qué puede hacer el servicio**, sin saber **cómo lo hace**.

#### 🧠 BankServiceImpl

Implementa la interfaz `BankService` y contiene la **lógica de negocio real**.

* Anotación: `@Service` → Indica que es un servicio manejado por Spring.
* Implementa todos los métodos definidos en la interfaz.
* Por ahora, usa **listas en memoria (ArrayList)** para guardar datos temporalmente.

#### 📈 InterestStrategy

Interfaz que define cómo se calcula el interés (patrón **Strategy**).

* Método: `double calculateInterest(double balance)`
* Ventaja: Permite cambiar la forma de calcular intereses sin modificar el código principal.

---

### 6. **Strategies**

#### 🧮 SimpleRateStrategy

Estrategia básica que implementa la interfaz `InterestStrategy`.
Define un **único método** para calcular el interés con una tasa fija.

#### 💹 TieredRateStrategy

Estrategia avanzada con tasas escalonadas según el saldo:

* 1% → hasta $1000
* 2% → entre $1001 y $5000
* 3% → más de $5000
  Modelo más realista, premiando saldos mayores con mejores tasas.

---

### 7. **Util**

#### 🧰 JsonUtil

Clase **utilitaria auxiliar** para simplificar las tareas de conversión y manejo de archivos JSON.
Sirve como una **“caja de herramientas estática”** que otras clases pueden usar.

##### Métodos principales:

* `toJson(Object object)` → Convierte un objeto Java a texto JSON.
* `saveToFile(Object object, File file)` → Guarda un objeto en un archivo JSON.
* `readFromFile(File file, Class<T> clazz)` → Lee un archivo JSON y lo convierte a un objeto Java.
* `readListFromFile(File file, Class<T> clazz)` → Lee un archivo JSON y lo convierte en una lista de objetos Java.

---

## 🧩 Conclusión

La aplicación demuestra un **diseño modular, mantenible y escalable**, construido con buenas prácticas de ingeniería de software.
Se aplicaron:

* **POO (Herencia, Encapsulación, Polimorfismo)**
* **Patrón Strategy**
* **Arquitectura en Capas (Controller, Service, Model, Repository)**

---
<img width="1078" height="608" alt="image" src="https://github.com/user-attachments/assets/7d3c00f1-0a04-4774-a404-0eb7c63448e0" />
<img width="1080" height="606" alt="image" src="https://github.com/user-attachments/assets/750e74b3-6982-4be5-aa07-5af3b1c9ff66" />
<img width="1079" height="611" alt="image" src="https://github.com/user-attachments/assets/ded32b46-0b0f-4243-86c9-de9a80ddbfe5" />
<img width="1077" height="607" alt="image" src="https://github.com/user-attachments/assets/303e3545-2037-4b74-821b-95c942c206f1" />








