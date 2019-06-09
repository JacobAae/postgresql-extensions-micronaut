package postgresql.extensions.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
class GormExtensionServiceSpec extends Specification {

    @Shared
    DockerComposeContainer dockerComposeContainer =
            new DockerComposeContainer(new File("docker-compose.yml"))
                    .withExposedService("database", 5432)

    @Shared @AutoCleanup
    EmbeddedServer embeddedServer

    @Shared
    GormStorageService gormStorageService

    @Shared
    GormExtensionsService gormExtensionsService

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
        gormExtensionsService = embeddedServer.applicationContext.getBean(GormExtensionsService)
        gormStorageService = embeddedServer.applicationContext.getBean(GormStorageService)
    }


    void "test save and filteringByAttributes"() {
        setup:
        gormStorageService.save(new GormStorageObject(
                job: 'irrelevant',
                history: [date: '2019-05-26', activity:'Speakers dinner'],
                favoriteNumbers: [1,2,3],
                testAttributes: [happy:'path',funny:'yes']
        ))

        when:
        List<GormStorageObject> result = gormExtensionsService.filterByAttributes('happy')

        then:
        result.size() == 1
        result.first().testAttributes.happy == 'path'
    }


}
