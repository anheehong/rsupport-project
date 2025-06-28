package rsupport.project.utils

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

@Service
class FileStorageService {
    private val uploadDir: String = "uploads"

    fun save(file: MultipartFile): String {
        val filename = "${UUID.randomUUID()}_${file.originalFilename}"
        val path: Path = Paths.get(uploadDir, filename)

        return try {
            Files.createDirectories(path.parent) // 디렉터리 없으면 생성
            file.transferTo(path)                // 파일 저장
            path.toString()
        } catch (e: IOException) {
            throw RuntimeException("파일 저장 중 오류가 발생했습니다: ${e.message}", e)
        } catch (e: Exception) {
            throw RuntimeException("알 수 없는 파일 저장 오류: ${e.message}", e)
        }
    }

}
