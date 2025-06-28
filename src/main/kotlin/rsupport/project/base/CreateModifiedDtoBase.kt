package rsupport.project.base

import java.time.LocalDateTime

open class CreateModifiedDtoBase(
    var modifiedAt: LocalDateTime? = null
): CreateDtoBase()