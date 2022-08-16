package com.oguzhanaslann.hgp.di

import androidx.lifecycle.ViewModel
import com.oguzhanaslann.camera.CameraSearchContent
import com.oguzhanaslann.commonui.base.SearchContent
import com.oguzhanaslann.hgp.domain.SearchContentProvider
import com.oguzhanaslann.hgp.ui.main.SearchType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideSearchContentProvider(
        @ApplicationContext context: android.content.Context
    ) : SearchContentProvider = object : SearchContentProvider {
        override fun getSearchContentBy(searchType: SearchType): SearchContent<ViewModel> {
            val cameraSearchContent = CameraSearchContent(context)
            return when(searchType) {
                SearchType.TextSearch -> cameraSearchContent as SearchContent<ViewModel>
                SearchType.VoiceSearch -> cameraSearchContent as SearchContent<ViewModel>
                SearchType.ImageSearch -> cameraSearchContent as SearchContent<ViewModel>
                SearchType.QRScanSearch -> cameraSearchContent as SearchContent<ViewModel>
            }
        }
    }
}
