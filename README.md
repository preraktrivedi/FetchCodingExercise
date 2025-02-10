### FetchCodingExercise
`FetchCodingExercise` is an Android application that fetches structured data from a [remote JSON API](https://fetch-hiring.s3.amazonaws.com/hiring.json) and displays it in a list. User can scroll through the entire list and open a detailed view for an item.

## Features
- Fetches data from a data from a [remote JSON API](https://fetch-hiring.s3.amazonaws.com/hiring.json)
- Displays result in a list with following requirements (as defined):
  - Display all the items grouped by "listId"
  - Sort the results first by "listId" then by "name" when displaying.
  - Filter out any items where "name" is blank or null
- Click on an item in the list to go to the detailed view

## Technical Details
- Language: Kotlin
- Architecture: MVVM
- UI: Jetpack Compose with navigation

## Installation
- Clone the repository: `git clone https://github.com/preraktrivedi/FetchCodingExercise.git`
- Open the project in Android Studio
- Build + Run app (clean if needed)

## Walkthrough
![GIF](/ezgif-3b6866bda12418.gif)
