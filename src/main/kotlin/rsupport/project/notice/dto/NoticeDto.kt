package rsupport.project.notice.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import rsupport.project.base.CreateDtoBase
import rsupport.project.base.CreateModifiedDtoBase
import rsupport.project.notice.Notice
import rsupport.project.notice.file.dto.NoticeFileDto
import rsupport.project.notice.file.dto.dto
import rsupport.project.user.User
import rsupport.project.user.dto.UserDto
import rsupport.project.user.dto.dto
import java.time.LocalDate

/**
 * 생성시
 */
@Schema(description = "공지사항 생성시 요청 DTO")
data class NoticeRequestCreateDto(
    @Schema(description = "제목", example = "공지사항 1")
    var title: String,
    @Schema(description = "내용", example = "공지사항 1 내용입니다.")
    var contents: String,
    @Schema(description = "공지시작일", example = "2025-06-01")
    var from: LocalDate,
    @Schema(description = "공지종료일", example = "2025-06-30")
    var to: LocalDate,
    @Schema(description = "사용자 ID", example = "1L")
    var userId: Long
)
fun NoticeRequestCreateDto.toEntity(user: User): Notice = Notice(
    title = this.title,
    contents = this.contents,
    from = this.from,
    to = this.to,
    user = user
)

/**
 * 업데이트시
 */
@Schema(description = "공지사항 수정시 요청 DTO")
data class NoticeRequestUpdateDto(
    @Schema(description = "제목", example = "공지사항 1")
    var title: String,
    @Schema(description = "내용", example = "공지사항 1 내용입니다.")
    var contents: String,
    @Schema(description = "공지시작일", example = "2025-06-01")
    var from: LocalDate,
    @Schema(description = "공지종료일", example = "2025-06-30")
    var to: LocalDate
){
    @Schema(description = "삭제할 파일 ID", example = "[1, 2]")
    var deleteFileIds = mutableListOf<Long>()
}
fun Notice.updateBy(dto: NoticeRequestUpdateDto): Notice = this.apply {
    this.title = dto.title
    this.contents = dto.contents
    this.from = dto.from
    this.to = dto.to
}

/**
 * 반환시 상세
 */
@Schema(description = "공지사항 상세 응답 DTO")
data class NoticeResponseDto(
    @Schema(description = "ID", example = "0L")
    var id: Long? = null,
    @Schema(description = "제목", example = "공지사항 1")
    var title: String,
    @Schema(description = "내용", example = "공지사항 1 내용입니다.")
    var contents: String,
    @Schema(description = "공지시작일", example = "2025-06-01")
    var from: LocalDate,
    @Schema(description = "공지종료일", example = "2025-06-30")
    var to: LocalDate,
    @Schema(description = "작성자", example = "작성자 객체")
    var user: UserDto
): CreateModifiedDtoBase(){

    @Schema(description = "첨부파일", example = "첨부파일 객체")
    var files: MutableList<NoticeFileDto> = mutableListOf()
    @Schema(description = "조회수", example = "0")
    var views: Long = 0L

    constructor(notice:Notice): this(
        notice.id,
        notice.title,
        notice.contents,
        notice.from,
        notice.to,
        notice.user.dto
    ) {
        files = notice.files.map { it.dto }.toMutableList()
        views = notice.views
        createdAt = notice.createdAt
        modifiedAt = notice.modifiedAt
    }
}
val Notice.responseDto get() = NoticeResponseDto(this)

/**
 * 반환시 리스트
 */
@Schema(description = "공지사항 리스트 응답 DTO")
data class NoticeResponseListDto(
    @Schema(description = "ID", example = "0L")
    var id: Long? = null,
    @Schema(description = "제목", example = "공지사항 1")
    var title: String,
    @Schema(description = "작성자명", example = "사용자 1")
    var username: String
): CreateDtoBase(){

    @Schema(description = "첨부파일 존재 여부", example = "TRUE")
    var filesExist: Boolean = false
    @Schema(description = "조회수", example = "0")
    var views: Long = 0L

    constructor(notice:Notice): this(
        notice.id,
        notice.title,
        notice.user.username
    ) {
        filesExist = notice.files.size > 0
        views = notice.views
        createdAt = notice.createdAt
    }
}
val Notice.responseListDto get() = NoticeResponseListDto(this)

/**
 * 검색시
 */
@Schema(description = "공지사항 검색 요청 DTO")
data class NoticeSearchRequestDto(
    @Schema(description = "검색어", example = "공지")
    var searchKeyword: String = "",
    @Schema(description = "검색 구분", example = "TITLE_AND_CONTENT, TITLE")
    var searchType: SearchType = SearchType.TITLE_AND_CONTENT,
    @Schema(description = "검색 시작일", example = "2025-06-01")
    var fromDate: LocalDate,
    @Schema(description = "검색 종료일", example = "2025-06-30")
    var toDate: LocalDate
){

    @Schema(description = "검색 구분")
    enum class SearchType {
        @Schema(description = "재목 + 내용")
        TITLE_AND_CONTENT,
        @Schema(description = "제목")
        TITLE
    }
}
