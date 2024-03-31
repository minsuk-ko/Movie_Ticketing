package com.example.movie_ticketing.domain

import jakarta.persistence.*


@Entity
class Member{

    var name : String = ""

    var email: String? = null
        private set

    var age: Int =0
        private set

    var password : String = ""
        private set

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    var id : Int = 0

    protected constructor() {
    }

    private constructor(name: String, email: String, age: Int, password: String) {
        this.name = name
        this.email = email
        this.age = age
        this.password = password
    }
}

