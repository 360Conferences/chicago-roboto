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

      Library("RecyclerView", "Android", "Apache 2", "https://android.googlesource.com/platform/frameworks/support/+/androidx-master-dev/recyclerview/"),
      Library("AppCompat", "Android", "Apache 2", "https://android.googlesource.com/platform/frameworks/support/+/androidx-master-dev/appcompat/"),
      Library("ConstraintLayout", "Android", "Apache 2", "https://android.googlesource.com/platform/frameworks/opt/sherpa/+/refs/heads/mirror-goog-studio-master-dev"),
      Library("Fragment", "Android", "Apache 2", "https://android.googlesource.com/platform/frameworks/support/+/androidx-master-dev/fragment/"),
      Library("Lifecycle", "Android", "Apache 2", "https://android.googlesource.com/platform/frameworks/support/+/androidx-master-dev/lifecycle/"),
      Library("ViewPager2", "Android", "Apache 2", "https://android.googlesource.com/platform/frameworks/support/+/androidx-master-dev/viewpager2/"),

      Library("Maps", "Google", "Android SDK", "https://developers.google.com/maps/documentation/android-sdk/intro"),

      Library("Auth", "Firebase", "Android SDK", "https://firebase.google.com/docs/auth?authuser=0"),
      Library("Database", "Firebase", "Apache 2", "https://github.com/firebase/firebase-android-sdk/tree/master/firebase-database"),
      Library("Storage", "Firebase", "Apache 2", "https://github.com/firebase/firebase-android-sdk/tree/master/firebase-storage"),

      Library("Glide", "Sam Judd", "BSD", "https://github.com/bumptech/glide"),

      Library("Insetter", "Chris Banes", "Apache 2", "https://github.com/chrisbanes/insetter"),


      Library("Kotlin", "JetBrains", "Apache 2", "https://kotlinlang.org"),
      Library("Coroutines", "JetBrains", "Apache 2", "https://github.com/Kotlin/kotlinx.coroutines"),

      Library("Mutliline CollapsingToolbar", "Opac App", "Apache 2", "https://github.com/opacapp/multiline-collapsingtoolbar"),

      Library("Timber", "Jake Wharton", "Apache 2", "https://github.com/JakeWharton/timber"),

      Library("Material Components", "Google", "Apache 2", "https://github.com/material-components/material-components-android"),
      Library("Dagger", "Google", "Apache 2", "https://github.com/google/dagger")
  )
      .sortedBy { it.name }
      .sortedBy { it.owner }

  override fun libraries(): Flow<List<Library>> = flowOf(libraries)
}
