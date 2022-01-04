# Marvel Characters

'Marvel characters' is a simple application to display information about the characters of the marvel universe. It uses different patterns and libraries from modern android development, such as Material3, MVVM, unidirectional data flow, coroutines, Dagger Hilt, view binding, and others.

It contains two screens. One shows a list of characters and another with detailed information about a specific character.

Both screens are implemented using fragments:

- [`CharacterListFragment`]()
- [`CharacterDetailFragment`]()

A fragment does not know anything about the other fragment. There is a host activity in charge of managing the navigation implementing the [`HomeNavigator`]() interface. This is how the navigation is done when clicking on a character item to show the item detail.

**Contents**

*   [Navigation](#Navigation)
*   [Architecture](#Architecture)
*   [Libraries](#Libraries)
*   [UI](#UI)
*   [Considerations](#Considerations)

## Architecture

The architecture of the project is based on my **opinionated interpretation** of the architectural pattern [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html), adapted to this project. 

It is also aligned on the recently published: ['Guide to app architecture'](https://developer.android.com/jetpack/guide) from the official docs. The main goal followed is the [separation of concerns](https://en.wikipedia.org/wiki/Separation_of_concerns) between the different layers identified.

*   [The domain layer](#The domain layer)
*   [The data layer](#The data layer)
*   [The UI layer](#The UI layer)

### The domain layer

In this simple project the domain layer is simple enough to only contain the entities, or models used by the other layers.

### The data layer

To implement the data layer, the repository pattern has been used to extend, to other layers, a single entry point to obtain the data of the marvel characters.

For simplicity, the character repository only uses one data source, which implements data extraction from the remote Marvel API. 

It would be easy to implement another data source with a database or any other persistence method to enrich this layer. This is the reason why there is an interface to implement a data source.



#### Concrete benefit of using a repository with data sources

From the Marvel API point of view, to send to the view a list of comics with their images and a portion of text, **it is a complex job.** 

To perform this task we need to do the following:

1. Fetch the list of comics for a given characters.

```kotlin
@GET("/v1/public/characters")
suspend fun characterDetail(@Query("id") id: Int): MarvelApiResponseDto<CharacterDetailDto>
```

2. For a given comic we must fetch its details by using each URL provided in the list of comics per comic.
```kotlin
@GET
suspend fun comic(@Url url: String): MarvelApiResponseDto<ComicDto>
```

Thanks to the use of data sources, the repository simply expects a list of `List<Comic>` . All the other implementation details for the Marvel API are encapsulated in the `CharacterRemoteDatasource`. 

Similarly, this method is very easily testable by simply creating a test for that class, see `CharacterTemoteDataSourceTest`.

### The UI layer

The UI layer is made up of the elements of the android SDK, the implementation of the views and other logic related to the user interface.

For the UI layer, it again aligns with the guide from [official docs] (https://developer.android.com/jetpack/guide/ui-layer).

#### Unidirectional data flow

We could say that the user interface is what the app says the user should see. We could define a series of UI states that model the way the view can be at any given time.

For example, a screen that shows a list of characters may be in a loading state (when fetching the data), in a success state (when the characters are fetched successfuly), or in a failure state (when something happened when loading the characters). 

```kotlin
    sealed class CharactersViewState {
        object Loading: CharactersViewState()
        class Failure(e: Throwable): CharactersViewState()
        class Success(val characters: List<CharacterPreview>): CharactersViewState()
    }
```

The view, in this case a fragment, **observes a single source** of states, and **reacts** when the viewmodel (in charge of producing states) emits a new state. When the view receives a new view state, its immediately bound into the UI. This way a view state is shown atomically, avoiding issues where multiples sources or truth are used.

This concept is also known as Unidirectional DataFlow. It provides severals benefits like, as the source of the view state is isolated, it's cycle of emissions can be tested easily. 

```kotlin
// ...
viewModel.onViewState.test {
    assertThat(awaitItem()).isNull()
    assertThat(awaitItem()).isInstanceOf(Loading::class.java)
    assertThat(awaitItem()).isInstanceOf(Success::class.java)
}

```

## Libraries

### Dagger - Hilt

Dagger-Hilt has been used for dependency injection. Hilt provides a very simple mechanism to inject dependencies with minimal boilerplate into android components. Helping the testability and cleanliness of the code.

Assist injection has also been used, for, among other uses, to be able to insert dynamic parameters in the creation of the viewmodels. [Assisted injection application]().



## UI


## Considerations

- Paging has been implemented in the character list but the **jetpack paging library has not been used**. Paging is simply accomplished with a callback from the [`CharacterListAdapter`]() that notifies when the end of the list has been reached.
  
  <br>
  The reason behind is to have prioritized the architecture, since the paging library, while providing a good experience when loading elements, is a bit aggressive in terms of architecture modeling.
