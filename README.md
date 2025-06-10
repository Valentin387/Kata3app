# Kata3App

## Project Overview

Kata3App is an Android application built with Kotlin, following the MVVM architecture, that provides a user-friendly interface for managing projects and tasks. Users can register, log in, and interact with items (projects or tasks) through a RESTful API. The app features a Navigation Drawer for easy access to "Projects," "Tasks," and "Sign Out" options, with fragments for listing, creating, viewing, and editing items. It uses secure token storage and modern Android development practices.

## Features

* **User Authentication**: Register and log in via OAuth2 with `/auth/register` and `/auth/login` endpoints.
* **Navigation Drawer**: Access "Projects," "Tasks," and "Sign Out" seamlessly.
* **Item Management**:

  * List items filtered by type (Project/Task) using `GET /items?type={project|task}`.
  * Create new items with `POST /items`.
  * View and edit item details with `GET /items/{id}` and `PATCH /items/{id}`.
  * Delete items with `DELETE /items/{id}`.
* **Secure Storage**: JWT tokens are stored using `EncryptedPrefsManager`.
* **UI Feedback**: Toast messages for success and error states, progress indicators for API calls.
* **Responsive Design**: XML layouts with ViewBinding and Navigation Component for smooth transitions.

## Project Metadata

The Android project is configured with the following settings in `build.gradle`:

```kotlin
android {
  namespace = "com.kata3.kata3app"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.kata3.kata3app"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
}
```

## Secret Management

### Create `secrets.properties`

If the `secrets.properties` file does not exist, create it in the root project directory.

```properties
BASE_URL=http://your-api-url
```

### Example for local development:

```properties
BASE_URL=http://localhost:8080
```

### Default Properties:

Create `local.defaults.properties` with fallback values:

```properties
BASE_URL=http://example.com
```

### Configuration in `build.gradle`:

```kotlin
secrets {
  propertiesFileName = "secrets.properties"
  defaultPropertiesFileName = "local.defaults.properties"
}
```

### Accessing Secrets:

The app accesses `BASE_URL` via `BuildConfig.BASE_URL`, automatically generated.

## Prerequisites

* Android Studio: Latest stable version (e.g., Koala or later).
* JDK: Version 17 or higher.
* Emulator/Device: API level 24 (Android 7.0) or higher.
* API Server: A running backend server supporting `/auth` and `/items` endpoints.

## Setup Instructions

```bash
git clone https://github.com/your-repo/kata3app.git
cd kata3app
```

### Configure Secrets:

* Create or update `secrets.properties` in the project root with your `BASE_URL`.
* Ensure `local.defaults.properties` exists for fallback values.

### Sync Project:

* Open the project in Android Studio.
* Sync the project with Gradle.

### Add Drawables:

Replace placeholder drawables in `res/drawable` with actual icons or use Android Studio’s vector asset tool.

### Build and Run:

* Select an emulator or connected device (API 24+).
* Click Run in Android Studio.

## Usage Instructions

### Launch the App:

* If no JWT token is stored, the app opens `LoginActivity`.
* If a token exists, it navigates to `MainActivity`.

### Register/Login:

* In `LoginActivity`, click "Sign Up" to navigate to `SignupActivity`.
* Register with username and password (min 8 characters).
* Log in to access `MainActivity`.

### Navigate the App:

* Use Navigation Drawer to select "Projects" or "Tasks" → loads `HomeFragment` with filtered items.
* Click "Add New Item" → opens `NewItemFragment`.
* Tap an item → opens `DetailsFragment`.
* Edit → triggers `PATCH /items/{id}`.
* Delete → triggers `DELETE /items/{id}`.
* Sign Out → clears token, navigates to `LoginActivity`.

### API Interaction:

Ensure backend server is available at configured `BASE_URL`. Use tools like Insomnia:

```http
POST /auth/register
{
  "username": "user",
  "password": "password123"
}

POST /auth/login
{
  "username": "user",
  "password": "password123"
}

GET /items?type=project
[
  {
    "id": "1",
    "name": "Test Project",
    "type": "PROJECT",
    "description": "",
    "created_at": "2025-06-10"
  }
]

POST /items
{
  "name": "New Task",
  "type": "TASK",
  "description": "Details"
}

GET /items/{id}
PATCH /items/{id}
DELETE /items/{id}
```

### Error Handling:

* API or network errors trigger toast: `"There was an error"`.
* Invalid inputs (e.g., login) show meaningful messages.

## Dependencies

```kotlin
implementation "androidx.navigation:navigation-fragment-ktx:2.7.7"
implementation "androidx.navigation:navigation-ui-ktx:2.7.7"
implementation "com.google.android.material:material:1.12.0"
implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0"
implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.8.0"
implementation "androidx.recyclerview:recyclerview:1.3.2"
implementation "androidx.security:security-crypto:1.1.0-alpha06"
implementation "com.squareup.retrofit2:retrofit:2.9.0"
implementation "com.squareup.retrofit2:converter-gson:2.9.0"
implementation "com.google.code.gson:gson:2.10.1"
```

## Folder Structure

```
kata3app/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com.kata3.kata3app/
│   │   │   │       ├── data/
│   │   │   │       │   ├── DTO/
│   │   │   │       │   │   ├── ItemDTO.kt
│   │   │   │       │   │   ├── SignInRequest.kt
│   │   │   │       │   │   └── SignUpRequest.kt
│   │   │   │       │   └── repositories/
│   │   │   │       │       ├── AuthRepository.kt
│   │   │   │       │       └── ItemRepository.kt
│   │   │   │       ├── io/
│   │   │   │       │   ├── AuthService.kt
│   │   │   │       │   └── ItemService.kt
│   │   │   │       ├── ui/
│   │   │   │       │   ├── login/
│   │   │   │       │   │   ├── LoginActivity.kt
│   │   │   │       │   │   └── LoginViewModel.kt
│   │   │   │       │   ├── main/
│   │   │   │       │   │   ├── MainActivity.kt
│   │   │   │       │   │   ├── details/
│   │   │   │       │   │   │   ├── DetailsFragment.kt
│   │   │   │       │   │   │   └── DetailsViewModel.kt
│   │   │   │       │   │   ├── home/
│   │   │   │       │   │   │   ├── HomeFragment.kt
│   │   │   │       │   │   │   ├── HomeViewModel.kt
│   │   │   │       │   │   │   └── ItemAdapter.kt
│   │   │   │       │   │   └── newItem/
│   │   │   │       │   │       ├── NewItemFragment.kt
│   │   │   │       │   │       └── NewItemViewModel.kt
│   │   │   │       │   └── signup/
│   │   │   │       │       ├── SignupActivity.kt
│   │   │   │       │       └── SignupViewModel.kt
│   │   │   │       └── utils/
│   │   │   │           └── EncryptedPrefsManager.kt
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   │   ├── ic_menu_projects.xml
│   │   │   │   │   ├── ic_menu_sign_out.xml
│   │   │   │   │   └── ic_menu_tasks.xml
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_login.xml
│   │   │   │   │   ├── activity_main.xml
│   │   │   │   │   ├── activity_signup.xml
│   │   │   │   │   ├── app_bar_main.xml
│   │   │   │   │   ├── fragment_details.xml
│   │   │   │   │   ├── fragment_home.xml
│   │   │   │   │   ├── fragment_new_item.xml
│   │   │   │   │   ├── item_layout.xml
│   │   │   │   │   └── nav_header_main.xml
│   │   │   │   ├── menu/
│   │   │   │   │   ├── activity_main_drawer.xml
│   │   │   │   │   └── main.xml
│   │   │   │   ├── navigation/
│   │   │   │   │   └── nav_graph.xml
│   │   │   │   └── values/
│   │   │   │       └── strings.xml
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   │       └── java/
│   │           └── com.kata3.kata3app/
│   ├── build.gradle
│   ├── local.defaults.properties
│   └── secrets.properties
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── build.gradle
├── gradle.properties
├── gradlew
├── gradlew.bat
└── settings.gradle
```

## Testing

### Unit Tests:

```bash
./gradlew test
```

Located in `app/src/test/java`.

### Instrumentation Tests:

```bash
./gradlew connectedAndroidTest
```

Located in `app/src/androidTest/java`.

### Manual Testing:

* Register/login with valid/invalid credentials.
* Create/edit/delete/list items.
* Use drawer menu for navigation.
* Simulate API errors via Insomnia.

## Known Issues

* Placeholder drawables must be replaced.
* Error messages are generic; refine if backend allows.
* No offline support.

## Future Improvements

* Add offline caching (Room).
* Implement pull-to-refresh.
* Enhance UI (icons, animations).
* Add more fragment/adapters unit tests.

## Contributing

1. Fork the repo.
2. Create a branch: `git checkout -b feature/your-feature`.
3. Commit changes: `git commit -m "Add your feature"`.
4. Push: `git push origin feature/your-feature`.
5. Open a Pull Request.

## License

This project is licensed under the MIT License.

## Contact

For issues or questions, contact the project maintainer at \[[your-email@example.com](mailto:your-email@example.com)].
