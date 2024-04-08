package com.example.movie_ticketing.domain

import jakarta.persistence.*


@Entity
class Member{

    var name : String = ""

    @Column(unique = true)
    var email: String? = null

    var age: Int? = null

    var password : String = ""

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    var id : Int = 0

    constructor() {
    }

    private constructor(name: String, email: String, age: Int, password: String) {
        this.name = name
        this.email = email
        this.age = age
        this.password = password
    }
}

