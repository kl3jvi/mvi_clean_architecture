

<h2 align="center"><b>🍔 Restaurant App - MVI Architecture Example </b></h2>
<h4 align="center">Restaurant listing android app using MVI architecture. Using jetpack libraries such as Flows,Coroutines, Dagger-Hilt, Room etc. </h4>


<p align="center">
<a href="hhttps://github.com/kl3jvi/animity/issues" alt="GitHub release"><img src="https://img.shields.io/github/issues/kl3jvi/mvi_clean_architecture" ></a>
<a href="https://github.com/kl3jvi/animity" alt="GitHub release"><img src="https://img.shields.io/github/stars/kl3jvi/mvi_clean_architecture" ></a>
<a href="/LICENSE" alt="License: GPLv3"><img src="https://img.shields.io/badge/License-MIT-orange.svg"></a>
<a href="https://github.com/kl3jvi/animity" alt="Build Status"><img src="https://img.shields.io/github/forks/kl3jvi/mvi_clean_architecture"></a>
</p>
<hr>

<h3 align="center">**Star :star:  this repo to show your support and it really does matter!** :clap:</h4>


## Screenshots

[<img src="images/sc_1.png" width=30%>](images/sc_1.png)
[<img src="images/sc_2.png" width=30%>](images/sc_2.png)
[<img src="images/sc_3.png" width=30%>](images/sc_3.png)


## Tech stack & Open-source libraries
- Minimum SDK level 21
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- Jetpack
  - Lifecycle: Observe Android lifecycles and handle UI states upon the lifecycle changes.
  - ViewModel: Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
  - DataBinding: Binds UI components in your layouts to data sources in your app using a declarative format rather than programmatically.
  - Room: Constructs Database by providing an abstraction layer over SQLite to allow fluent database access.
  - [Hilt](https://dagger.dev/hilt/): for dependency injection.
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
  - Repository Pattern
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit): Construct the REST APIs and paging network data.
- [ksp](https://github.com/google/ksp): Kotlin Symbol Processing API.
- [Turbine](https://github.com/cashapp/turbine): A small testing library for kotlinx.coroutines Flow.
- [Material-Components](https://github.com/material-components/material-components-android): Material design components for building ripple animation, and CardView.


## Architecture
**This App** is based on the MVVM architecture and the Repository pattern, which follows the [Google's official architecture guidance](https://developer.android.com/topic/architecture).

<img src="https://raw.githubusercontent.com/skydoves/Pokedex/main/figure/figure0.png"/>

## Contribution
Your ideas, translations, design changes, code cleaning, or real heavy code changes or any help is always welcome. The more is contribution the better it gets

[Pull requests](https://github.com/kl3jvi/animity/pulls) will be reviewed
