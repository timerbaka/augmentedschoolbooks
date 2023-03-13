package io.github.timerbaka.augmentedschoolbooks

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position
import io.github.sceneview.utils.setFullScreen

/**
 * Основной класс приложения, управляющий сценой ARCore и моделями.
 */
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    // Карта ключей-значений, содержащая 3D-модели
    private val models: Map<String, Model> = mapOf(
        "spheres" to Model("models/spheres.glb", scaleUnits = 0.5f, placementMode = PlacementMode.INSTANT),
    )
    // Объявление переменных для хранения View
    private lateinit var sceneView: ArSceneView
    private lateinit var loadingView: View

    // Объявление переменной для хранения текущего узла модели
    private var modelNode: ArModelNode? = null

    // Объявление переменной, показывающей, показывается ли интерфейс загрузки модели
    private var isLoading = false
        set(value) {
            field = value
            loadingView.isGone = !value
        }

    /**
     * Создает новый узел модели на основе переданной модели и добавляет его в сцену.
     * Удаляет текущий узел модели, если он не привязан к якорю.
     * @param model Модель, на основе которой создается новый узел модели.
     */
    private fun newModelNode(model: Model) {
        // Установка isLoading в true, чтобы отобразить интерфейс загрузки модели
        isLoading = true
        // Удаление текущего узла модели, если он не привязан к якорю
        modelNode?.takeIf { !it.isAnchored }?.let {
            sceneView.removeChild(it)
            it.destroy()
        }
        // Создание нового узла модели на основе переданной модели
        modelNode = AnimatedModelNode(model.placementMode).apply {
            applyPoseRotation = model.applyPoseRotation
            loadModelGlbAsync(
                context = this@MainActivity,
                glbFileLocation = model.fileLocation,
                autoAnimate = true,
                scaleToUnits = model.scaleUnits,
                // Помещение точки начала модели в центр нижней части модели
                centerOrigin = Position(y = -1.0f)
            ) {
                sceneView.planeRenderer.isVisible = true
                // После загрузки модели скрыть интерфейс загрузки
                isLoading = false
            }
        }
        // Добавление нового узла модели в сцену
        sceneView.addChild(modelNode!!)
        // Установка нового узла модели выбранным по умолчанию
        sceneView.selectedNode = modelNode
    }
    /**
     * Метод жизненного цикла, вызываемый при создании активности.
     * Настраивает полноэкранный режим, настраивает сцену ARCore, загружает модели и добавляет обработчики событий.
     * @param savedInstanceState Сохраненное состояние активности.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Настройка полноэкранного режима
        setFullScreen(
            findViewById(R.id.rootView),
            fullScreen = true,
            hideSystemBars = false,
            fitsSystemWindows = false
        )

        // Инициализация сцены ARCore
        sceneView = findViewById(R.id.sceneView)
        loadingView = findViewById(R.id.loadingView)
        // Конфигурация сессии
        sceneView.configureSession { arSession, config ->
            // Создание базы данных дополненных изображений
            val imgDb = AugmentedImageDatabase(arSession)
            imgDb.addImage("spheres", BitmapFactory.decodeStream(assets.open("images/spheres.png")))
            config.augmentedImageDatabase = imgDb
            // Отключение глубины
            config.depthMode = Config.DepthMode.DISABLED
            arSession.configure(config)
        }

        // Обработчик события обновления списка обнаруженных дополненных изображений
        var currentImage = ""
        sceneView.onAugmentedImageUpdate = mutableListOf(
            { augmentedImage ->
                if (augmentedImage.name != currentImage) {
                    // Создание нового узла модели на основе соответствующей модели в списке models
                    models[augmentedImage.name]?.let { newModelNode(it) }
                    currentImage = augmentedImage.name
                }
            }
        )
    }
}
