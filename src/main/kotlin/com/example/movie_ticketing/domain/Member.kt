package com.example.movie_ticketing.domain

import jakarta.persistence.*


@Entity
class Member{

    var name : String = ""

    @Column(unique = true)
    var email: String? = null

    var age: Int? = 0

    var password : String = ""

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    var id : Int = 0

    constructor() {
    }

    private constructor(name: String, email: String, age: Int, password: String) {
        this.name = name
        this.email = email
        this.age = age
        this.password = password
    }

    override fun toString(): String {
        return "Member(name='$name', email=$email, age=$age, password='$password', id=$id)"
    }

}

