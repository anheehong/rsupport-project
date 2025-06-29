package rsupport.project.notice

import com.querydsl.core.BooleanBuilder
import io.mockk.*
import org.junit.jupiter.api.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.web.multipart.MultipartFile
import rsupport.project.notice.dto.*
import rsupport.project.notice.view.NoticeViewService
import rsupport.project.user.*
import rsupport.project.utils.FileStorageService
import java.time.LocalDate
import java.util.*

class NoticeServiceTest {

    private lateinit var noticeRepository: NoticeRepository
    private lateinit var noticeViewService: NoticeViewService
    private lateinit var userService: UserService
    private lateinit var fileStorageService: FileStorageService
    private lateinit var noticeService: NoticeService

    @BeforeEach
    fun setup() {
        noticeRepository = mockk()
        noticeViewService = mockk()
        userService = mockk()
        fileStorageService = mockk()
        noticeService = NoticeService(noticeRepository, noticeViewService, userService, fileStorageService)
    }

    @Test
    fun createNoticeWithFile() {
        val user = User(1L, "test", "test@test.com")
        val createDto = NoticeRequestCreateDto("title", "contents", from = LocalDate.now(), to = LocalDate.now(), 1L)
        val notice = createDto.toEntity(user)

        val file: MultipartFile = mockk()
        every { file.name } returns "file.txt"
        every { file.originalFilename } returns "file.txt"
        every { fileStorageService.save(file) } returns "/upload/file.txt"

        every { userService.findById(1L) } returns user
        every { noticeRepository.save(any()) } returns notice

        val result = noticeService.createNoticeWithFiles(createDto, listOf(file))

        Assertions.assertEquals(createDto.title, result.title)
        verify { fileStorageService.save(file) }
        verify { noticeRepository.save(any()) }
    }

    @Test
    fun getNoticeView() {
        val user = User(1L, "test", "test@test.com")
        val notice = Notice(1L, "title", "contents", from = LocalDate.now(), to = LocalDate.now(), user)

        every { noticeRepository.findByIdWithFiles(1L) } returns notice
        every { userService.findById(1L) } returns user
        every { noticeViewService.view(any(), any(), any()) } returns true
        every { noticeRepository.save(any()) } returns notice

        val response = noticeService.getNoticeView(1L, MockHttpServletRequest(), MockHttpServletResponse() )

        Assertions.assertNotNull(response)
        verify { noticeRepository.findByIdWithFiles(1L) }
    }

    @Test
    fun getNotice_Fail() {
        every { noticeRepository.findByIdWithFiles(99L) } returns null

        val exception = Assertions.assertThrows(NoSuchElementException::class.java) {
            noticeService.getNotice(99L)
        }

        Assertions.assertTrue(exception.message!!.contains("id: 99"))
    }

    @Test
    fun deleteNotice() {
        every { noticeRepository.deleteById(1L) } just Runs

        noticeService.deleteNotice(1L)

        verify { noticeRepository.deleteById(1L) }
    }

    @Test
    fun updateNoticeWithFile() {
        val user = User(1L, "test", "test@test.com")
        val notice = Notice(1L, "title", "contents", from = LocalDate.now(), to = LocalDate.now(), user)
        val updated = Notice(1L, "updated title", "updated contents", from = LocalDate.now(), to = LocalDate.now(), user)
        val updateDto = NoticeRequestUpdateDto(
            title = "updated title",
            contents = "updated contents",
            from = LocalDate.now(),
            to = LocalDate.now()
        ).apply {
            deleteFileIds = mutableListOf(10L)
        }

        val file: MultipartFile = mockk()
        every { file.name } returns "newFile.txt"
        every { file.originalFilename } returns "newFile.txt"
        every { fileStorageService.save(file) } returns "/upload/newFile.txt"

        every { noticeRepository.findByIdWithFiles(1L) } returns notice
        every { noticeRepository.save(any()) } returns updated

        val result = noticeService.updateNoticeWithFiles(1L, updateDto, listOf(file))

        Assertions.assertEquals("updated title", result.title)
        verify { fileStorageService.save(file) }
        verify { noticeRepository.save(any()) }
    }


    @Test
    fun searchNotices() {
        // given
        val user = User(1L, "test", "test@test.com")
        val notice = Notice(
            id = 1L,
            title = "title",
            contents = "content",
            from = LocalDate.now().minusDays(1),
            to = LocalDate.now().plusDays(1),
            user = user
        )

        val searchDto = NoticeSearchRequestDto(
            searchKeyword = "title",
            searchType = NoticeSearchRequestDto.SearchType.TITLE,
            fromDate = LocalDate.now().minusDays(7),
            toDate = LocalDate.now()
        )

        val pageable = PageRequest.of(0, 10)
        val page = PageImpl(listOf(notice), pageable, 1)

        every { noticeRepository.findAll(any<BooleanBuilder>(), any<Pageable>()) } returns page

        // when
        val result = noticeService.searchNotices(searchDto, pageable)

        // then
        Assertions.assertEquals(1, result.totalElements)
        Assertions.assertEquals("title", result.content.first().title)

        verify {
            noticeRepository.findAll(any<BooleanBuilder>(), pageable)
        }
    }

}
