# Marvel Characters

'Marvel characters' is a simple application to display information about the characters of the Marvel universe. 

You can test the application by installing the [following apk](https://github.com/saulmm/Marvel/blob/main/marvel.apk?raw=true).

It uses different patterns and libraries from modern android development, such as Material 3, MVVM, unidirectional data flow, coroutines, Dagger Hilt, view binding, and others.

| <img src="https://user-images.githubusercontent.com/3531999/148698949-b8531ae2-6230-4419-960b-c5253777116c.jpg" height="700"/> | <img height="700" src="https://user-images.githubusercontent.com/3531999/148698950-ae8f1324-e710-41e7-8bd0-e784eb09b4c0.jpg"/>
|---|---|


It contains two screens. One shows a list of characters and another with detailed information about a specific character. Both screens are implemented using fragments:

- [`CharacterListFragment`](https://github.com/saulmm/marvel/blob/main/app/src/main/java/org/saulmm/marvel/characters/list/view/CharacterListFragment.kt)
- [`CharacterDetailFragment`](https://github.com/saulmm/marvel/blob/main/app/src/main/java/org/saulmm/marvel/characters/details/view/CharacterDetailFragment.kt)

Fragments are independent, one does not know anything about the other fragment. There is a host activity in charge of managing the navigation by implementing the [`HomeNavigator`](https://github.com/saulmm/marvel/blob/main/app/src/main/java/org/saulmm/marvel/home/view/HomeNavigator.kt) interface. This is how the navigation is done from clicking on a character item to show the item detail.

**Contents**

*   [Architecture](#Architecture)
*   [Considerations](#Considerations)

## Architecture

The architecture of this project is based on my interpretation of '[Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)', an architecture pattern focused on the [separation of concerns](https://en.wikipedia.org/wiki/Separation_of_concerns). Finding the following modules:

*   [The domain layer](#the-domain-layer)
*   [The data layer](#the-data-layer)
*   [The UI layer](#the-ui-layer)

### The domain layer

Given the simplicity of the project, the domain layer is only made up of entities, if there were any complex business logic, it would be implemented here in the form of use cases.

These entities are used as a 'source of truth' at the boundaries of the other layers. For example, repository in the data layer exposes a list of characters that are consumed in the UI layer by the view models.

### The data layer

The responsibility of the data layer is to expose character's data to other parts of the app, and to abstract the source of the data.

For simplicity, the character repository only uses [one data source](https://github.com/saulmm/marvel/blob/main/app/src/main/java/org/saulmm/marvel/characters/data/remote/CharacterRemoteDatasource.kt), which implements data extraction from the remote Marvel API. 

It would be easy to implement another data source with a database or any other persistence method to enrich this layer. This is the reason why there is an interface to represent a data source.


#### Concrete benefit of using a repository with data sources

Given the simplicity of the project, access to the data source is implemented with a repository that uses a single data source.

The data source communicates with the Marvel API and returns the data to the repository, using a domain entity: [`CharacterPreview`](https://github.com/saulmm/marvel/blob/main/app/src/main/java/org/saulmm/marvel/characters/domain/models/Character.kt), or [`Character`](https://github.com/saulmm/marvel/blob/main/app/src/main/java/org/saulmm/marvel/characters/domain/models/Character.kt).

The detail screen shows the details of a character and a few comics where it appears. A comic, in our domain, contains an image and a portion of text.

Using the Marvel API, this is a somewhat complex task as it requires using multiple endpoints:

1. Get the details of a character.

2. Get every comic detail, using the links provided by the detail of a character.

Thanks to the use of data sources, the repository simply expects a list of characters. All the other implementation details related to the Marvel API are encapsulated inside the [`CharacterRemoteDatasource`](https://github.com/saulmm/marvel/blob/main/app/src/main/java/org/saulmm/marvel/characters/data/remote/CharacterRemoteDatasource.kt), making it [easily testable](https://github.com/saulmm/marvel/blob/main/app/src/test/java/org/saulmm/marvel/characters/data/remote/CharacterRemoteDatasourceTest.kt).

### The UI layer

The UI layer is composed with elements from the android SDK, the implementation of the views and other logic related to the user interface. it aligns with the 'UI layer' guide from [official docs](https://developer.android.com/jetpack/guide/ui-layer).

We could say that the user interface should reflect what the app wants the user to see at a specific moment in time, this could be modeled as states of the UI.

For example, the character list may be loading, displaying the characters, or displaying an error if something unexpected occurs.

In this way, we could define three UI states: `Loading`,` Success` & `Failure`.

```kotlin
  sealed class CharactersViewState {
      object Loading: CharactersViewState()
      class Failure(e: Throwable): CharactersViewState()
      class Success(val characters: List<CharacterPreview>): CharactersViewState()
  }
```

The view, in this case a fragment, **observes a single source** of states, and **reacts** when the viewmodel (in charge of producing states) emits a new state.

A [`ViewModel`](https://developer.android.com/topic/libraries/architecture/viewmodel) is a great component for storing current UI state, as it survives fragment's lifecycle and other configuration changes. Thanks to the use of `StateFlow` to store the state, it's trivial to restore the last state if a fragment is recreated due to a configuration change.

When a fragment receives a new view state, it's immediately bound into the UI. This way, all views in a fragment are perfectly set to the given view state atomically, avoiding issues where multiples sources or truth are used.



This concept is also known as [Unidirectional Data Flow](https://en.wikipedia.org/wiki/Unidirectional_Data_Flow_(computer_science)). It provides several benefits like, as the source of the view state is isolated, it's cycle of emissions can be tested easily. 

```kotlin
// ...
viewModel.onViewState.test {
    assertThat(awaitItem()).isNull()
    assertThat(awaitItem()).isInstanceOf(Loading::class.java)
    assertThat(awaitItem()).isInstanceOf(Success::class.java)
}

```

## Considerations

### Paging

Paging has been implemented in the character list but the **jetpack paging library has not been used**. Paging is simply accomplished with a callback from the [`CharacterListAdapter`]() that notifies when the end of the list has been reached.
  
The reason behind is to have prioritized the architecture, since the paging library, while providing a good experience when loading elements, is a bit aggressive in terms of architecture modeling.

### Dagger - Hilt

Dagger-Hilt has been used for dependency injection. Hilt provides a very simple mechanism to inject dependencies with minimal boilerplate into android components. Helping the testability and cleanliness of the code.

Assist injection has also been used, for, among other uses, to be able to insert [dynamic parameters](https://github.com/saulmm/marvel/blob/main/app/src/main/java/org/saulmm/marvel/characters/details/view/CharacterDetailViewModel.kt#L20) in the creation of the view models.

### Signing a Marvel Request

The Marvel API, in order to [authorize a request](https://developer.marvel.com/documentation/authorization), requires that each endpoint must include a `ts` parameter and a` hash`  that is computed with the private key and a timestamp.

To avoid dirtying each endpoint or duplicating the authorization logic, a [`MarvelApiServiceAuthenticatorInterceptor`](https://github.com/saulmm/marvel/blob/main/app/src/main/java/org/saulmm/marvel/characters/data/remote/api/MarvelApiServiceAuthenticatorInterceptor.kt) has been implemented that adds the necessary parameters to sign each request.

In this way, the logic is encapsulated, and it's [easily testable](https://github.com/saulmm/marvel/blob/a5d802042d3247d6bf62fd5a40170c7d418ff3df/app/src/test/java/org/saulmm/marvel/characters/data/remote/api/MarvelApiServiceAuthenticatorInterceptorTest.kt).

### Mock web server in unit tests

[**MockWebServer**](https://github.com/square/okhttp/tree/master/mockwebserver) it's used to test elements of the network layer, such as the signing of requests or the correct mapping of network entities to domain entities.

This library offers an easy and **deterministic** way to behave like a real network service, without the inconveniences of variable wait times, network problems or changes to the server. Allowing to load existing JSON payloads in the resources folder from the unit tests source root.
