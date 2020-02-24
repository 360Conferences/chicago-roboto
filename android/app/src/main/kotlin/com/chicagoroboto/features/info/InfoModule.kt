package com.chicagoroboto.features.info

import com.chicagoroboto.data.AndroidLibraryProvider
import com.chicagoroboto.data.LibraryProvider
import com.chicagoroboto.navigator.AndroidWebNavigator
import com.chicagoroboto.navigator.WebNavigator
import dagger.Binds
import dagger.Module

@Module
abstract class InfoModule {
  @Binds abstract fun bindLibraryProvider(impl: AndroidLibraryProvider): LibraryProvider
  @Binds abstract fun bindWebNavigator(impl: AndroidWebNavigator): WebNavigator
}
