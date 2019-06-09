package postgresql.extensions.micronaut

import groovy.util.logging.Slf4j
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.server.event.ServerStartupEvent
import net.kaleidos.hibernate.postgresql.criteria.ArrayCriterias
import net.kaleidos.hibernate.postgresql.criteria.HstoreCriterias
import net.kaleidos.hibernate.postgresql.criteria.JsonCriterias

import javax.inject.Singleton

@Slf4j
@Singleton
class GormExtensionInitializer implements ApplicationEventListener<ServerStartupEvent> {

    @Override
    void onApplicationEvent(ServerStartupEvent event) {
        new ArrayCriterias()
        new HstoreCriterias()
        new JsonCriterias()
    }

}
