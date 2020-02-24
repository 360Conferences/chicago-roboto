package com.chicagoroboto.data

import android.app.Application
import com.chicagoroboto.model.Library
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class AndroidLibraryProvider @Inject constructor(
    private val app: Application
) : LibraryProvider {

  private val libraries = listOf(
      Library("AOSP", "Android", "Apache 2", "https://android.googlesource.com"),
      Library("fragment-ktx", "Android", "Apache 2", "https://android.googlesource.com"),
      Library("lifecycle", "Android", "Apache 2", "https://android.googlesource.com"),
      Library("viewpager2", "Android", "Apache 2", "https://android.googlesource.com/platform/frameworks/support/+/master/v7/appcompat/"),
      Library("constraint-layout", "Android", "Android SDK", "https://developer.android.com/studio/terms.html"),
      Library("design", "Android", "Apache 2", "https://android.googlesource.com/platform/frameworks/support/+/master/design/"),
      Library("recyclerview-v7", "Android", "Apache 2", "https://android.googlesource.com/platform/frameworks/support/+/refs/heads/master/v7/recyclerview"),
      Library("Dagger 2", "Google", "Apache 2", "https://github.com/google/dagger"),
      Library("Kotlin", "JetBrains", "Apache 2", "http://kotlinlang.org/"),
      Library("CircularImageView", "Lopez Mikhael", "Apache 2", "https://github.com/lopspower/CircularImageView"),
      Library("Glide", "Sam Judd", "Apache 2", "https://github.com/bumptech/glide"),
      Library("multiline-collapsingtoolbar", "Opac App", "Apache 2", "https://github.com/opacapp/multiline-collapsingtoolbar"),
      Library("firebase-auth", "Firebase", "Apache 2", "https://github.com/bumptech/glide"),
      Library("firebase-database", "Firebase", "Apache 2", "https://github.com/bumptech/glide"),
      Library("firebase-storage", "Firebase", "Apache 2", "https://github.com/bumptech/glide"),
      Library("Insetter", "Chris Banes", "Apache 2", "https://github.com/chrisbanes/insetter")
  )

  override fun libraries(): Flow<List<Library>> = flowOf(libraries)
}
