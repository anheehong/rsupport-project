package rsupport.project.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>{
    fun findByUsernameAndEmail(username: String, email: String): User?
}
