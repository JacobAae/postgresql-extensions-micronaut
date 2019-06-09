package postgresql.extensions.micronaut

import grails.gorm.services.Service

import javax.inject.Singleton

@Singleton
@Service(GormStorageObject)
interface GormStorageService {

    List<GormStorageObject> findAll()

    GormStorageObject save(GormStorageObject gormStorageObject)
}