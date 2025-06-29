package rsupport.project.base

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class CreateModifiedBase: CreateBase() {
    @LastModifiedDate
    @Column(name = "modified_at")
    var modifiedAt: LocalDateTime? = null
}