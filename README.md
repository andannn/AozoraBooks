# Aozora Reader

**AozoraBooks** is a modern, cross-platform reading app built with **Kotlin Multiplatform**, designed for reading novels in the [Aozora Bunko (青空文庫)](https://www.aozora.gr.jp/) format. 
It supports advanced layout features like ruby annotations and vertical text, and runs on Android, iOS.

⚠️ This project is in a very early stage of development. 

## Features

- Support for Aozora Bunko HTML format, including:
  - Ruby text (`<ruby>`)
  - ..
- Cross-platform with shared codebase (Android / iOS)

## Screenshots

## 🛠️ Tech Stack

| Layer          | Tech                                 |
|----------------|--------------------------------------|
| Language       | Kotlin Multiplatform                 |
| UI             | Jetpack Compose Multiplatform        |
| Parsing        | Ksoup (for HTML), custom parser      |
| I/O            | Okio, kotlinx-io                     |
| State Mgmt     | Circuit (Composable UDF)             |
| Dependency Injection | Koin                           |
| Platforms      | Android, iOS                         |

## 🚀 Getting Started

### Prerequisites

### Run the app

#### Android
