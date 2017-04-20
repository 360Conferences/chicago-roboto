package com.chicagoroboto.model

import com.chicagoroboto.ext.toHexString
import java.security.MessageDigest

class Speaker(val id: String? = null, val name: String? = null, val email: String? = null,
              val twitter: String? = null, val github: String? = null, val bio: String? = null) {


    val avatar: String?
        get() {
            return "https://firebasestorage.googleapis.com/v0/b/chicago-roboto.appspot.com/o/speakers%2F$id.jpg?alt=media"
        }

    private fun hash(): String {
        val md = MessageDigest.getInstance("MD5")
        return md.digest(email?.toByteArray(Charsets.UTF_8)).toHexString()
    }

    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Speaker

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int{
        return id?.hashCode() ?: 0
    }

}
