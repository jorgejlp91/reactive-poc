# Getting Started

### Pre-Requisites
   * Java 1.8
   * Docker Composer
   * Maven 3.5 or higher
   * Postman

## Run Applications

Just run the follow comands bellow
```bash
docker-compose -f docker-compose.yml up --build --force-recreate -d
```

```bash
mvn spring-boot:run
```
Then access http://localhost:8081 with **user** *admin* and **pass** *admin* to view mongo database.
Access http://localhost:8085 with email: teste@teste.com and pass *teste*. Then create a server connections with the following parameteres:
 * host postgres
 * user root
 * pass pass123

 Finally, open Postman Application and import the collection located in the source root path
