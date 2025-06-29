package rsupport.project.notice.view

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import rsupport.project.base.CreateBase

@Entity
@Table(name = "RS_NOTICE_VIEW")
@EntityListeners(AuditingEntityListener::class)
class NoticeView(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val noticeId: Long,
    val viewerKey: String
): CreateBase()
