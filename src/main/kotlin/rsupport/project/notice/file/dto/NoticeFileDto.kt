package rsupport.project.notice.file.dto

import io.swagger.v3.oas.annotations.media.Schema
import rsupport.project.base.CreateDtoBase
import rsupport.project.notice.file.NoticeFile

data class NoticeFileDto(
    @Schema(description = "ID", example = "0L")
    val id: Long?,
    @Schema(description = "파일명", example = "newText.txt")
    var name: String,
    @Schema(description = "파일명", example = "newText.txt")
    val fileName: String?,
    @Schema(description = "파일경로", example = "upload/newText.txt")
    val filePath: String
): CreateDtoBase() {

    constructor(entity: NoticeFile) : this(
        id = entity.id,
        name = entity.name,
        fileName = entity.fileName,
        filePath = entity.filePath
    ){
        createdAt = entity.createdAt
    }
}
val NoticeFile.dto get() = NoticeFileDto(this)
