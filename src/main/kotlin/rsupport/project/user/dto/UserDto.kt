package rsupport.project.user.dto

import io.swagger.v3.oas.annotations.media.Schema
import rsupport.project.base.CreateModifiedDtoBase
import rsupport.project.user.User

data class UserRequestDto(

    @Schema(description = "이름", example = "사용자1")
    var username: String = "",

    @Schema(description = "이메일", example = "사용자1@rsupport.com")
    var email: String = "",
)
val UserRequestDto.entity get() = User(
    username = username,
    email = email
)

data class UserDto(
    @Schema(description = "ID", example = "0L")
    val id: Long? = null,
    @Schema(description = "이름", example = "사용자1")
    var username: String,
    @Schema(description = "이메일", example = "사용자1@rsupport.com")
    val email: String = "",
): CreateModifiedDtoBase() {
    constructor(user: User): this(user.id, user.username, user.email) {
        createdAt = user.createdAt
        modifiedAt = user.modifiedAt
    }
}
val User.dto get() = UserDto(this)