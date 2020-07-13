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
*   [Espresso](https://developer.android.com/training/testing/espresso) UI tests on android
*   UI tests on IOS


## Notes

## MVVM (Model-View-View-Model)
MVVM is my preferred design pattern as it make sure you write quality maintainable code and also helps to follow
the [SOLID](https://en.wikipedia.org/wiki/SOLID) and clean architecture.

#### Advantages

*   it's a clean separation of the code.
*   Makes it easier to go into one parts and make changes without affecting other key parts of the code.
*   That means you can remain agile with new releases  of code quickly.
*   The presentation layer and the logic is loosely coupled.
*   The external and internal dependences are in separate pieces of code from the parts with the core logic beinfg tested are.
*   That makes it a lot easier to write unit tests against a core logic - this is not a feature I was able to run in the current app structure. Please can we discuss how to do this as part of the review/feedback. 

### Disadvantages

*   Some people think that for simple UIs, MVVM can be overkill.


## Assumptions

Due to the wide growth of the Kotlin language, Kotlin multiplatform applications are becoming quite popular. 
It gives you the ability to write software for a wide range of platforms sharing one code base(the business logic) not just mobile with 
a language that is already popular with developers. Koltin syntax is very similar to swift, making it easier to pick up by IOS developers.

Although initially the app architecture can take some getting use to, Kotlin multiplatform is really about pragmatic programming 
to help meet the growing demand to develop mobile applications on multiple platforms – a clean, clear way to avoid having to write the code twice.

    



