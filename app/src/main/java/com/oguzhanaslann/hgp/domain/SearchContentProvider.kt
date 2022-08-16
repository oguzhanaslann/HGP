package com.oguzhanaslann.hgp.domain

import androidx.lifecycle.ViewModel
import com.oguzhanaslann.commonui.base.SearchContent
import com.oguzhanaslann.hgp.ui.main.SearchType

interface SearchContentProvider {
    fun getSearchContentBy(searchType: SearchType): SearchContent<ViewModel>
}
