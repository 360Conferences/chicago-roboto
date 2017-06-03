package com.conferences.model

import com.conferences.ext.toHexString
import java.security.MessageDigest

data class Speaker(val id: String? = null,
                   val name: String? = null,
                   val email: String? = null,
                   // TODO this should be set in the datastore data
                   val avatar: String? = "https://firebasestorage.googleapis.com/v0/b/chicago-roboto.appspot.com/o/speakers%2F$id.jpg?alt=media",
                   val twitter: String? = null,
                   val github: String? = null,
                   val bio: String? = null) {

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
