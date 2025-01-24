# БуквARик
![logo_with_text.png](contrib/img/logo_with_text.png)

BukvARik is a sample application demonstrating the capabilities of augmented reality (AR) on Android. The app allows loading and displaying 3D models in AR using physical objects as markers.

## Device Requirements
The application requires a device running Android 7.0 or higher with ARCore support. To check if your device supports ARCore, visit the [ARCore supported devices page](https://developers.google.com/ar/discover/supported-devices).

## Libraries and Tools Used
The application utilizes the following libraries and tools:

- [SceneView](https://sceneview.github.io/) - A library for working with 3D scenes on Android.
- [ARCore](https://developers.google.com/ar) - Google's augmented reality platform for Android.
- [Material Design Components](https://m3.material.io/components) - A UI component library following Google's Material Design guidelines.

## Installation and Running the Application
- Clone the repository to your local machine:
    ```bash
    git clone https://github.com/timerbaka/augmentedschoolbooks.git
    ```
- Open the project in Android Studio or IntelliJ IDEA.
- Connect your Android device to the computer and ensure USB debugging is enabled.
- Run the project on your device using the "Run" button in the IDE.

## How to Use the Application
After launching the application, grant camera access so the app can use it for AR functionality.  
Once ARCore and SceneView are initialized, the screen will switch to the device's camera view. The app automatically scans for augmented reality images with templates from the `images` folder and displays corresponding 3D models.

The application can also work with custom augmented reality images in PNG format located in the `images` folder. To use a custom image:
1. Add the new image to the `images` folder.
2. Add a corresponding 3D model in [GLB format](https://en.wikipedia.org/wiki/GlTF) to the `models` folder.
3. Add the new image to the AugmentedImageDatabase, using its name as the key.
4. Specify the new model in the `models` list within `MainActivity.kt`, using the key name as the first argument.

The application supports automatic model animations as well as manual control for rotating and scaling the model.

After selecting a marker, the model loading process will begin, which may take some time. Once loaded, the model will be placed at the marker's location in AR. Users can interact with the model by moving and rotating it in space using gestures.

## License
BukvARik is distributed under the MIT License. For more details, refer to the LICENSE file.

## Authors
BukvARik was developed by Timur Saifiev in 2023. For any questions, feel free to contact me at timerbaka@gmail.com.