# Api y front end

## Proyecto base
Usamos como estructura de proyecto [Java Play React Seed](https://github.com/yohangz/java-play-react-seed). Acá aclara qué versiones usó para el proyecto (de Play, React y Create React App) y muchas cosas útiles más. Explica también el layout de directorios. Y qué agregó para que funcione React.

## Prerrequisitos
* [Node.js](https://nodejs.org/)
* [JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [SBT](http://www.scala-sbt.org/)

## Instrucciones
Correr estando en dir `web` el comando `sbt`. Esto creo que instala cosas. Después tirar `run` y debería levantar el servidor. Ir a localhost:9000.

## Requests
Están subidas en `web` unas request de postman.

#### Cȯmo levantar con Intellij
[Tutorial Intellij con Play](https://www.playframework.com/documentation/2.7.x/IDE#IntelliJ-IDEA)

###### Importar proyecto
- Instalar prerrequisitos
- Bajar el plugin scala de intellij (preferences->plugins)
- Open Project
- Import project
- seleccionar el proyecto (la carpeta contenedora, o sea, `web`, creo)
- Import project from external: SBT project
- Finish

###### Armar una run configuration
- Ir a menú `run`, elegir `edit configurations`
- `+` para crear una nueva config
- Elegir tipo `SBT Task`
- In the “tasks” input box, simply put “run”
- Apply changes and select OK.
- Now you can choose “Run” from the main Run menu and run your application
