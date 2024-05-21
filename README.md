# Distance Tracker App

This project is aimed at creating a Distance Tracker App using the Google Maps SDK for Android. The app tracks the user's location in the background and calculates the distance traveled.
It provides a real-time map interface and continuously updates the user's route and distance covered.

## Features

- **Real-time Location Tracking**: Continuously tracks the user's location in the background.
- **Distance Calculation**: Calculates the total distance traveled by the user.
- **Map Interface**: Displays the user's current location and route on a Google Map.
- **User-Friendly UI**: Simple and intuitive interface for ease of use.

## Getting Started

### Prerequisites

- Android Studio
- Google Maps API Key

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/unon4all/distance-tracker-app.git
   ```

2. **Open the project in Android Studio:**
   - Open Android Studio.
   - Select "Open an existing Android Studio project".
   - Navigate to the cloned repository and select it.

3. **Obtain a Google Maps API Key:**
   - Follow the instructions on the [Google Cloud Console](https://console.cloud.google.com/) to obtain an API key.
   - Enable the Maps SDK for Android on your project.

4. **Add your API Key to the project:**
   - Open `local.properties` file in the project root.
   - Add your API key:
     ```properties
     MAPS_API_KEY=YOUR_API_KEY
     ```
   - Note: A demo key is already added in the project, but it is better to create your own for personalization.

### Building and Running the App

1. **Build the project:**
   - Click on the "Build" menu and select "Make Project".

2. **Run the app:**
   - Connect your Android device or start an emulator.
   - Click on the "Run" menu and select "Run 'app'".

## Usage

- **Start Tracking**: Open the app and click on the "Start" button to begin tracking your location.
- **Stop Tracking**: Click on the "Stop" button to stop tracking.
- **View Distance**: The app will display the total distance traveled on the screen.

## Technologies Used

- **Google Maps SDK for Android**: For displaying maps and user's location.
- **Android Location Services**: For accessing the user's location in the background.
- **Java/Kotlin**: For Android app development.

## Contributing

Feel free to fork this repository and contribute by submitting a pull request. Any improvements or bug fixes are welcome!

## Acknowledgements

Special thanks to the resources and documentation provided by Google Developers for the Maps SDK and Android development.
Thanks to [Stephen Jovanovic](https://github.com/stevdza-san/) for his insightful guidance and tutorials.

---

For any queries or feedback, please contact me at [namangr8y@gmail.com](namangr8y@gmail.com). 

Happy tracking! 🚶‍♂️📍

---

**Note**: This README is created as part of my personal learning journey in implementing Google Maps and developing Android apps. It is inspired by various resources and practical applications.

---

*This project is a work in progress. Future updates will include additional features such as saving tracking history and user authentication.*
