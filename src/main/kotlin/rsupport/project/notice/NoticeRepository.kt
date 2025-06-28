package rsupport.project.notice

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.query.Param

interface NoticeRepository : JpaRepository<Notice, Long>, QuerydslPredicateExecutor<Notice>{

    @Query("SELECT n FROM Notice n LEFT JOIN FETCH n.files WHERE n.id = :id")
    fun findByIdWithFiles(@Param("id") id: Long): Notice?
}
