# Getting Started

## Purpose of this Project

This project was created for stude the concepts of reactive programming

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

# Reactive vs Non-Reactive

According to tests performed to see the benefits of a reactive application,
there is an advantage in using this approach when there are many concurrent accesses as follows:

#### Non Reactive - Few Requests

| Info                 | Value                |
|----------------------|----------------------|
| Requests per second: | 19.64 [#/sec] (mean) |
| Time per request:    | 509.198 [ms] (mean)  |
| Number of Requests:  | 100                  |
| Concurrency          | 10                   |


Percentage of the requests served within a certain time (ms)

| Percentage | ms                    |
|------------|-----------------------|
| 50%        | 508                   |
| 66%        | 509                   |
| 75%        | 510                   |
| 80%        | 510                   |
| 90%        | 512                   |
| 95%        | 512                   |
| 98%        | 515                   |
| 99%        | 517                   |
| 100%       | 517 (longest request) |

#### Reactive - Few Requests

| Info                 | Value                |
|----------------------|----------------------|
| Requests per second: | 19.63 [#/sec] (mean) |
| Time per request:    | 509.465 [ms] (mean)  |
| Number of Requests:  | 100                  |
| Concurrency Level:   | 10                   |

Percentage of the requests served within a certain time (ms)

| Percentage | ms                    |
|------------|-----------------------|
| 50%        | 506                   |
| 66%        | 507                   |
| 75%        | 508                   |
| 80%        | 509                   |
| 90%        | 517                   |
| 95%        | 522                   |
| 98%        | 526                   |
| 99%        | 528                   |
| 100%       | 528 (longest request) |


#### Non Reactive - Many Requests

| Info                  | Value                 |
|-----------------------|-----------------------|
| Requests per second:  | 387.53 [#/sec] (mean) |
| Time per request:     | 2580.473 [ms] (mean)  |
| Number of Requests:   | 10.000                |
| Concurrency Level:    | 1.000                 |

Percentage of the requests served within a certain time (ms)

| Percentage | ms                     |
|------------|------------------------|
| 50%        | 2521                   |
| 66%        | 2531                   |
| 75%        | 2540                   |
| 80%        | 2548                   |
| 90%        | 2576                   |
| 95%        | 2607                   |
| 98%        | 2661                   |
| 99%        | 2772                   |
| 100%       | 2779 (longest request) |


#### Reactive - Many Requests

| Info                 | Value                  |
|----------------------|------------------------|
| Requests per second: | 1401.69 [#/sec] (mean) |
| Time per request:    | 713.423 [ms] (mean)    |
| Numer of Requests    | 10.000                 |
| Concurrency Level:   | 1.000                  |

Percentage of the requests served within a certain time (ms)

| Percentage | ms                     |
|------------|------------------------|
| 50%        | 577                    |
| 66%        | 611                    |
| 75%        | 628                    |
| 80%        | 665                    |
| 90%        | 817                    |
| 95%        | 1591                   |
| 98%        | 1614                   |
| 99%        | 1641                   |
| 100%       | 1680 (longest request) |



### Executing the tests above

You need to install Apache Bench (or AB)

Then up the non reactive application as bellow:
```bash
cd non-reactive-poc-test/
mvn spring-boot:run
```

Then execute the ab command for non reactive:
```bash
ab -n 100 -c 10 http://localhost:8082/orders/1
```
Change port to 8080 for execute reactive application.

## Another benefits

Another benefit of the reactive is when it is necessary to perform processing that can be parallel like merging values of the database with values searched in some underlying api.
The endpoint http://localhost:8082/orderStatus show this benefits.
Test the endpoint with more then one single data in database and you will see the result.
