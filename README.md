# CrySky: Weather Forecast Application

## Project Brief Description

CrySky is an Android mobile application designed to provide users with real-time weather status and temperature information based on their location. Users can also select specific locations on a map or use autocomplete text to search for locations, adding them to a list of favorite places for detailed weather updates. Additionally, the app allows users to set weather alerts for various conditions such as rain, wind, extreme temperatures, fog, and snow.

## Project’s Screens

### Settings Screen

The Settings screen allows users to configure preferences such as:
- Location selection (GPS or map)
- Temperature units (Kelvin, Celsius, Fahrenheit)
- Wind speed units (meter/sec, miles/hour)
- Language (Arabic, English)

### Home Screen

The Home screen displays:
- Current temperature
- Current date and time
- Humidity
- Wind speed
- Pressure
- Clouds
- City name
- Weather icon (representing weather status)
- Weather description (e.g., clear sky, light rain)
- Hourly forecast for the current date
- 5-day forecast

### Weather Alerts Screen

The Weather Alerts screen allows users to set weather alerts with customizable options:
- Duration of alarm activity
- Type of alarm (notification or default alarm sound)
- Option to stop notification or turn off the alarm

### Favorite Screen

The Favorite screen lists the user's favorite locations, providing access to detailed forecast information for each place. Users can:
- Add a new favorite place using the FAB button, which opens a screen for location selection.
- Remove saved locations from the list.

*Note: The app utilizes the Weather API from [OpenWeatherMap](https://api.openweathermap.org/data/2.5/forecast). Please refer to the API documentation for integration details.*
