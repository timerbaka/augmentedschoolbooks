package io.github.timerbaka.augmentedschoolbooks

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.math.Position

/**
 * Класс ModelManager отвечает за управление моделями в AR-сцене.
 * Обрабатывает загрузку, удаление, создание и добавление узлов моделей.
 *
 * @param activity Экземпляр активности, в которой используется ModelManager.
 * @param sceneView Экземпляр ArSceneView для управления моделями в сцене.
 * @param loadingView Экземпляр View, отображающийся во время загрузки модели.
 */
class ModelManager(private val activity: AppCompatActivity, private val sceneView: ArSceneView, private val loadingView: View) {

    // Объявление переменной, показывающей, показывается ли интерфейс загрузки модели
    private var isLoading = false
        set(value) {
            field = value
            loadingView.isGone = !value
        }

    private var modelNode: AnimatedModelNode? = null

    // Устанавливает значение переменной isLoading и обновляет интерфейс загрузки
    private fun setModelLoading(isLoading: Boolean) {
        // Установка значения isLoading и обновление интерфейса загрузки
        this.isLoading = isLoading
    }

    // Удаление текущего узла модели, если он не привязан к якорю
    private fun removeCurrentModelNode() {
        modelNode?.takeIf { !it.isAnchored }?.let {
            // Удаление узла модели из сцены
            sceneView.removeChild(it)
            // Уничтожение узла модели
            it.destroy()
        }
    }

    // Создание нового узла модели на основе переданной модели
    private fun createNewModelNode(model: Model): AnimatedModelNode {
        return AnimatedModelNode(model.placementMode).apply {
            applyPoseRotation = model.applyPoseRotation
            // Асинхронная загрузка модели GLB
            loadModelGlbAsync(
                glbFileLocation = model.fileLocation,
                autoAnimate = true,
                scaleToUnits = model.scaleUnits,
                // Помещение точки начала модели в центр нижней части модели
                centerOrigin = Position(y = -1.0f)
            ) {
                // После загрузки модели сделать видимым рендерер плоскости
                sceneView.planeRenderer.isVisible = true
                // После загрузки модели скрыть интерфейс загрузки
                setModelLoading(false)
            }
        }
    }

    // Добавление нового узла модели в сцену и установка его как выбранного
    private fun addAndSelectModelNode(newModelNode: AnimatedModelNode) {
        // Добавление нового узла модели в сцену
        sceneView.addChild(newModelNode)
        // Установка нового узла модели выбранным по умолчанию
        sceneView.selectedNode = newModelNode
    }

    // Создание нового узла модели, добавление его в сцену и установка его как выбранного
    fun newModelNode(model: Model) {
        // Установка значения isLoading в true и обновление интерфейса загрузки
        setModelLoading(true)
        // Удаление текущего узла модели, если он не привязан к якорю
        removeCurrentModelNode()
        // Создание нового узла модели на основе переданной модели
        modelNode = createNewModelNode(model)
        // Добавление нового узла модели в сцену и установка его как выбранного
        addAndSelectModelNode(modelNode!!)
        // Скрытие интерфейса загрузки после загрузки модели
        setModelLoading(false)
    }

}
