# 🍔 Restaurant App

A restaurant listing app built with MVI architecture and clean architecture principles. Shows a list of restaurants with sorting, favoriting, and detail views.

[![Issues](https://img.shields.io/github/issues/kl3jvi/mvi_clean_architecture)](https://github.com/kl3jvi/mvi_clean_architecture/issues)
[![Stars](https://img.shields.io/github/stars/kl3jvi/mvi_clean_architecture)](https://github.com/kl3jvi/mvi_clean_architecture)
[![License](https://img.shields.io/badge/License-MIT-orange.svg)](/LICENSE)

## Screenshots

[<img src="images/sc_1.png" width=30%>](images/sc_1.png)
[<img src="images/sc_2.png" width=30%>](images/sc_2.png)
[<img src="images/sc_3.png" width=30%>](images/sc_3.png)

## Architecture

The app follows MVI (Model-View-Intent) with unidirectional data flow. Each screen has its own ViewModel that handles user intents and emits state updates.

```mermaid
graph TB
    subgraph Presentation["feature-home"]
        Fragment["Fragment"] -->|"emits Intent"| VM["ViewModel"]
        VM -->|"State"| Fragment
        VM -->|"Effect"| Fragment
    end
    
    subgraph Domain["core-domain"]
        UC["Use Cases"]
        Repo["Repository Interface"]
    end
    
    subgraph Data["core-data"]
        RepoImpl["RestaurantRepositoryImpl"]
        DS["RestaurantDataSource"]
    end
    
    subgraph Persistence["core-persistence"]
        DAO["RestaurantDao"]
        DB["Room Database"]
    end
    
    VM --> UC
    UC --> Repo
    RepoImpl -.->|implements| Repo
    RepoImpl --> DS
    RepoImpl --> DAO
    DAO --> DB
```

### MVI Flow

```mermaid
flowchart LR
    User([User Action]) --> Intent
    Intent --> Reducer["handleIntent()"]
    Reducer --> State
    State --> UI[UI Update]
    
    Reducer -.-> Effect
    Effect -.-> SideEffect[One-shot Event]
```

**Key components:**
- **Intent** — User actions (tap sort, toggle favorite, etc.)
- **State** — Immutable UI state, survives config changes
- **Effect** — One-time events (show toast, navigate)

## Module Structure

```
├── app                   # Main app, DI setup
├── feature-home          # Home + Details screens
├── core-domain           # Use cases, repository interfaces
├── core-data             # Repository implementations, data sources
├── core-persistence      # Room database, DAOs
├── core-model            # Shared data models
└── core-common           # Utilities, base classes
```

## Tech Stack

- Kotlin, Coroutines, Flow
- Jetpack (ViewModel, Room, Navigation, DataBinding)
- Hilt for DI
- Material Components
- Turbine for Flow testing

## Building

```bash
# Debug build
./gradlew assembleDebug

# Run tests
./gradlew testDebugUnitTest
```

**Requirements:** JDK 17+, Android SDK 34

## Contributing

PRs welcome! Fork, make changes, submit a pull request.
