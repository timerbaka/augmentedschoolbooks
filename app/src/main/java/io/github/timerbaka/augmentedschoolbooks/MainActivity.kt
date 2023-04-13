package io.github.timerbaka.augmentedschoolbooks

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Rotation
import io.github.sceneview.utils.setFullScreen

/**
 * Основной класс приложения, управляющий сценой ARCore и моделями.
 */
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    // Карта ключей-значений, содержащая 3D-модели
    private val models: Map<String, Model> = mapOf(
        "gravity_law" to Model("models/gravity_law.glb", scaleUnits = 0.5f, rotation = Rotation(0f,270f,0f), placementMode = PlacementMode.INSTANT),
    )
    // Объявление переменных для хранения View
    private lateinit var sceneView: ArSceneView
    private lateinit var loadingView: View

    // Экземпляр ModelManager для управления моделями в AR-сцене.
    private lateinit var modelManager: ModelManager

    /**
     * Метод жизненного цикла, вызываемый при создании активности.
     * Настраивает полноэкранный режим, настраивает сцену ARCore, загружает модели и добавляет обработчики событий.
     * @param savedInstanceState Сохраненное состояние активности.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Настройка полноэкранного режима
        setupFullScreen()

        // Настройка сцены ARCore
        setupARCoreScene()

        // Инициализация ModelManager
        modelManager = ModelManager(this, sceneView, loadingView)

        // Настройка обработчика событий обновления списка обнаруженных дополненных изображений
        setupAugmentedImageUpdateHandler()
    }

    /**
     * Настройка полноэкранного режима для приложения.
     * Установка параметров полноэкранного режима и системных панелей.
     */
    private fun setupFullScreen() {
        setFullScreen(
            findViewById(R.id.rootView),
            fullScreen = true,
            hideSystemBars = false,
            fitsSystemWindows = false
        )
    }

    /**
     * Настраивает сцену ARCore и конфигурацию сессии.
     * Создает базу данных дополненных изображений и настраивает параметры сессии.
     */
    private fun setupARCoreScene() {
        // Инициализация сцены ARCore и виджета для отображения загрузки
        sceneView = findViewById(R.id.sceneView)
        loadingView = findViewById(R.id.loadingView)

        // Конфигурация сессии ARCore
        sceneView.configureSession { arSession, config ->
            // Создание базы данных дополненных изображений
            val imgDb = AugmentedImageDatabase(arSession)
            // Заполнение базы данных дополненных изображений
            models.keys.forEach {
                imgDb.addImage(it, BitmapFactory.decodeStream(assets.open("images/$it.png")))
             }
            config.augmentedImageDatabase = imgDb

            // Отключение глубины
            config.depthMode = Config.DepthMode.DISABLED

            // Применение конфигурации сессии
            arSession.configure(config)
        }
    }

    /**
     * Настройка обработчика событий по обновлению списка обнаруженных дополненных изображений.
     * Создает новый узел модели, когда обнаружено новое изображение.
     */
    private fun setupAugmentedImageUpdateHandler() {
        // Имя обнаруженного изображения
        var currentImage = ""

        // Установка обработчика событий обновления списка обнаруженных дополненных изображений
        sceneView.onAugmentedImageUpdate = mutableListOf(
            { augmentedImage ->
                // Если имя обнаруженного изображения отличается от текущего
                if (augmentedImage.name != currentImage) {
                    // Создание нового узла модели на основе соответствующей модели в списке models
                    models[augmentedImage.name]?.let { modelManager.newModelNode(it) }
                    // Обновление имени обнаруженного изображения
                    currentImage = augmentedImage.name
                }
            }
        )
    }
}
