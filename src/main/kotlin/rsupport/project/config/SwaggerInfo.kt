package rsupport.project.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "swagger.info")
data class SwaggerInfo(
    var title: String = "",
    var description: String = "",
    var version: String = "",
    var contact: Contact = Contact()
) {
    data class Contact(
        var name: String = "",
        var url: String = "",
        var email: String = ""
    )
}
