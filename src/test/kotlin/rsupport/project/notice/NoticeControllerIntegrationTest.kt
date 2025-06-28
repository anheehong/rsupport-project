package rsupport.project.notice

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import rsupport.project.notice.dto.NoticeRequestCreateDto
import rsupport.project.notice.dto.NoticeRequestUpdateDto
import rsupport.project.notice.file.NoticeFile
import rsupport.project.user.UserRepository
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NoticeControllerIntegrationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var noticeRepository: NoticeRepository

    lateinit var savedNotice: Notice

    @BeforeEach
    fun setup() {
        val saveUser = userRepository.findById(1L).get()
        val notice = Notice(
            title = "title",
            contents = "contents",
            from = LocalDate.now().minusDays(1),
            to = LocalDate.now().plusDays(1),
            user = saveUser
        )
        savedNotice = noticeRepository.save(notice)
    }

    @Test
    fun createNoticeWithFiles () {
        val dto = NoticeRequestCreateDto(
            title = "title",
            contents = "contents",
            userId = 1L,
            from = LocalDate.now(),
            to = LocalDate.now().plusDays(1)
        )

        val jsonDto = objectMapper.writeValueAsString(dto)
        val jsonPart = MockMultipartFile("notice", "", MediaType.APPLICATION_JSON_VALUE, jsonDto.toByteArray())
        val file = MockMultipartFile("files", "test.txt", MediaType.TEXT_PLAIN_VALUE, "내용입니다".toByteArray())

        mockMvc.perform(multipart("/api/notice").file(jsonPart).file(file))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("게시글 생성 성공"))
    }

    @Test
    fun getNoticeView() {
        mockMvc.perform(get("/api/notice/{id}", savedNotice.id).param("userId", "1"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("게시글 조회 성공"))
    }

    @Test
    fun searchNotices() {
        val saveUser = userRepository.findById(1L).get()
        val notice = Notice(
            title = "title",
            contents = "contents",
            from = LocalDate.now().minusDays(1),
            to = LocalDate.now().plusDays(1),
            user = saveUser
        )
        notice.files.add(NoticeFile(name = "file.txt", fileName = "file.txt", filePath = "uploads/file.txt"))
        noticeRepository.save(notice)

        mockMvc.perform(get("/api/notice")
            .param("searchKeyword", "t")
            .param("searchType", "TITLE_AND_CONTENT")
            .param("fromDate", LocalDate.now().minusDays(7).toString())
            .param("toDate", LocalDate.now().toString()))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("게시글 검색 성공"))
    }

    @Test
    fun updateNotice() {
        val saveUser = userRepository.findById(1L).get()
        val notice = Notice(
            title = "title",
            contents = "contents",
            from = LocalDate.now().minusDays(1),
            to = LocalDate.now().plusDays(1),
            user = saveUser
        )
        notice.files.add(NoticeFile(name = "file1.txt", fileName = "file1.txt", filePath = "uploads/file1.txt"))
        notice.files.add(NoticeFile(name = "file2.txt", fileName = "file2.txt", filePath = "uploads/file2.txt"))

        val newNotice = noticeRepository.save(notice)

        val dto = NoticeRequestUpdateDto(
            title = "updated title",
            contents = "updated contents",
            from = LocalDate.now(),
            to = LocalDate.now().plusDays(2)
        )
        newNotice.files.let {
            dto.deleteFileIds.add(it[0].id!!)
        }
        
        val jsonDto = objectMapper.writeValueAsString(dto)
        val jsonPart = MockMultipartFile("notice", "notice.json", MediaType.APPLICATION_JSON_VALUE, jsonDto.toByteArray())
        val file = MockMultipartFile("files", "newFile.txt", MediaType.TEXT_PLAIN_VALUE, "새로운 내용".toByteArray())
        val file1 = MockMultipartFile("files", "newFile1.txt", MediaType.TEXT_PLAIN_VALUE, "새로운 내용1".toByteArray())

        mockMvc.perform(multipart("/api/notice/{id}", newNotice.id)
            .file(jsonPart)
            .file(file)
            .file(file1)
            .with { it.method = "PUT"; it })
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("게시글 수정 성공"))
    }

    @Test
    fun deleteNotice() {
        mockMvc.perform(delete("/api/notice/{id}", savedNotice.id))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("게시글 삭제 성공"))
    }
} 
