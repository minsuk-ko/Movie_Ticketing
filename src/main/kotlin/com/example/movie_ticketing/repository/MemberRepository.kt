package com.example.movie_ticketing.repository

import com.example.movie_ticketing.domain.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Repository
interface MemberRepository : JpaRepository<Member?, Int?> {
    // todo
//    fun findAll(pageable: Pageable): Page<Member?>

    //fun findByEmail(email: String) : MutableList<Member>
    // email이 유니크 속성 갖고있다면 Optional로 반환하는 것이 적합
    // MutableList로 반환할 경우는 이메일이 고유하지 않을 경우(unique 없을경우)
    // 또한 CustomUserDetailService에서 email/password를 접근해야하는데
    //Mutable이라 List로 반환되기에 직접 접근을 못함
    fun findByEmail(email: String) : Optional<Member>

    fun findByName(member: Member)

    fun findByNameAndEmail(name: String, email: String)

    fun existsByEmail(email: String); //email 존재여부(Db에 있으면 참)

    @Modifying
    @Transactional
    @Query("update Member set password=:password where id=:id")
    fun updatePassword(id: Int, password : String)

}
