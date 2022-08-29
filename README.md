<h1 align="center">Kichkina Shahzoda</h1>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
</p>

<p align="center">
🎵 "Kichkina Shahzoda" app demonstrates modern Android app development with Jetpack Compose, Hilt, Material3, Coroutines, Flows, ExoPlayer2 based on MVVM architecture.
</p>
</br>

<p align="center">
  <img src="/preview/Kichkina%20Shahzoda.jpg"/>
</p>

# Download

You can download the release app on Google Play Store:

<a href="https://play.google.com/store/apps/details?id=xyz.teamgravity.kichkinashahzoda">
  <img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" width="200"/>
</a>

# Tech stack

- [Kotlin](https://kotlinlang.org/): first class programming language for native Android development.
- [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines): structured concurrency.
- [Kotlin Flows](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/): reactive communication.
- [Material3](https://m3.material.io/): modern UI/UX guidelines and components.
- [Jetpack Compose](https://developer.android.com/jetpack/compose): modern, declarative way of building UI in Kotlin.
- [Jetpack Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle): observe Android lifecycles and handle UI states upon the lifecycle changes.
- [Jetpack ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel): manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
- [Dagger Hilt](https://dagger.dev/hilt/): first class dependency injection for native Android development.
- [Firebase](https://firebase.google.com/): tracks analytics and crashes using the Firebase services.
- [ExoPlayer2](https://exoplayer.dev/): an application level media player for Android.
- [Compose Destinations](https://composedestinations.rafaelcosta.xyz/): a type-safe navigation for composables.
- [Timber](https://github.com/JakeWharton/timber): a logger with a small, extensible API.

# Architecture

"Kichkina Shahzoda" is based on the MVVM architecture pattern, Repository pattern, Mapper pattern.

<img src="/preview/mvvm-pattern.png"/>

# MAD Score

<p align="center">
  <img src="/preview/summary_1.png"/>
  <img src="/preview/summary_2.png"/>
</p>

# About

A simple and light app that plays "Kichkina Shahzoda" audio-book without internet connection (locally). It also supports dark theme and dynamic theme. It also robust to configuration changes and has adaptive screens that load according to screen orientation. The audio is played in the service, so when the app is closed, it doesn't affect the audio-play. Audio can be easily controlled via music notification.

# Features

- Play and pause audios.
- Skip to next and previous audios.
- Seek to the position using slider.
- Choose the audio to play.
- Control the audio using music notification.
- Dark/Light theme setup.
- Dynamic theme setup.
- Work without the internet.
- Play audio in the background (service).
- Simplicity that has only four screens.
- Adaptive screens that change according to screen orientation.

# License

```xml
Designed and developed by raheemadamboev (Raheem) 2022.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
