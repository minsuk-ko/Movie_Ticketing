package com.example.movie_ticketing

import org.mindrot.jbcrypt.BCrypt
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.full.memberProperties

/**
 * 오브젝트의 속성들을 맵으로 만드는 extension
 */
val Any.map: Map<String, Any?>
    get() = this::class.memberProperties.associate { prop ->
        prop.name to prop.getter.call(this)
    }

val String.bcryptHashed: String get() = BCrypt.hashpw(this, BCrypt.gensalt())

val formatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

val LocalDateTime.formatted: String get() = this.format(formatter)
