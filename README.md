docker run --name tds -e MARIADB_ROOT_PASSWORD=password -p 3306:3306 -d mariadb:latest

docker start tds

swagger: http://localhost:8080/swagger-ui/index.html