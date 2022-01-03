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

## Libraries

## UI

## Considerations