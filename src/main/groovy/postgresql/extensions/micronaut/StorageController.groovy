package postgresql.extensions.micronaut

import groovy.util.logging.Slf4j
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post

@Slf4j
@Controller("/storage")
class StorageController {

    GormStorageService gormStorageService
    GormExtensionsService gormExtensionsService

    StorageController(GormStorageService gormStorageService, GormExtensionsService gormExtensionsService) {
        this.gormStorageService = gormStorageService
        this.gormExtensionsService = gormExtensionsService
    }

    @Get("/")
    List<GormStorageObject> index() {
        log.info("List all")
        gormStorageService.findAll()
    }

    @Post("/")
    GormStorageObject save(GormStorageObject gormStorageObject) {
        log.info("Save: ${gormStorageObject}")
        gormStorageService.save(gormStorageObject)
    }

    @Get("/numbers")
    List<GormStorageObject> filterByNumbers(Integer[] numbers) {
        log.info("filterByNumbers: ${numbers}")
        gormExtensionsService.filterByHasFavoriteNumbers(numbers)
    }

    @Get("/attribute")
    List<GormStorageObject> filterByTestAttributes(String attribute) {
        log.info("filterByTestAttributes: ${attribute}")
        gormExtensionsService.filterByAttributes(attribute)
    }

    @Get("/history")
    List<GormStorageObject> filterByHistory(String date) {
        log.info("filterByHistory: ${date}")
        gormExtensionsService.filterByHistory(date)
    }

}
