import com.example.movie_ticketing.domain.Member
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import java.awt.print.Pageable


interface MemberRepository : JpaRepository<Member?, Int?> {

    fun findAll(pageable: Pageable?): Page<Member?>?

    fun findByEmail(member: Member) : MutableList<Member>

}
