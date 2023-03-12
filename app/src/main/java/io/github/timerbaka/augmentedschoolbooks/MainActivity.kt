package io.github.timerbaka.augmentedschoolbooks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position

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
        modelNode = ArModelNode(model.placementMode).apply {
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
