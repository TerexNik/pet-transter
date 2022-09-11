## How to build project

First build project with

```shell
mvn clean install 
```

Then build layered docker with

```shell
docker build -t transfer:${version} .
```

## Swagger

You need to run application to see swagger first

```shell
mvn spring-boot:run
```

Then you can access swagger by url http://localhost:8080/swagger-ui/index.html
You also can try execute requests there, but you need to run docker compose first

## Docker compose (for Postgresql)

Install docker on your machine and then just run

```shell
cd local-env
docker compose up -d
```