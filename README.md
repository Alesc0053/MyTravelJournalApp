# TravelJournal App

A travel logging app that allows users to save locations, view real-time weather data, and display locations on an interactive map. Built using **Room Database, Retrofit, Moshi, and Mapbox API**, it provides an efficient and user-friendly experience.

## Features

✅ **User Authentication** – Secure login with data stored locally using Room.  
✅ **Location Logging** – Save visited locations with details like notes, date, business type, and enjoyment level.  
✅ **Weather Integration** – Fetch real-time weather data using **OpenWeather API**.  
✅ **Mapbox Integration** – Auto-completes locations and displays them on a map.  
✅ **Favorites System** – Mark and filter favorite locations for quick access.  
✅ **RecyclerView Optimization** – Efficiently handles large lists using **RecyclerView**.  
✅ **MVVM Architecture** – Ensures separation of concerns and clean code structure.  

## Tech Stack

- **Kotlin** – Primary programming language  
- **Room Database** – Local data persistence  
- **Retrofit + Moshi** – API calls and JSON parsing  
- **Mapbox API** – Location search and mapping  
- **Coroutines** – Background operations for smooth performance  
- **RecyclerView** – Optimized list handling  

## Installation

1. Clone the repository:  
   ```bash
   git clone https://github.com/yourusername/TravelJournal.git
2. Open the project in Android Studio.
3. Add your API keys for OpenWeather and Mapbox in strings.xml:
   ```bash
   <string name="openweather_api_key">YOUR_API_KEY</string>
   <string name="mapbox_access_token">YOUR_ACCESS_TOKEN</string>
5. Build and run the app on an Android emulator or device.
