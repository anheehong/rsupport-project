package rsupport.project.notice.view

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class NoticeViewService(
    private val noticeViewRepository: NoticeViewRepository
){
    fun isUniqueView(request: HttpServletRequest, response: HttpServletResponse, noticeId: Long): Boolean {
        val cookieName = "notice_view_$noticeId"
        val cookies = request.cookies ?: return true

        val hasViewed = cookies.any { it.name == cookieName }
        if (hasViewed) return false

        val newCookie = Cookie(cookieName, "viewed").apply {
            maxAge = 60 * 60 // 1시간
            path = "/"
        }
        response.addCookie(newCookie)

        return true
    }

    fun hasRecentView(ip: String, noticeId: Long): Boolean {
        val threshold = LocalDateTime.now().minusHours(1)
        return noticeViewRepository.existsByNoticeIdAndViewerKeyAndCreatedAtAfter(noticeId, ip, threshold)
    }

    fun view(request: HttpServletRequest, response: HttpServletResponse, noticeId: Long): Boolean {
        val ip = request.remoteAddr
        val isNewCookieView = isUniqueView(request, response, noticeId)

        if (isNewCookieView && !hasRecentView(ip, noticeId)) {
            noticeViewRepository.save(NoticeView( noticeId = noticeId, viewerKey = ip))
            return true // 조회수 증가
        }
        return false // 이미 본 사용자
    }
}
