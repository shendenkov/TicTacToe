# Intro
### Version 1.0.1
This is the Demo game project. It includes 3 microservices:
- "ui-service" for Web User Interface, depends on session-service
- "session-service" for managing game sessions and game simulation, depends on engine-service
- "engine-service" for managing game state

You can use only Maven for unit-testing, building and running. For this, you need to use the following commands from section Maven. And 
then, just open the index.html file in the browser.

You can use Docker to pack services to images, run them in containers, open browser and use.

Or you can use Maven + Docker to automatically build, test, pack and run all services by one command, open browser and use.

# Maven

### Test
- Open console on project root
- Run tests for engine service:

`mvn -f engine test`

- Run tests for session service:

`mvn -f session test`

### Build
- Open console on project root
- Build engine service:

`mvn -f engine clean package -DskipTests`

- Build session service:

`mvn -f session clean package -DskipTests`

### Run
- Open first console on project root
- Run engine service:

`java -jar engine/target/engine-1.0.1.jar`

- Open second console on project root
- Run session service:

`java -jar session/target/session-1.0.1.jar --spring.profiles.active=local`

- Open **ui/index.html** file in you favorite browser
- Create and start game.

### Stop
Use `ctrl-c` to stop session service and then the same to stop engine service.

# Docker
### Pack
- Open console on project root
- Pack engine service image:

`docker build engine/. -t ttt/engine-service`

- Pack session service image:

`docker build session/. -t ttt/session-service`

- Pack ui service image:

`docker build ui/. -t ttt/ui-service`

### Run
- Create private network:

`docker network create ttt-network`

- Run engine service container:

`docker run -d --name engine-service --network ttt-network ttt/engine-service`

- Run session service container:

`docker run -d --name session-service --network ttt-network ttt/session-service`

- Run ui service container:

`docker run -d -p 8080:80 --name ui-service --network ttt-network ttt/ui-service`

- Open browser and type:

`http://localhost:8080`

### Stop
Use next commands to stop:

`docker stop ui-service`

`docker stop session-service`

`docker stop engine-service`

`docker network rm ttt-network`

If you need remove all stopped containers also, then use command:

`docker container prune`

# Maven + Docker
- Open console on project root
- Run command:

`docker-compose up -d`

- Open browser and type:

`http://localhost:8080/index.html`

### Stop
Use next commands to stop and remove containers:

`docker-compose down`