package rsupport.project.config

import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.OpenAPI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig(
    private val swaggerInfo: SwaggerInfo
) {
    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI().info(
            Info()
                .title(swaggerInfo.title)
                .description(swaggerInfo.description)
                .version(swaggerInfo.version)
                .contact(
                    Contact()
                        .name(swaggerInfo.contact.name)
                        .url(swaggerInfo.contact.url)
                        .email(swaggerInfo.contact.email)
                )
        )
    }
}
