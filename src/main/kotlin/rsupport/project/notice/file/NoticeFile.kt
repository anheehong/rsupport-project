package rsupport.project.notice.file

import jakarta.persistence.*
import rsupport.project.base.CreateBase

@Entity
@Table(name = "RS_NOTICE_FILE")
class NoticeFile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
    var fileName: String?,
    var filePath: String
): CreateBase()
