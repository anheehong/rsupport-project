package rsupport.project.base

data class ApiResponse<T>(
    val message: String,
    val data: T? = null
) {
    companion object {
        @JvmStatic
        fun <T> success(data: T): ApiResponse<T> =
            ApiResponse("요청이 성공적으로 처리되었습니다.", data)

        @JvmStatic
        fun <T> success(message: String, data: T): ApiResponse<T> =
            ApiResponse(message, data)

        @JvmStatic
        fun <T> error(message: String): ApiResponse<T> =
            ApiResponse(message, null)
    }
}
