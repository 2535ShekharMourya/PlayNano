# 🎬 PlayNano – Short-Series Streaming App

PlayNano is an Android streaming application designed for consuming **short-series content**, where each series contains **5–10 short episodes**. The app delivers a smooth playback experience with fast navigation, clean architecture, and modern Android development practices.

---

## ✨ Features
- 🎥 Stream short-series with multiple episodes
- ⚡ Fast episode loading and smooth playback
- 📱 Hybrid UI using **Jetpack Compose & XML**
- 🔄 Seamless episode navigation
- 🌐 Dynamic content fetched from REST APIs
- 🧩 Scalable and maintainable MVVM architecture

---

## 🛠 Tech Stack
- **Language:** Kotlin  
- **UI:** Jetpack Compose + XML  
- **Architecture:** MVVM  
- **Networking:** Retrofit  
- **Concurrency:** Kotlin Coroutines  
- **Media Playback:** ExoPlayer  
- **Image Loading:** Glide / Coil  
- **Build Tool:** Gradle  

---

## 🏗 Architecture Overview
PlayNano follows **MVVM architecture** to maintain a clear separation of concerns:

- **View:** Compose screens & XML layouts for UI
- **ViewModel:** Manages UI state, episode selection, and playback logic
- **Repository:** Handles API communication
- **Data Layer:** REST APIs providing series, episodes, metadata, and thumbnails

This architecture ensures **testability, scalability, and smooth UI performance**.

---



