package rsupport.project.user

import rsupport.project.base.CreateModifiedBase
import jakarta.persistence.*

@Entity
@Table(name = "RS_USER")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false, unique = true)
    var username: String,
    @Column(nullable = false, unique = true)
    var email: String
): CreateModifiedBase()
 
