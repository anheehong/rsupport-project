package rsupport.project.user

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import rsupport.project.user.dto.UserDto
import rsupport.project.user.dto.UserRequestDto
import rsupport.project.user.dto.dto
import rsupport.project.user.dto.entity

@Service
@Transactional
class UserService(@Autowired val userRepository: UserRepository){

    fun createUser(userRequestDto: UserRequestDto): UserDto {
        val user = userRepository.findByUsernameAndEmail(userRequestDto.username, userRequestDto.email)
        if(user != null){
            throw RuntimeException("해당 사용자(username: ${user.username}, email: ${user.email} )가 이미 존재합니다.")
        }
        return userRepository.save(userRequestDto.entity).dto
    }

    fun deleteUser(id: Long){
        userRepository.deleteById(id)
    }

    fun findById(id: Long): User {
        return userRepository.findById(id).orElseThrow { NoSuchElementException("해당 사용자(id: $id)를 찾을 수 없습니다.") }
    }

    fun getUserById(id: Long): UserDto? {
        return findById(id).dto
    }

    fun getUserByUsernameAndEmail(userRequestDto: UserRequestDto): User {
        return userRepository.findByUsernameAndEmail(userRequestDto.username, userRequestDto.email)
            ?: throw NoSuchElementException("해당 사용자(username: ${userRequestDto.username}, email: ${userRequestDto.email})를 찾을 수 없습니다.")
    }

}