# Flicker Android App

### Flicker App Jetpack Components
App demonstrating Clean Architecture using Coroutines and Android Jetpack Components (Room, MVVM and Live Data)

- Displaying recent image search results
- Supporting offline first architecture

### ScreenShots
<img src = "https://github.com/pawanchauhan05/Flicker/blob/dev/screenshots/listing_from_remote_success.jpeg" width = 260 height = 550/> <img src = "https://github.com/pawanchauhan05/Flicker/blob/dev/screenshots/local_source_listing_no_internet_error.jpeg" width = 260 height = 550/> <img src = "https://github.com/pawanchauhan05/Flicker/blob/dev/screenshots/empty_list_no_interner_error.jpeg" width = 260 height = 550/>

### Tech-Stack

* __Retrofit__ : For Network calls
* __Architecture__ : MVVM
* __Coroutines__ for background operations like fetching network response
* __Room database__ : For offline persistence
* __Live Data__ : To notify view for change
* __Hilt__ : To injecting dependancy (Google Dependency injection framework)
* __Language__ : Kotlin

### Source code & Test Cases
> **app/src/main:** Directory having **main source code** of the Flicker app.

> **app/src/androidTest:** Directory having **Local Database Test Case** of the Flicker app.

> **app/src/test:** Directory having **JUnit Test Case** for repository, ViewModel, Remote Date Source and Local Data Source of the Flicker app.

### Architecture Diagram
This application strictly follows the below architecture

<img src = "https://github.com/pawanchauhan05/Flicker/blob/dev/screenshots/Architecture.png" width = 450 />

