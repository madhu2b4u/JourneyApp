# Journey App - Posts and Post Details Features (MVVM Clean Architecture)

This project demonstrates the implementation of the **Posts** and **Post Details** features using the MVVM architecture in Jetpack Compose, with **Dagger-Hilt** for dependency injection.

## Features
- **List of Posts**: Fetch posts from an API and display them in a scrollable list.
- **Search Functionality**: Dynamically search and filter posts based on the title.
- **Post Details**: Navigate to a detailed view of a selected post.
- **MVVM Architecture**: Separation of concerns with `ViewModel`, `UseCase`, and `Repository`.

## Project Structure

```bash
com.divine.journey
├── di                    # Dependency Injection with Dagger Hilt
├── navigation             # Navigation logic between screens
├── posts                  # Posts feature module
│   ├── data               # Data layer (API, Models, Repositories)
│   ├── domain             # Business logic (UseCases)
│   └── presentation       # UI Layer (ViewModels, Composables)
└── MainActivity.kt        # Application entry point

```

## Technologies & Tools

- **Jetpack Compose**: For building declarative UI.
- **Dagger-Hilt**: For Dependency Injection.
- **Kotlin Coroutines**: For handling asynchronous tasks.
- **StateFlow**: To manage and observe UI state.
- **Unit Testing**: For testing `ViewModel` logic.

# Features Breakdown

## Posts Feature

### 1. Fetching and Displaying Posts
The `PostsViewModel` fetches a list of posts using a `PostsUseCase`, which communicates with the data layer. This fetched data is then observed by the `PostListScreen` to update the UI.

### 2. Search Functionality
The `ViewModel` observes the search query and dynamically filters the list of posts to display only those that match the query.

### 3. Post Details
The app allows users to navigate to a `PostDetailsScreen` to view detailed information about a selected post.

# Getting Started

## Prerequisites

- **Android Studio**
- **Kotlin 1.5+**
- **Gradle 7.0+**
- An internet connection to fetch data from the API.


## Setup Instructions

1. **Clone the repository**:
   ```bash
   git clone https://github.com/madhu2b4u/JourneyApp.git
2. **Open the project in Android Studio.**
3. **Build the project and run the app on an Android emulator or physical device.**

# How It Works

## MVVM Architecture

The project follows the **MVVM (Model-View-ViewModel)** pattern to promote a clean separation of concerns:

### Model (Data Layer):
- Responsible for data handling, including fetching from APIs and managing data models.
- **Example**: `PostsRepository`, `Post` data class.

### ViewModel (Domain Layer):
- Handles business logic and prepares data for the UI.
- **Example**: `PostsViewModel`.

### View (Presentation Layer):
- Composes the UI using Jetpack Compose and observes data from the ViewModel.
- **Example**: `PostListScreen`, `PostDetailsScreen`.

---

## Posts Feature

### 1. Fetching and Displaying Posts

#### ViewModel (`PostsViewModel`):
- Utilizes `PostsUseCase` to fetch posts from the repository.
- Manages UI state using `StateFlow`.
- Observes search queries to filter posts dynamically.

#### UI (`PostListScreen`):
- Displays a list of posts in a `LazyColumn`.
- Includes a `TextField` for users to input search queries.
- Navigates to `PostDetailsScreen` upon selecting a post.

---

### 2. Search Functionality

#### ViewModel:
- Listens to changes in the search query.
- Filters the list of posts based on the input, updating the UI accordingly.

#### UI:
- Reflects the filtered list of posts in real-time as the user types.

---

### 3. Post Details

#### UI (`PostDetailsScreen`):
- Shows detailed information about a selected post.
- Displays associated comments fetched from the repository.

