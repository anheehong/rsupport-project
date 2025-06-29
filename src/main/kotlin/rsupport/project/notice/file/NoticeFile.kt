package rsupport.project.notice.file

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import rsupport.project.base.CreateBase

@Entity
@Table(name = "RS_NOTICE_FILE")
@EntityListeners(AuditingEntityListener::class)
class NoticeFile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
    var fileName: String?,
    var filePath: String
): CreateBase()
