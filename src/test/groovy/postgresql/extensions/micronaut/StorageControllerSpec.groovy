package postgresql.extensions.micronaut

import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import spock.lang.AutoCleanup
import spock.lang.Specification

import javax.inject.Inject


@MicronautTest
class StorageControllerSpec extends Specification {

    @Inject
    EmbeddedServer embeddedServer

    @AutoCleanup @Inject @Client("/")
    RxHttpClient client



    void "test index"() {
        given:
        HttpResponse response = client.toBlocking().exchange("/storage")

        expect:
        response.status == HttpStatus.OK
    }


}
