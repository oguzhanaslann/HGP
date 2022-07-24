package com.oguzhanaslann.hgp.di

import com.oguzhanaslann.hgp.ui.navigation.ComposeNavigator
import com.oguzhanaslann.hgp.ui.navigation.ComposeNavigatorImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideComposeNavigator(): ComposeNavigator {
        return ComposeNavigatorImp()
    }
}
