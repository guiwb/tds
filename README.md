# tds_guilherme

## Rodando localmente

Suba apenas o banco em um container:

```bash
docker run --name tds -e MARIADB_ROOT_PASSWORD=password -p 3306:3306 -d mariadb:latest
docker start tds
```

Depois rode a aplicação pela IDE ou com `./mvnw spring-boot:run`.

Swagger: http://localhost:8080/swagger-ui/index.html

## Rodando com Docker Compose

Sobe o banco e a aplicação juntos:

```bash
docker compose up -d --build
```

Para parar:

```bash
docker compose down
```

Para parar e apagar os dados do banco:

```bash
docker compose down -v
```

### Variáveis de ambiente

Todas possuem valor padrão, então são opcionais.

| Variável | Padrão | Uso |
| --- | --- | --- |
| `DB_PASSWORD` | `tds` | Senha do usuário `tds` do banco |
| `DB_ROOT_PASSWORD` | `password` | Senha do root do MariaDB |
| `JWT_SECRET` | `12345678123456781234567812345678` | Secret para assinar o token JWT |

A porta 3306 do banco não é exposta para o host (evita conflito com um MariaDB local). A aplicação acessa o banco pela rede interna do Compose.

### Healthcheck do banco

A aplicação só sobe depois que o banco está pronto, graças ao `depends_on` com `condition: service_healthy`.

O script `healthcheck.sh` não está no projeto: ele vem dentro da imagem oficial do MariaDB, em `/usr/local/bin/healthcheck.sh`.

* Na primeira inicialização do banco, a imagem cria um usuário `healthcheck` com senha aleatória e salva as credenciais em `/var/lib/mysql/.my-healthcheck.cnf`. O script usa esse usuário.
* `--connect` verifica se o servidor aceita conexões.
* `--innodb_initialized` verifica se o InnoDB terminou de inicializar (banco pronto para consultas).
* O Docker executa o script a cada 5s. Quando ele retorna 0, o container fica `healthy` e a aplicação é iniciada.

Para testar manualmente:

```bash
docker exec tds-mariadb healthcheck.sh --connect --innodb_initialized; echo $?
```

Se o volume `mariadb_data` foi criado por uma imagem antiga (sem o usuário `healthcheck`), o healthcheck falha. Nesse caso, rode `docker compose down -v` ou adicione `MARIADB_AUTO_UPGRADE: 1` ao serviço `mariadb`.

## Testes

Os testes de integração usam o profile `test` e o banco `tds_test` em `localhost:3306` (a senha vem da variável `MARIADB_SECRET`, padrão `password`). Com o MariaDB local rodando:

```bash
./mvnw clean test
```
