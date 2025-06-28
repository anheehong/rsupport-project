package rsupport.project.notice

import rsupport.project.base.CreateModifiedBase
import jakarta.persistence.*
import rsupport.project.notice.file.NoticeFile
import rsupport.project.user.User
import java.time.LocalDate

@Entity
@Table(name = "RS_NOTICE")
class Notice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var title: String,
    @Column(length = 10000)
    var contents: String,
    @Column(name ="\"from\"", nullable = false)
    var from: LocalDate,
    @Column(name ="\"to\"", nullable = false)
    var to: LocalDate,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User
): CreateModifiedBase() {

    @Column(nullable = false)
    var views: Long = 0L

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "notice_id")
    var files: MutableList<NoticeFile> = mutableListOf()
}