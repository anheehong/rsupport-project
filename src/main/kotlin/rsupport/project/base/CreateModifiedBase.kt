package rsupport.project.base

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
abstract class CreateModifiedBase: CreateBase() {

    @Column(name = "modified_at")
    var modifiedAt: LocalDateTime = LocalDateTime.now()

}