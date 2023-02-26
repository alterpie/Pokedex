## Description
An app to look up the direct evolution chain of a Pokémon species.
The app shows a list of Pokémon species.
When the user selects a Pokémon from the list, the user navigates to a Pokémon detail screen.

The public *Poké-API*: https://pokeapi.co is used.

## Functionality
1. List of (paginated) Pokémon species (`v2/pokemon-species`)
   * Name
   * Image (For the image https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/{species_id}.png is used)

2. Details screen of species, containing:
   * Name
   * *flavor_text* (description)
   * Name & image of the first evolution species the current Pokémon evolves to. 
      * The Evolution chain is retrieved from here (`/v2/evolution-chain/{chain_id}`) 
   * The *capture_rate* difference between the current species and the species it evolves to. 
     * If capture_rate difference is negative it is highlighted in red, and in green if it is positive.
     * *capture_rate* is retrieved from https://pokeapi.co/api/v2/pokemon-species/{pokemon-id}

## Assumptions
* Species evolves into only 1 other species
* Use english locale for text

Dear reviewer! Here are some notes about the implementation details of this project:

1. Application uses local database as single source of truth for app data. It provides observable source so whenever there are any changes to the locally stored data - every subscriber will be notified.
2. Application supports following configuration changes: screen rotation, dark mode.
3. Application draws it's content behind system bars and handles window insets.
4. For presentation layer MVI architecture with some adjustments is used in order to achieve loose coupling. Main component that connects logic altogether is Feature, it is completely decoupled from any android related dependencies(could be reused in multiplatform setup for presentation layer of the app). Another vital components are:
- Actor(performs logic required to manipulate domain data for the screen)
- Reducer(maps domain data to screen state).
  All components communicate via proxy classes that carry the data related to what logic should be invoked or what data should be updated. These are:
- Event(comes from UI layer as an action to users interaction with it)
- Effect(each Event triggers some logic which results in Effects that carry the data necessary to update the State)
- State(model that represents UI state of the screen)

Such architecture is easily scalable since another handlers for screen contract's related classes could be added without adding changes to existing ones. All components aren't aware of each other and it's easy to unit test them.

5. UI is implemented via Jetpack Compose(special compose related are optimizations included: kotlin [immutable collections](https://github.com/Kotlin/kotlinx.collections.immutable), R8 minification support).
6. One time events are handled as part of the State updates as recommended by [Google](https://developer.android.com/topic/architecture/ui-layer/events#handle-viewmodel-events) for Compose architecture.
7. Gradle setup is done via separate scripts for different types of modules/functionality

App architecture could be described by following scheme(without some intermediate abstractions as use cases):

![App arch](assets/arch_scheme.png)
