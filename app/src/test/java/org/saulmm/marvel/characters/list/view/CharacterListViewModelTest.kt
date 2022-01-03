package org.saulmm.marvel.characters.list.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.stub
import org.saulmm.marvel.characters.data.CharacterRepository
import org.saulmm.marvel.characters.domain.models.CharacterPreview
import org.saulmm.marvel.characters.domain.models.Image
import org.saulmm.marvel.characters.list.view.CharacterListViewModel.CharactersViewState.*
import org.saulmm.marvel.utils.CoroutineTestRule

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterListViewModelTest {

    @Mock
    lateinit var characterRepository: CharacterRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val charactersList = listOf(
        CharacterPreview(1, "Iron Man", Image("iron_man", ".jpg")),
        CharacterPreview(2, "Captain America", Image("captain_america", ".jpg")),
        CharacterPreview(3, "Hulk", Image("hulk", ".jpg")),
    )


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }


    @Test
    fun `when an error happens in the repository, an error is emitted`() = coroutineTestRule.runBlockingTest {
        characterRepository.stub {
            onBlocking { characterRepository.characters(offset = any()) }.thenThrow(IllegalStateException::class.java)
        }

        val viewModel = CharacterListViewModel(characterRepository)

        assertThat(viewModel.onViewState.value).isInstanceOf(Failure::class.java)
    }

    @Test
    fun `when the repository dispatches characters, these are emitted as success`() = coroutineTestRule.runBlockingTest {
        characterRepository.stub {
            onBlocking { characterRepository.characters(offset = any()) }.thenReturn(charactersList)
        }

        val viewModel = CharacterListViewModel(characterRepository)

        val characters = (viewModel.onViewState.value as Success).characters
        assertThat(characters.size).isEqualTo(charactersList.size)
    }

    @Test
    fun `when the repository dispatches a failure, a retry action is saved`() = coroutineTestRule.runBlockingTest {
        characterRepository.stub {
            onBlocking { characterRepository.characters(offset = any()) }.thenThrow(IllegalStateException::class.java)
        }

        val viewModel = CharacterListViewModel(characterRepository)

        assertThat(viewModel.tryAgainAction).isNotNull()
    }

    @Test
    fun `when the repository dispatches a success, a retry action is null`() = coroutineTestRule.runBlockingTest {
        characterRepository.stub {
            onBlocking { characterRepository.characters(offset = any()) }.thenReturn(charactersList)
        }

        val viewModel = CharacterListViewModel(characterRepository)

        assertThat(viewModel.tryAgainAction).isNull()
    }

    @Test
    fun `when viewmodel initializes a loading event is emitted`() = coroutineTestRule.runBlockingTest {
        characterRepository.stub {
            onBlocking { characterRepository.characters(offset = any()) }.thenReturn(charactersList)
        }

        // We want to pause the dispatcher to listen to the event that is sent in the initialization block
        coroutineTestRule.testDispatcher.pauseDispatcher()

        val viewModel = CharacterListViewModel(characterRepository)

        viewModel.onViewState.test {
            assertThat(awaitItem()).isNull()
            assertThat(awaitItem()).isInstanceOf(Loading::class.java)
            cancelAndIgnoreRemainingEvents()
        }

        coroutineTestRule.testDispatcher.resumeDispatcher()
    }

    @Test
    fun `when the repository dispatches a success, loading and success are emitted`() = coroutineTestRule.runBlockingTest {
        characterRepository.stub {
            onBlocking { characterRepository.characters(offset = any()) }.thenReturn(charactersList)
        }

        // We want to pause the dispatcher to listen to the event that is sent in the initialization block
        coroutineTestRule.testDispatcher.pauseDispatcher()

        val viewModel = CharacterListViewModel(characterRepository)

        viewModel.onViewState.test {
            assertThat(awaitItem()).isNull()
            assertThat(awaitItem()).isInstanceOf(Loading::class.java)
            assertThat(awaitItem()).isInstanceOf(Success::class.java)
            cancel()
        }

        coroutineTestRule.testDispatcher.resumeDispatcher()
    }
}
