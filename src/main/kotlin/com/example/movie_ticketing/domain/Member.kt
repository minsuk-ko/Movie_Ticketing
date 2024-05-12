package com.example.movie_ticketing.domain

import jakarta.persistence.*


@Entity
class Member{

    lateinit var name : String

    @Column(unique = true)
    var email: String? = null

    var age: Int? = 0

    lateinit var password : String

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Int = 0

    var role : String = "ROLE_USER" // 기본권한은 유저

    override fun toString(): String {
        return "Member(name='$name', email=$email, age=$age, password='$password', id=$id, role='$role')"
    }
}

