package com.kata3.kata3app

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.kata3.kata3app.data.DTO.ItemCreateRequest
import com.kata3.kata3app.data.DTO.ItemResponse
import com.kata3.kata3app.data.repositories.ItemRepository
import com.kata3.kata3app.ui.main.newItem.CreateItemResult
import com.kata3.kata3app.ui.main.newItem.NewItemViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NewItemViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: NewItemViewModel
    private lateinit var itemRepository: ItemRepository
    private lateinit var createResultObserver: Observer<CreateItemResult>

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        itemRepository = mock()
        viewModel = NewItemViewModel(itemRepository)
        createResultObserver = mock()
        viewModel.createResult.observeForever(createResultObserver)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel.createResult.removeObserver(createResultObserver)
    }

    @Test
    fun `createItem posts Success when repository returns item`() = runTest(testDispatcher) {
        // Given
        val token = "valid_token"
        val request = ItemCreateRequest("title", "PROJECT", "desc", false)
        val item = ItemResponse("1", "title", "PROJECT", "desc")
        whenever(itemRepository.createItem(token, request)).thenReturn(item)

        // When
        viewModel.createItem(token, request)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(createResultObserver).onChanged(CreateItemResult.Success(item))
    }

    @Test
    fun `createItem posts Error when repository returns null`() = runTest(testDispatcher) {
        // Given
        val token = "invalid_token"
        val request = ItemCreateRequest("title", "PROJECT", "desc", false)
        whenever(itemRepository.createItem(token, request)).thenReturn(null)

        // When
        viewModel.createItem(token, request)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(createResultObserver).onChanged(CreateItemResult.Error("Failed to create item."))
    }

    @Test
    fun `createItem posts Error when repository throws exception`() = runTest(testDispatcher) {
        // Given
        val token = "valid_token"
        val request = ItemCreateRequest("title", "PROJECT", "desc", false)
        whenever(itemRepository.createItem(token, request)).thenThrow(RuntimeException("Network error"))

        // When
        viewModel.createItem(token, request)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(createResultObserver).onChanged(CreateItemResult.Error("Error: Network error"))
    }

    /*@Test
    fun `createItem posts Error when token is null`() = runTest(testDispatcher) {
        // Given
        val token: String? = null
        val request = ItemCreateRequest("title", "PROJECT", "desc", false)

        // When
        viewModel.createItem(token, request)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(createResultObserver).onChanged(CreateItemResult.Error("No authentication token found."))
    }*/
}