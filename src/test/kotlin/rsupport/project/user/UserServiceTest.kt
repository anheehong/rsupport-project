package rsupport.project.user

import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import rsupport.project.user.dto.UserRequestDto

class UserServiceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        userService = UserService(userRepository)
    }

    @Test
    fun createUser() {
        // given
        val dto = UserRequestDto(username = "test", email = "test@naver.com")
        val savedUser = User(1L, dto.username, dto.email)

        every { userRepository.findByUsernameAndEmail(dto.username, dto.email) } returns null
        every { userRepository.save(any()) } returns savedUser

        // when
        val result = userService.createUser(dto)

        // then
        assertEquals("test", result.username)
        verify { userRepository.save(any()) }
    }

    @Test
    fun deleteUser() {
        every { userRepository.deleteById(1L) } just Runs

        userService.deleteUser(1L)

        verify { userRepository.deleteById(1L) }
    }

}
