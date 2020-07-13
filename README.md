# Prerequisites
Android Studio `3.5.x`
XCode `11.3.x`

# Structure
The project has three main subfolders:
* `app` is the Android app
* `ios` is the iOS app
* `shared` is the **Kotlin Multiplatform** library that's accessible to both apps

# Setup

You'll need to edit or override `local.properties` to point to your own Android SDK directory

To build the **Koltin Multiplatform** library you might want to run `./gradlew sharedcode:packForXCode` at least once.  

## Features
*   Targeting Android and IOS mobile plateform
*   [MVVM](https://en.wikipedia.org/wiki/Model–view–viewmodel) Design pattern
*   [Lifecycle Arch component](https://developer.android.com/topic/libraries/architecture/lifecycle) and LiveData on Android.
*   [SwiftUI on iOS](https://developer.apple.com/xcode/swiftui/)
*   Network calls with [Ktor](https://ktor.io/clients/index.html)
*   Data persistence on both Android & IOS with [SQLDelight](https://cashapp.github.io/sqldelight/)


## Notes
### MVVM (Model View View Model)
MVVM is my preferred design pattern as it make sure you write quality maintainable code and also helps to follow
the [SOLID](https://en.wikipedia.org/wiki/SOLID) and clean architecture.

#### Advantages

Maintainability
----------------
*   A clean separation of code should make it easier to go into one or several of those more granular and focused parts and make changes without worrying.
*   That means you can remain agile and keep moving out to new releases quickly.
*   The presentation layer and the logic is loosely coupled.

Testability
---------------
*   If implemented right your external and internal dependences are in separate pieces of code from the parts with the core logic that you would like to test.
*   That makes it a lot easier to write unit tests against a core logic.
*   The ViewModel is easier to unit test than code-behind or event-driven code.

Extensibility
----------------
*   Clean separation boundaries and more granular pieces of code.


### Disadvantages

*   Debugging would be bit difficult when we have complex data bindings.
*   Some people think that for simple UIs, MVVM can be overkill.
    



