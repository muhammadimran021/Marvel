package org.saulmm.marvel.characters.details.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.stub
import org.saulmm.marvel.characters.data.CharacterRepository
import org.saulmm.marvel.characters.data.models.Character
import org.saulmm.marvel.characters.data.models.CharacterPreview
import org.saulmm.marvel.characters.data.models.Image
import org.saulmm.marvel.utils.CoroutineTestRule

class CharacterDetailViewModelTest {

    @Mock
    lateinit var characterRepository: CharacterRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `when viewmodel initializes, a loading event is emitted`() = coroutineTestRule.runBlockingTest {
        characterRepository.stub {
            onBlocking { characterRepository.character(any()) }.thenReturn(any())
        }

        coroutineTestRule.testDispatcher.pauseDispatcher()

        val viewModel = CharacterDetailViewModel(
            characterPreview = CharacterPreview(1, "Hulk", Image("hulk", ".jpg")),
            characterRepository = characterRepository
        )

        viewModel.onViewState.test {
            assertThat(awaitItem()).isNull()
            assertThat(awaitItem()).isInstanceOf(CharacterDetailViewState.Loading::class.java)
            cancelAndIgnoreRemainingEvents()
        }

        coroutineTestRule.testDispatcher.resumeDispatcher()
    }

    @Test
    fun `when the character loads successfully, a success view state is emitted`() = coroutineTestRule.runBlockingTest {
        val hulkPreview = CharacterPreview(1, "Hulk", Image("hulk", ".jpg"))
        val hulk = Character(id = 1, name = "Hulk", comics = emptyList())

        characterRepository.stub {
            onBlocking { characterRepository.character(id = 1) }.thenReturn(hulk)
        }

        coroutineTestRule.testDispatcher.pauseDispatcher()

        val viewModel = CharacterDetailViewModel(
            characterPreview = hulkPreview,
            characterRepository = characterRepository
        )

        viewModel.onViewState.test {
            awaitItem() // null
            awaitItem() // Loading
            assertThat(awaitItem()).isInstanceOf(CharacterDetailViewState.Success::class.java)
            cancelAndIgnoreRemainingEvents()
        }

        coroutineTestRule.testDispatcher.resumeDispatcher()
    }


    @Test
    fun `when the character load fails, a error view state is emitted`() = coroutineTestRule.runBlockingTest {
        val hulkPreview = CharacterPreview(1, "Hulk", Image("hulk", ".jpg"))

        characterRepository.stub {
            onBlocking { characterRepository.character(id = 1) }.thenThrow(RuntimeException())
        }

        coroutineTestRule.testDispatcher.pauseDispatcher()

        val viewModel = CharacterDetailViewModel(
            characterPreview = hulkPreview,
            characterRepository = characterRepository
        )

        viewModel.onViewState.test {
            awaitItem() // null
            awaitItem() // Loading
            assertThat(awaitItem()).isInstanceOf(CharacterDetailViewState.Failure::class.java)
            cancelAndIgnoreRemainingEvents()
        }

        coroutineTestRule.testDispatcher.resumeDispatcher()
    }
}