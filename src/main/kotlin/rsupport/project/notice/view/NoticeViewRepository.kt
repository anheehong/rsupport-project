package rsupport.project.notice.view

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface NoticeViewRepository : JpaRepository<NoticeView, Long>{

    fun existsByNoticeIdAndViewerKeyAndCreatedAtAfter(noticeId: Long, viewerKey: String, createdAt: LocalDateTime): Boolean
}
