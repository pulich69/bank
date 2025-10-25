# Explicacion del codigo Bank
## Pasos para ejecutar el banco
1. Abre el código en Visual Studio Code.
2. Buscar el archivo que hace de "arrancador" (AppbankApplication.java).
3. Dale al botón de "Ejecutar"
4. La aplicación se inicia y te dice que está lista en el puerto 8080.
   
## Explicación del SRC
Appbank: este bloque de código sirve como un pequeño arrancador del Spring este define donde esta 
como se llama y le indica al spring boot que configure y ejecute.
<img width="577" height="256" alt="image" src="https://github.com/user-attachments/assets/8120d6b5-b22e-48ca-b0d1-91980bccf526" />

### Bankcontroller: 
este es el traductor entre peticiones externas (HTTP) y las peticiones de bankservice haciendo este que la aplicación responda correctamente con los datos solicitados ampliando estos métodos que son los que definen las operaciones financieras.
Explicación manejo de datos: Extrae datos de la URL (@PathVariable, @RequestParam) o del cuerpo de la petición (@RequestBody) y los pasa al BankService
@PostMapping("/customers") : Crear Cliente Maneja peticiones POST a /api/bank/customers. Recibiendo los datos del cliente en el cuerpo de la petición (@RequestBody Customer customer)
@GetMapping("/customers") : Listar Clientes Maneja peticiones GET a /api/bank/customers. Llama al servicio para obtener la lista.
@GetMapping("/customers/{customerId}") : Buscar Cliente Maneja peticiones GET a /api/bank/customers/123. El valor de {customerId} se extrae de la URL con @PathVariable. Devuelve 200 OK si lo encuentra o 404 NOT FOUND si no existe

### Exception: 
DomainException funciona para indicar fallas específicas de la lógica del banco permitiendo que la API REST devuelva mensajes de error claros y controlados.
### MODEL: 
Es la base de datos conceptual de la aplicación bancaria 
#### Account: 
Esta es la clase base que permite definir lo que todas las cuentas tienen en común aplicando el principio de herencia por ejmplo : 
deposit(Money amount): Lógica para depositar dinero y registrar la transacción.
withdraw(Money amount): Lógica básica de retiro (solo permite retirar si hay saldo suficiente) y registra la transacción.

#### CheckingAccount 
Esta es una clase concreta que añade lógica especifica de cuentas corrientes el atributo es overdraftLimit (límite de sobregiro). 
Y se usaron algunos @override como @Override public void applyInterest() para decir que las cuentas corrientes no ganan interés 
@Override public boolean withdraw(Money amount) que quiere decir que permite retiros incluso si el saldo es negativo simpre y cuando no exceda el overdraftLimit

#### Customer 
Esta es una clase que sirve para mostrar al cliente. Con atributos como id, name y email. Contiene solo los constructores y los métodos getters y setters estos son métodos de acceso para encapsular dichos datos 
#### Money 
Es un Objeto de Valor. Representa una cantidad monetaria específica tiene un constructor vacío y uno que requiere el amount y la currency
#### SavingsAccount 
es una entidad concreta es como una cuenta como la lógica espeficica de ahorro extends Account. Hereda todos los atributos comunes (id, owner, balance, transactions) y los métodos básicos (deposit, withdraw) de la clase base Account.
#### Trasaction  
Esta clase registra cada movimiento que ocurre en una cuenta funciona registrando la hora actual automática al ser creada, sirviendo como un registro inmutable del evento los atributos clave que la componen es type, amount, accountid y timestamp

### Repository
#### JsonRepositoty 
Esta es la clase de una interfaz que define el contrato de cualquier repositorio que maneje entidades definiendo las operaciones CRUD crear, leer, actualizar y eliminar básicas para el manejo de colecciones de datos. En otras palabras el json establece el que hacer sin especificar como hacerlo forzando a los repositorios concretos a seguir este estándar  
Indicando que dicha interfaz puede trabajar con cualquier tipo de entidad, por ejemplo: customer, account cuando se implemente (T) será reemplazado por la clase de la entidad 
##### FileManager
Esta clase es el componente que se encarga de la interacción directa con el sistema de archivos para leer y escribir los json Utilizando la librería Jackson (ObjectMapper) para convertir objetos Java en texto JSON así obtiene la ruta base para guardar los archivos de la configuración de Spring (@Value("${bank.data.path}")). 
read(): Lee el contenido de un archivo JSON (como customers.json), lo convierte en una lista de objetos Java (por ejemplo, List<Customer>), y maneja el caso de que el archivo no exista o esté vacío.
Rol en la Arquitectura: Se usa como un componente inyectable (@Component) que aísla la lógica de acceso a archivos del resto de la aplicación. Los repositorios específicos (que implementarían JsonRepository) lo utilizan para llevar a cabo sus operaciones CRUD.

### service 
#### BankService 
Es una Interfaz un contrato Define qué funcionalidades debe tener el banco, pero no cómo se implementan. Son solo una lista de todas las acciones que puedes pedirle al banco: createCustomer, deposit, withdraw, transfer, applyInterest. El propósito de esto es la definición de la acción de su implementación. Esto permite que el Controller solo sepa que puede llamar a deposit(), sin preocuparse de los detalles internos de cómo se hace BankService es la lista de servicios que el banco ofrece..

#### Bankservicelmpl
Esta clase es la que realmente hace el trabajo. Implementa el contrato BankService y contiene la lógica de negocio o los pasos necesarios para completar cada acción. La estructura de este @Service: Una anotación de Spring que le dice al sistema: "Esta es la clase que contiene la lógica de negocio; guárdala para que otros componentes (como el Controller) puedan usarla."
implements BankService: Declara que esta clase cumplirá con todas las promesas (métodos) definidas en la interfaz BankService.
Almacenamiento (Temporal): Por ahora, los datos (customers y accounts) se guardan en listas de Java (ArrayList). Esto significa que si la aplicación se reinicia, se pierden todos los datos. Más adelante, se usaría la capa de Repository (como el FileManager que ya vimos) para guardar los datos de forma permanente.

#### Intereststrategy
Es una Interfaz que define cómo se calcula el interés, siguiendo el patrón de diseño Strategy.
Solo tiene un método: calculateInterest(double balance).VentajaPermite cambiar la fórmula para calcular el interés (por ejemplo, de interés simple a compuesto) sin tener que modificar la clase BankServiceImpl. Simplemente se crea una nueva clase que implemente InterestStrategy.

### Strategies  
#### SimpleRateStrategies 
Es la interfaz que define un método único para calcular el interés: double calculateInterest(double balance). Cualquier clase dentro del paquete strategies debe implementar esta interfaz, lo que garantiza que, sin importar cuán compleja sea la fórmula, siempre sabrá recibir el saldo y devolver el monto del interés.
#### TieredRateStrategies
La tasa de interés aplicada depende del monto del saldo: * 1% para saldos hasta $1000. * 2% para saldos entre $1001 y $5000. * 3% para saldos mayores a $5000 esto refleja un modelo de negocio bancario más realista, donde los clientes con saldos más altos son recompensados con mejores tasas.
### Útil 
#### Jasonutil 
Este último archivo, JsonUtil.java, es una clase de utilidad auxiliar. Su función principal es simplificar las tareas de conversión y manejo de archivos JSON, encapsulando la complejidad de la librería Jackson que usa tu FileManager.
La explicación del jason es Piensa en JsonUtil como una caja de herramientas estática que cualquier parte de la aplicación puede usar para trabajar con JSON.
Explicación de métodos: 
toJson(Object object) : Convierte cualquier objeto Java en una cadena de texto JSON.
saveToFile(Object object, File file): Toma un objeto Java y lo escribe directamente en un archivo como JSON.
readFromFile(File file, Class<T> clazz): Lee un archivo JSON y lo convierte de nuevo a un objeto Java de la clase especificada (clazz).
readListFromFile(File file, Class<T> clazz): Lee un archivo JSON y lo convierte en una List de objetos Java de la clase especificada. Es crucial para leer colecciones de datos.


## La Arquitectura de la Aplicación Bancaria
se desarrolló una app Banca Digital construida con Spring Boot y un diseño de arquitectura por capas (Controlador, Servicio, Modelo, Repositorio), también aplicando importantes patrones de diseño y buenas prácticas. Aplicando un flujo del que el usuario envia una petición a un Controller pide al Service que realice la acción luego este manipula los objetos del Model (usando la lógica de herencia y las Strategies adecuadas) y, en una aplicación completa, le pediría al Repository que guarde los cambios en el disco luego el resultado regresa al Controller y se envía al usuario.
Ya Para finalizar este proyecto se usaron temas como la programación orientada a objetos(herencia) y patrones de diseño (Strategy) para construir una aplicación bancaria modular y mantenible.




