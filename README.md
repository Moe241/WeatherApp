# WeatherApp

WeatherApp is a simple and intuitive weather application built using Kotlin and Jetpack Compose. This app provides current weather information for any location, utilizing the OpenWeather API. The app displays weather conditions, temperature, humidity, wind speed, and other relevant data.

## Features

- Displays current weather conditions (temperature, humidity, wind speed, etc.)
- Search for weather in different cities or locations
- Refresh weather data with real-time updates
- Clean and modern UI built with Jetpack Compose
- Lightweight and efficient

## Screenshots
![App Screenshot](path/to/screenshot.png) <!-- Update with actual screenshots -->

## Built With

- [Kotlin](https://kotlinlang.org/) - The programming language used for Android app development.
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern Android UI toolkit for building native interfaces.
- [OpenWeather API](https://openweathermap.org/api) - Provides the weather data used in the app.

## Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

- Android Studio (with Kotlin support)
- OpenWeather API key (sign up at [OpenWeather](https://home.openweathermap.org/users/sign_up) for free)

### Installation

1. Clone the repo:
   ```bash
   git clone https://github.com/yourusername/WeatherApp.git
   ```

2. Open the project in Android Studio.

3. Obtain your OpenWeather API key by signing up at [OpenWeather](https://home.openweathermap.org/users/sign_up).

4. In your `local.properties` file (located in the root folder), add your API key as follows:
   ```properties
   OPEN_WEATHER_API_KEY=your_api_key_here
   ```

5. Build and run the project on an Android device or emulator.

## Usage

Once the app is installed, enter the name of the city or location you want to check the weather for in the search bar. The app will display the current weather details based on your input.

## Code Structure

- `MainActivity.kt` - The entry point of the application.
- `WeatherViewModel.kt` - ViewModel to handle business logic and interact with the API.
- `WeatherRepository.kt` - Contains the logic to fetch data from the OpenWeather API.
- `WeatherScreen.kt` - Compose UI component that displays the weather data.

## API Integration

This app uses the OpenWeather API to fetch weather data. You can explore more about this API [here](https://openweathermap.org/api).

### Example API Request

An example request URL used to fetch the weather data:
```
https://api.openweathermap.org/data/2.5/weather?q={city_name}&appid={API_KEY}
```

### Sample Response
```json
{
  "weather": [
    {
      "description": "clear sky"
    }
  ],
  "main": {
    "temp": 293.25,
    "humidity": 56
  },
  "wind": {
    "speed": 3.6
  }
}
```

## Dependencies

- [Retrofit](https://square.github.io/retrofit/) - For making network requests to the OpenWeather API.
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - For building the UI components.
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - For dependency injection.
- [Coroutines](https://developer.android.com/kotlin/coroutines) - For asynchronous programming.

## Contributing

Contributions are welcome! Feel free to submit a pull request or open an issue.

- **API Integration**: Provides context about the OpenWeather API and how it's integrated.
- **Contributing** and **License**: Guidelines for contributing to the project and legal information. 

Let me know if you need any more customization!
