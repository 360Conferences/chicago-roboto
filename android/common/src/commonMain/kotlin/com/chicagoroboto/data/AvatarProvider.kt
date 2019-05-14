package com.chicagoroboto.data

import com.chicagoroboto.model.Speaker

interface AvatarProvider {
  fun getAvatarUri(speaker: Speaker, callback: (String) -> Unit)
}