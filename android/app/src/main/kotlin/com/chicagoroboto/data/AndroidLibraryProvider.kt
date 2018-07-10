package com.chicagoroboto.data

import android.content.Context
import com.chicagoroboto.model.Library

class AndroidLibraryProvider(private val context: Context) : LibraryProvider {

    val libraries = listOf(
            Library("AOSP", "Android", "Apache 2", "https://android.googlesource.com"),
            Library("appcompat-v7", "Android", "Apache 2", "https://android.googlesource.com/platform/frameworks/support/+/master/v7/appcompat/"),
            Library("constraint-layout", "Android", "Android SDK", "https://developer.android.com/studio/terms.html"),
            Library("design", "Android", "Apache 2", "https://android.googlesource.com/platform/frameworks/support/+/master/design/"),
            Library("recyclerview-v7", "Android", "Apache 2", "https://android.googlesource.com/platform/frameworks/support/+/refs/heads/master/v7/recyclerview"),
            Library("Dagger 2", "Google", "Apache 2", "https://github.com/google/dagger"),
//            Library("Google Play Services", "Google", "", ""),
            Library("Kotlin", "JetBrains", "Apache 2", "http://kotlinlang.org/"),
            Library("CircularImageView", "Lopez Mikhael", "Apache 2", "https://github.com/lopspower/CircularImageView"),
            Library("Glide", "Sam Judd", "Apache 2", "https://github.com/bumptech/glide"),
            Library("multiline-collapsingtoolbar", "Opac App", "Apache 2", "https://github.com/opacapp/multiline-collapsingtoolbar")
    )

    override fun libraries(onComplete: (List<Library>) -> Unit) {
        onComplete(libraries)
    }
}