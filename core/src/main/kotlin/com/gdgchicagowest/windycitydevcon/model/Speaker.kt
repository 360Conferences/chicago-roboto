package com.gdgchicagowest.windycitydevcon.model

import com.gdgchicagowest.windycitydevcon.ext.toHexString
import java.security.MessageDigest

class Speaker(val name: String? = null, val email: String? = null, val bio: String? = null) {

    val avatar: String?
        get() {
            if (email == null) {
                return null
            } else {
                return "https://www.gravatar.com/avatar/${hash()}"
            }
        }

    private fun hash(): String {
        val md = MessageDigest.getInstance("MD5")
        return md.digest(email?.toByteArray(Charsets.UTF_8)).toHexString()
    }
}
