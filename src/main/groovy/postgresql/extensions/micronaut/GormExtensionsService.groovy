package postgresql.extensions.micronaut

import grails.gorm.transactions.Transactional

import javax.inject.Singleton

@Singleton
@Transactional
class GormExtensionsService {

    List<GormStorageObject> filterByAttributes(String attribute) {
        GormStorageObject.withCriteria {
            pgHstoreContainsKey "testAttributes", attribute
        }
    }

    List<GormStorageObject> filterByHasFavoriteNumbers(Integer[] numbers) {
        GormStorageObject.withCriteria {
            pgArrayContains 'favoriteNumbers', numbers
        }
    }

    List<GormStorageObject> filterByHistory(String date) {
        GormStorageObject.withCriteria {
            pgJson 'history', '->>', "date", '=', date
        }
    }


}