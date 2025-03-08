# task-manager
# 📋 Interactive Task Manager with Jetpack Compose

## 🚀 Objective
This Android application is an **interactive task manager** built entirely with **Jetpack Compose**. It features a **visually appealing design, smooth animations, intuitive navigation, and accessibility support** while adhering to **Material Design 3** principles.

## 🛠️ Technologies Used
- **Android SDK 34/35**
- **Kotlin 2.x**
- **Jetpack Compose 1.6.x**
- **Room Database**
- **Material Design 3**
- **Navigation Component**

---

## 🎯 Features

### ✅ **Core Features**
- **Task Creation**:
    - Add tasks with a **title (required)**, **description (optional)**, **priority (Low, Medium, High)**, and **due date (date picker)**.
- **Task List**:
    - Sort tasks **by priority, due date, or alphabetically**.
    - Filter tasks by **status (All, Completed, Pending)**.
- **Task Details**:
    - View details, **mark as complete**, or **delete tasks**.
- **Persistent Storage**:
    - Uses **Room Database** to **store tasks** and **persist data** across app restarts.

### 🎨 **UI/UX Design**
- **Jetpack Compose (1.6.x) + Material Design 3**
- **Light & Dark Mode** (Material You, Dynamic Theming).
- **Adaptive Layout** (supports phones & tablets).
- **Smooth Animations**:
    - **Slide-in/Slide-out** for adding/removing tasks.
    - **Circular reveal** for opening task details.
    - **Subtle bounce** effect for **FAB taps**.
- **Navigation**:
    - **Home Screen (Task List)**
    - **Task Creation Screen**
    - **Task Details Screen**
    - **Settings Screen**

### 🚀 **Advanced UI Features**
- **Drag-and-Drop**: Reorder tasks with haptic feedback. - TODO 
- **Swipe Gestures**:
    - **Swipe left** to delete a task (**Undo Snackbar**).
    - **Swipe right** to mark a task as complete.
- **Custom Progress Indicator**:
    - A **circular animated progress bar** shows completed tasks.
- **Empty State UI**:
    - Displays an **illustration + motivational message** when no tasks exist.

### ♿ **Accessibility**
- **Screen Reader Support** (content descriptions for UI elements).
- **Large Text Scaling & High-Contrast Mode**.
- **Keyboard Navigation** for **task creation & list interactions**.

### ⚡ **Performance & Optimization**
- Uses **LazyColumn** for smooth scrolling with **100+ tasks**.
- Optimized **recomposition** using `remember` & `LaunchedEffect`.
- **Shimmer Effect** while loading data from Room.

### 🛠 **Testing**
- **UI Tests (Compose Testing)**
    - ✅ Task creation flow
    - ✅ Sorting & filtering validation
    - ✅ Animation verification
- **Screenshot Tests (Robolectric)**
    - ✅ Validate UI across **light & dark modes** - TODO (Manually Tested with Settings)

---

## 🛠 **Setup & Installation**

### 📌 **Prerequisites**
- **Android Studio Giraffe | Hedgehog (2025) or newer**
- **JDK 17+**
- **Kotlin 2.x**
- **Gradle 8.x**
- **Android SDK 34/35**

### 🔧 **Installation Steps**

1️⃣ **Clone the Repository**
```sh
git clone https://github.com/svalanedwin/task-manager.git
cd TaskManager

2️⃣ Open in Android Studio

Open Android Studio
Select "Open an Existing Project"
Choose the TaskManagerCompose folder
3️⃣ Run the App

Select a device (Emulator or Physical Android device)
Click ▶️ Run

🔨 Build Generation & Testing
🔧 Generate Debug Build (APK)

To build a debug APK, run:
./gradlew assembleDebug

The generated APK will be in:
app/build/outputs/apk/debug/app-debug.apk

Run UI Tests
./gradlew test


🏗 Project Structure
📂 TaskManagerCompose
 ┣ 📂 app/src/main
 ┃ ┣ 📂 java/com/example/taskmanager
 ┃ ┃ ┣ 📂 ui         # Jetpack Compose UI Components
 ┃ ┃ ┣ 📂 model      # Data models (Task.kt)
 ┃ ┃ ┣ 📂 repository # Room Database Repository
 ┃ ┃ ┣ 📂 viewmodel  # ViewModels for state management
 ┃ ┃ ┗ 📂 navigation # Navigation graph
 ┃ ┣ 📂 res         # Resources (drawables, themes, etc.)
 ┃ ┗ AndroidManifest.xml

🔮 Design Rationale
✨ Why Jetpack Compose?
Declarative UI → Eliminates XML layouts, making UI code cleaner & scalable.
State Management → Uses ViewModel + Compose’s remember API for efficient recomposition.
Material Design 3 Support → Ensures modern UI consistency across Android devices.
🎭 Why Room Database?
Ensures data persistence for tasks, even after app restarts.
Uses LiveData/Flow to automatically update UI when the database changes.
🔗 Why Compose Navigation?
Provides seamless, type-safe navigation without requiring Fragments.
Supports animations & transitions natively.

🏆 Future Enhancements
🔹 Cloud Sync (Firebase/GraphQL) – Sync tasks across multiple devices.
🔹 Notifications – Reminders for upcoming due dates.
🔹 Collaborative Task Sharing – Assign tasks to friends/family.
🔹 Voice Input – Add tasks via voice commands.
