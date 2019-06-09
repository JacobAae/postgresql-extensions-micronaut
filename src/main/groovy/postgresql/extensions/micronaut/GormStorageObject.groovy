package postgresql.extensions.micronaut

import grails.gorm.annotation.Entity
import net.kaleidos.hibernate.usertype.JsonbMapType
import net.kaleidos.hibernate.usertype.ArrayType
import net.kaleidos.hibernate.usertype.HstoreMapType

@Entity
class GormStorageObject {

    String job
    Map history
    Integer[] favoriteNumbers = []
    Map testAttributes

    static mapping = {
        history type: JsonbMapType
        favoriteNumbers type:ArrayType, params: [type: Integer]
        testAttributes type: HstoreMapType
    }
}
