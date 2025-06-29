package rsupport.project.user

import rsupport.project.base.CreateModifiedBase
import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@Table(name = "RS_USER")
@EntityListeners(AuditingEntityListener::class)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false, unique = true)
    var username: String,
    @Column(nullable = false, unique = true)
    var email: String
): CreateModifiedBase()
 
