package com.example.movie_ticketing.domain

import jakarta.persistence.*

@Entity
class Member {
    var email: String? = null
        private set

    var age : Int? = null
        private set

    var password: String? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    val id: Long = 0L

    protected constructor() {
        // no-args constructor required by JPA spec
        // this one is protected since it should not be used directly
    }

    constructor(email : String?, password : String?, age : Int?){
        this.email = email
        this.password = password
        this.age = age
    }
}
