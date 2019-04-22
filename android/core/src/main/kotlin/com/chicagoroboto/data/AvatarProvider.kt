package com.chicagoroboto.data

import com.chicagoroboto.model.Speaker

interface AvatarProvider {
  suspend fun getAvatarUri(speaker: Speaker): String
}