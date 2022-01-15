package org.saulmm.marvel.characters.list.view

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.stub
import org.mockito.kotlin.whenever
import org.saulmm.marvel.app.utils.CoroutineDispatcherRule
import org.saulmm.marvel.characters.data.CharacterRepository
import org.saulmm.marvel.characters.domain.models.CharacterPreview
import org.saulmm.marvel.characters.domain.models.Image
import org.saulmm.marvel.characters.list.view.CharactersListViewState.*

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterListViewModelTest {

    @Mock
    lateinit var characterRepository: CharacterRepository

    @get:Rule
    val coroutineDispatcherRule = CoroutineDispatcherRule()

    private val charactersListPage1 = listOf(
        CharacterPreview(1, "Iron Man", Image("iron_man", ".jpg")),
        CharacterPreview(2, "Captain America", Image("captain_america", ".jpg")),
        CharacterPreview(3, "Hulk", Image("hulk", ".jpg")),
    )

    private val charactersListPage2 = listOf(
        CharacterPreview(4, "Spider Man", Image("spider_man", ".jpg")),
        CharacterPreview(5, "Black Widow", Image("black_widow", ".jpg")),
        CharacterPreview(6, "Thor", Image("thor", ".jpg")),
    )

    private val charactersListPage3 = listOf(
        CharacterPreview(7, "Black Panther", Image("black_panter", ".jpg")),
        CharacterPreview(8, "Captain Marvel", Image("captain_marvel", ".jpg")),
        CharacterPreview(9, "Doctor Strange", Image("doctor_strange", ".jpg")),
    )


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `when an error happens in the repository, an error is emitted`() = runTest {
        characterRepository.stub {
            onBlocking { characterRepository.characters(offset = any()) }.thenThrow(IllegalStateException::class.java)
        }

        val viewModel = CharacterListViewModel(characterRepository)

        viewModel.onViewState.test {
            awaitItem() // null
            awaitItem() // Loading
            assertThat(awaitItem()).isInstanceOf(Failure::class.java)
        }
    }

    @Test
    fun `when the repository dispatches characters, these are emitted as success`() = runTest {
        whenever(characterRepository.characters(offset = any())).thenReturn(charactersListPage1)

        val viewModel = CharacterListViewModel(characterRepository)

        viewModel.onViewState.test {
            awaitItem() // null
            awaitItem() // Loading
            awaitItem() // Success
            cancel()

            val characters = (viewModel.onViewState.value as Success).characters
            assertThat(characters.size).isEqualTo(charactersListPage1.size)
        }
    }

    @Test
    fun `when the repository dispatches a failure, a retry action is saved`() = runTest {
        whenever(characterRepository.characters(offset = any()))
            .thenThrow(IllegalStateException::class.java)

        val viewModel = CharacterListViewModel(characterRepository)

        viewModel.onViewState.test {
            awaitItem() // null
            awaitItem() // Loading
            awaitItem() // Failure
            assertThat(viewModel.tryAgainAction).isNotNull()
            cancel()
        }
    }

    @Test
    fun `when the repository dispatches a success, a retry action is null`() = runTest {
        whenever(characterRepository.characters(offset = any())).thenReturn(charactersListPage1)

        val viewModel = CharacterListViewModel(characterRepository)

        assertThat(viewModel.tryAgainAction).isNull()
    }

    @Test
    fun `when viewmodel initializes a loading event is emitted`() = runTest {
        whenever(characterRepository.characters(offset = any())).thenReturn(charactersListPage1)

        val viewModel = CharacterListViewModel(characterRepository)

        viewModel.onViewState.test {
            assertThat(awaitItem()).isNull()
            assertThat(awaitItem()).isInstanceOf(Loading::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when the repository dispatches a success, loading and success are emitted`() = runTest {
        whenever(characterRepository.characters(offset = any())).thenReturn(charactersListPage1)

        val viewModel = CharacterListViewModel(characterRepository)

        viewModel.onViewState.test {
            assertThat(awaitItem()).isNull()
            assertThat(awaitItem()).isInstanceOf(Loading::class.java)
            assertThat(awaitItem()).isInstanceOf(Success::class.java)
            cancel()
        }
    }

    @Test
    fun `when requesting more characters, a full list is delivered`() = runTest {
        whenever(characterRepository.characters(offset = 0)).thenReturn(charactersListPage1)
        whenever(characterRepository.characters(offset = 1)).thenReturn(charactersListPage2)
        whenever(characterRepository.characters(offset = 2)).thenReturn(charactersListPage3)

        val viewModel = CharacterListViewModel(characterRepository)

        viewModel.onViewState.test {
            awaitItem() // Null
            awaitItem() // Loading
            awaitItem() // Success
            viewModel.loadCharacters(1) // Load page 2
            awaitItem() // Loading
            awaitItem() // Success
            viewModel.loadCharacters(2) // Load page 3
            awaitItem() // Loading

            val twoPagesList = (awaitItem() as Success).characters.also { cancel() }
            val characterTitles = twoPagesList.map { it.name }

            assertThat(twoPagesList.size).isEqualTo(9)
            assertThat(characterTitles).containsExactly(
                "Iron Man",
                "Captain America",
                "Hulk",
                "Spider Man",
                "Black Widow",
                "Thor",
                "Black Panther",
                "Captain Marvel",
                "Doctor Strange",
            )
        }
    }
}
