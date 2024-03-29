package postgresql.extensions.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
class StorageControllerSpec extends Specification {

    @AutoCleanup
    @Shared
    RxHttpClient client

    @Shared
    DockerComposeContainer dockerComposeContainer =
            new DockerComposeContainer(new File("docker-compose.yml"))
                    .withExposedService("database", 5432)

    @Shared @AutoCleanup
    EmbeddedServer embeddedServer

    void setupSpec() {
        String host = dockerComposeContainer.getServiceHost("database", 5432)
        Integer port = dockerComposeContainer.getServicePort("database", 5432)

        embeddedServer = ApplicationContext
                .build()
                .properties(
                        'dataSource.url':"jdbc:postgresql://${host}:${port}/postgres",
                        'dataSource.username':"postgres",
                        'dataSource.password':"postgres",
                )
                .packages(
                        'postgresql.extensions.micronaut'
                )
                .run(EmbeddedServer)
        client = embeddedServer.applicationContext.createBean(RxHttpClient, embeddedServer.getURL())

        GormStorageObject object = new GormStorageObject(
                job: 'irrelevant',
                history: [date: '2019-05-26', activity:'Speakers dinner'],
                favoriteNumbers: [1,2,3],
                testAttributes: [happy:'path',funny:'yes']
        )

        HttpRequest request = HttpRequest.POST("/storage", object)
        HttpResponse response = client.toBlocking().exchange(request, Map)
    }

    void "test index"() {
        given:
        HttpResponse response = client.toBlocking().exchange("/storage")

        expect:
        response.status == HttpStatus.OK
    }

    void "test filterByNumbers"() {
        when:
        def result = client.toBlocking().retrieve(HttpRequest.GET('/storage/numbers?numbers=2&numbers=3'), List)

        println result

        then:
        result
        result.size() == 1
        result.first().favoriteNumbers == [1,2,3]
    }
}
