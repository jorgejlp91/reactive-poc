package com.piccolomini.reactive.gateways.http

import com.piccolomini.reactive.domains.Account
import com.piccolomini.reactive.gateways.AccountGateway
import com.piccolomini.reactive.usecases.UpdateAccountUseCase
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import spock.lang.Specification

class AccountControllerSpec extends Specification {

    AccountGateway gateway = Mock(AccountGateway)
    UpdateAccountUseCase updateAccountUseCase = Mock(UpdateAccountUseCase)
    WebTestClient webTestClient
    AccountController accountController

    void setup() {

        accountController = new AccountController(gateway, updateAccountUseCase)

        this.webTestClient = WebTestClient.bindToController(accountController)
                .configureClient()
                .build()
    }


    def "get order status"() {
        given: "gets"

        when: "get product and availability data"
        def exchange = webTestClient
                .get()
                .uri("/accounts/userTest")
                .exchange()

        then:
        "the response is valid"
        exchange.expectStatus()
                .isOk()
                .expectBody()
                .jsonPath('$.user_id')
                .isEqualTo(1)
                .jsonPath('$.username')
                .isEqualTo("userTest")
                .jsonPath('$.password')
                .isEqualTo('pass123')
                .jsonPath('$.email')
                .isEqualTo('teste@gmail.com')

        and: "the gateway was called correctly"
        1 * gateway.findOne(_) >> {
            assert it[0] == 'userTest'
            return Mono.just(new Account(1, "userTest", "pass123", "teste@gmail.com"))
        }

    }


}
