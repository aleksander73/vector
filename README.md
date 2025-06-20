# Vector

Vector is an open source 3d game engine framework for Android written in Java and OpenGL ES.

## Features
* rendering engine written in OpenGL
* support for linear algebra, i.e.
  * vectors
  * matrices
  * quaternions
* built-in MVP matrix transformations
  * model matrices
  * view matrices
  * projection matrices
* customizable camera
* support for user-created GLSL shaders
* GUI elements generation
* support for component-based and scripted game objects
* support for animations
  * value animations
* physics system
  * collision detection
  * gravity simulation
* resource system
  * mesh loader (custom formatted, example below)
  * texture loader
  * shader loader
  * audio player
* timers

## Usage
The game engine is available as a JitPack package. The following is the instruction for adding dependencies to your Android Studio project.

1. Add the JitPack repository to project build file, i.e. add it in the root `build.gradle` at the end of repositories
    ```
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
    ```
2. Add the dependencies
    ```
    dependencies {
        implementation 'com.github.aleksander73:vector:v1.1.2'
        implementation 'com.github.aleksander73:math-library-android:v1.0'
    }
    ```

3. Synchronize Gradle files in Android Studio, i.e. click `File` > `Sync Project with Gradle Files`

## Hello Vector

1. Open `Android Studio`
2. Create your game instance by extending `Game` class
3. Initialize `MainActivity.java` as such

    ```java
    public class MainActivity extends AppCompatActivity {
        private final GameEngine gameEngine = new GameEngine();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            gameEngine.getOnInitialized().queueRunnable(new Runnable() {
                @Override
                public void run() {
                    Game myGame = new MyGame();
                    gameEngine.startGame(myGame);
                }
            });
            gameEngine.initialize(this);
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            gameEngine.shutdown();
            System.exit(0);
        }
    }
    ```

## Mesh file example

* *rectangle.mesh*
  ```
  v -0.5f -1.0f 0.0f
  v -0.5f 1.0f 0.0f
  v 0.5f 1.0f 0.0f
  v 0.5f -1.0f 0.0f
  f 1 2 3
  f 1 3 4
  tc 1 0.0f 1.0f
  tc 2 0.0f 0.0f
  tc 3 1.0f 0.0f
  tc 4 1.0f 1.0f
  ```
* Legend
  * `v <x> <y> <z>` - vertex
  * `f <vertex-index> <vertex-index> <vertex-index>` - face
  * `tc <vertex-index> <x> <y>` - texture coordinate
