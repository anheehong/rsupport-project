package rsupport.project.notice

import com.querydsl.core.BooleanBuilder
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import rsupport.project.notice.dto.*
import rsupport.project.notice.file.NoticeFile
import rsupport.project.notice.view.NoticeView
import rsupport.project.notice.view.NoticeViewRepository
import rsupport.project.notice.view.NoticeViewService
import rsupport.project.user.UserService
import rsupport.project.utils.FileStorageService
import java.time.LocalTime

@Service
class NoticeService(
    private val noticeRepository: NoticeRepository,
    private val noticeViewService: NoticeViewService,
    private val userService: UserService,
    private val fileStorageService: FileStorageService
) {

    fun createNoticeWithFiles(createDto: NoticeRequestCreateDto, files: List<MultipartFile> = mutableListOf()): NoticeResponseDto {
        val user = userService.findById(createDto.userId)
        val newNotice = createDto.toEntity(user)

        files.forEach { file ->
            val storedPath = fileStorageService.save(file)
            val noticeFile = NoticeFile(name = file.name, fileName = file.originalFilename, filePath = storedPath)
            newNotice.files.add(noticeFile)
        }
        return noticeRepository.save(newNotice).responseDto
    }

    fun getNotice(id: Long): NoticeResponseDto = findNoticeWithFiles(id).responseDto

    fun getNoticeView(id: Long, request: HttpServletRequest, response: HttpServletResponse): NoticeResponseDto {
        val notice = findNoticeWithFiles(id)
        if(noticeViewService.view(request, response, id)) {
            notice.views += 1
            return noticeRepository.save(notice).responseDto
        }
        return notice.responseDto
    }

    private fun findNotice(id: Long): Notice =
        noticeRepository.findById(id).orElseThrow { NoSuchElementException("해당 글(id: ${id})를 찾을 수 없습니다.") }

    private fun findNoticeWithFiles(id: Long): Notice =
        noticeRepository.findByIdWithFiles(id) ?: throw NoSuchElementException("해당 글(id: ${id})를 찾을 수 없습니다.")

    fun searchNotices(searchDto: NoticeSearchRequestDto, pageable: Pageable): Page<NoticeResponseListDto> {
        val qNotice = QNotice.notice
        val builder = BooleanBuilder()

        if (searchDto.searchKeyword.isNotBlank()) {
            when (searchDto.searchType) {
                NoticeSearchRequestDto.SearchType.TITLE -> builder.and(qNotice.title.containsIgnoreCase(searchDto.searchKeyword))
                NoticeSearchRequestDto.SearchType.TITLE_AND_CONTENT -> builder.and(
                    qNotice.title.containsIgnoreCase(searchDto.searchKeyword)
                        .or(qNotice.contents.containsIgnoreCase(searchDto.searchKeyword))
                )
            }
        }
        builder.and(qNotice.createdAt.goe(searchDto.fromDate.atStartOfDay()))
        builder.and(qNotice.createdAt.loe(searchDto.toDate.atTime(LocalTime.MAX)))

        return noticeRepository.findAll(builder, pageable).map { it.responseListDto }
    }

    fun updateNoticeWithFiles(id: Long, updateDto: NoticeRequestUpdateDto, files: List<MultipartFile> = mutableListOf()): NoticeResponseDto {
        val updateNotice = findNoticeWithFiles(id).updateBy(updateDto)

        updateDto.deleteFileIds.let { deleteIds ->
            updateNotice.files.removeIf { it.id in deleteIds }
        }

        files.forEach { file ->
            val storedPath = fileStorageService.save(file)
            val noticeFile = NoticeFile(name = file.name, fileName = file.name, filePath = storedPath)
            updateNotice.files.add(noticeFile)
        }

        return noticeRepository.save(updateNotice).responseDto
    }

    fun deleteNotice(id: Long) {
        noticeRepository.deleteById(id)
    }
}
