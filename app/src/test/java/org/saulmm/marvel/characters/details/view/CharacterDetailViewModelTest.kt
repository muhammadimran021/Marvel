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
import org.saulmm.marvel.characters.domain.models.Character
import org.saulmm.marvel.characters.domain.models.CharacterPreview
import org.saulmm.marvel.characters.domain.models.Image
import org.saulmm.marvel.app.utils.runViewModelTest

class CharacterDetailViewModelTest {

    @Mock
    lateinit var characterRepository: CharacterRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `when viewmodel initializes, a loading event is emitted`() = runViewModelTest {
        characterRepository.stub {
            onBlocking { characterRepository.character(any()) }.thenReturn(any())
        }

        val viewModel = CharacterDetailViewModel(
            characterPreview = CharacterPreview(1, "Hulk", Image("hulk", ".jpg")),
            characterRepository = characterRepository
        )

        viewModel.onViewState.test {
            assertThat(awaitItem()).isNull()
            assertThat(awaitItem()).isInstanceOf(CharacterDetailViewState.Loading::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when the character loads successfully, a success view state is emitted`() = runViewModelTest {
        val hulkPreview = CharacterPreview(1, "Hulk", Image("hulk", ".jpg"))
        val hulk = Character(id = 1, name = "Hulk", comics = emptyList())

        characterRepository.stub {
            onBlocking { characterRepository.character(id = 1) }.thenReturn(hulk)
        }

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
    }

    @Test
    fun `when the character load fails, a error view state is emitted`() = runViewModelTest {
        val hulkPreview = CharacterPreview(1, "Hulk", Image("hulk", ".jpg"))

        characterRepository.stub {
            onBlocking { characterRepository.character(id = 1) }.thenThrow(RuntimeException())
        }

        val viewModel = CharacterDetailViewModel(
            characterPreview = hulkPreview,
            characterRepository = characterRepository
        )

        viewModel.onViewState.test {
            awaitItem() // null
            awaitItem() // Loading
            assertThat(awaitItem()).isInstanceOf(CharacterDetailViewState.Failure::class.java)
            cancel()
        }
    }

    @Test
    fun `when the repository dispatches a failure, a retry action is saved`() = runViewModelTest {
        val hulkPreview = CharacterPreview(1, "Hulk", Image("hulk", ".jpg"))

        characterRepository.stub {
            onBlocking { characterRepository.character(id = 1) }.thenThrow(IllegalStateException::class.java)
        }

        val viewModel = CharacterDetailViewModel(hulkPreview, characterRepository)

        viewModel.onViewState.test {
            awaitItem() // null
            awaitItem() // Loading
            awaitItem() // Failure
            cancel()

            assertThat(viewModel.tryAgainAction).isNotNull()
        }
    }

    @Test
    fun `when the repository dispatches a success, a retry action is null`() = runViewModelTest {
        val hulkPreview = CharacterPreview(1, "Hulk", Image("hulk", ".jpg"))

        characterRepository.stub {
            onBlocking { characterRepository.character(id = any()) }.thenReturn(any())
        }

        val viewModel = CharacterDetailViewModel(hulkPreview, characterRepository)

        assertThat(viewModel.tryAgainAction).isNull()
    }
}