package com.srikanthpuram.tmtsampleapp

import android.app.Application
import androidx.lifecycle.Observer
import com.srikanthpuram.tmtsampleapp.model.ApiResponse
import com.srikanthpuram.tmtsampleapp.model.Card
import com.srikanthpuram.tmtsampleapp.model.CardX
import com.srikanthpuram.tmtsampleapp.model.Page
import com.srikanthpuram.tmtsampleapp.repository.TmtRepository
import com.srikanthpuram.tmtsampleapp.ui.TmtViewModel
import com.srikanthpuram.tmtsampleapp.util.Resource
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify

class TmtViewModelTest {

    val repo = Mockito.mock(TmtRepository::class.java)
    val app = Mockito.mock(Application::class.java)
    val viewmodel = TmtViewModel(app, repo)
    @Mock
    lateinit var apiObserver: Observer<Resource<ApiResponse>>
    @Mock
    val cardz = CardX(null,null,null,null,"Text")
    @Mock
    val cardsList : List<Card> = listOf(Card(cardz, "text") , Card(cardz, "text")  )
    @Mock
    val response = ApiResponse(Page(cardsList))

    @Test
    fun testgetImageList() = runBlocking{
        viewmodel.getImageList()
        viewmodel.imageList.observeForever(apiObserver)
        verify(apiObserver).onChanged(Resource.Success(response))
        viewmodel.imageList.removeObserver(apiObserver)
    }

}

