package de.stefanbissell.dbmig

import java.time.Instant
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import mu.KotlinLogging
import org.springframework.data.repository.CrudRepository
import org.springframework.util.DigestUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

interface ImportantRepository : CrudRepository<ImportantObject, String>

@Suppress("JpaDataSourceORMInspection")
@Table(name = "important")
@Entity
class ImportantObject(
    @Id
    @GeneratedValue
    val id: Long = 0L,
    val time: Instant = Instant.now(),
    val payload: ByteArray
)

@RestController
class ImportantController(
    private val importantRepository: ImportantRepository
) {

    private val logger = KotlinLogging.logger {}

    @GetMapping("/all")
    fun getAll(): List<ImportantDto> {
        logger.info { "/all called" }

        return importantRepository
            .findAll()
            .sortedBy { it.id }
            .map {
                ImportantDto(it.id, it.time, DigestUtils.md5DigestAsHex(it.payload))
            }
    }
}

data class ImportantDto(
    val id: Long,
    val time: Instant,
    val payloadHash: String
)
