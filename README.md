# Marvel Characters

'Marvel Characters' is a sample project focused on showing practices of modern android development focusing on architecture, ui and jetpack libraries.

**Contents**

*   [Navigation](#Navigation)
*   [Architecture](#Architecture)
*   [Libraries](#Libraries)
*   [UI](#UI)
*   [Considerations](#Considerations)

## Navigation

This application is a very simple mobile client of the marvel API. Composed of two screens: list of characters, detail of a character.

Fragments have been chosen to implement these screens, activities could have been used instead, however, fragments have been used as a lighter and more reusable method than opening a particular activity.

- [`CharacterListFragment`]()
- [`CharacterDetailFragment`]()

However, a fragment does not know anything about the other fragment, these assume that its host activity is able to manage navigation implementing the [`HomeNavigator`]() interface. This way they both can and navigate, for example from the list screen to the detail screen when a character is clicked.

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

### The UI layer


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


## Libraries

## UI

## Considerations