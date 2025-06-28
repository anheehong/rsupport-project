package rsupport.project.notice.view

import jakarta.persistence.*
import rsupport.project.base.CreateBase

@Entity
@Table(name = "RS_NOTICE_VIEW")
class NoticeView(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val noticeId: Long,
    val viewerKey: String
): CreateBase()
