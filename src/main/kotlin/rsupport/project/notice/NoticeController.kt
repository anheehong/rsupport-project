package rsupport.project.notice

import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import rsupport.project.base.ApiResponse
import rsupport.project.notice.dto.*

@RestController
@RequestMapping("/api/notice")
@Validated
class NoticeController(
    private val noticeService: NoticeService
) {

    @Operation(summary = "게시글 생성 WITH 파일", description = "새 게시글을 생성합니다. 파일 첨부")
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createNoticeWithFiles(
        @RequestPart("notice") @Valid dto: NoticeRequestCreateDto,
        @RequestPart("files", required = false) files: List<MultipartFile> = mutableListOf()
    ): ResponseEntity<ApiResponse<NoticeResponseDto>>
        = ResponseEntity.ok(ApiResponse.success("게시글 생성 성공", noticeService.createNoticeWithFiles(dto, files)))

    @Operation(summary = "게시글 조회", description = "ID로 게시글을 조회합니다. 사용자가 해당 글을 읽었는지 확인하고 읽은 기록이 없다면 조회수를 올립니다. 쿠키와 IP 중복 불가")
    @GetMapping("/{id}")
    fun getNoticeView(
        @PathVariable id: Long,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<ApiResponse<NoticeResponseDto>> =
        ResponseEntity.ok(ApiResponse.success("게시글 조회 성공", noticeService.getNoticeView(id, request, response)))

    @Operation(summary = "게시글 검색", description = "게시글을 검색합니다.")
    @GetMapping
    fun searchNotices(
        searchDto: NoticeSearchRequestDto,
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC)
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<NoticeResponseListDto>>>
        = ResponseEntity.ok(ApiResponse.success("게시글 검색 성공", noticeService.searchNotices(searchDto, pageable)))

    @Operation(summary = "게시글 수정 WITH 파일", description = "ID로 게시글을 수정합니다. 파일 첨부")
    @PutMapping("/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateNotice(
        @PathVariable id: Long,
        @RequestPart("notice") dto: NoticeRequestUpdateDto,
        @RequestPart("files", required = false) files: List<MultipartFile> = mutableListOf()
    ): ResponseEntity<ApiResponse<NoticeResponseDto>>
        = ResponseEntity.ok(ApiResponse.success("게시글 수정 성공", noticeService.updateNoticeWithFiles(id, dto, files)))

    @Operation(summary = "게시글 삭제", description = "ID로 게시글을 삭제합니다.")
    @DeleteMapping("/{id}")
    fun deleteNotice(@PathVariable id: Long): ResponseEntity<ApiResponse<Void?>> {
        noticeService.deleteNotice(id)
        return ResponseEntity.ok(ApiResponse.success("게시글 삭제 성공", null))
    }
}
