package io.github.timerbaka.augmentedschoolbooks

import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position
import io.github.sceneview.model.ModelInstance

/**
 * Расширение [ArModelNode], которое автоматически запускает все анимации в
 * [ModelInstance], если они доступны, когда новый экземпляр модели загружен в узел.
 */
class AnimatedModelNode(
    placementMode: PlacementMode = DEFAULT_PLACEMENT_MODE, // Режим размещения узла
    hitPosition: Position = DEFAULT_HIT_POSITION, // Позиция модели.
    followHitPosition: Boolean = true, // Разрешено ли следовать за позицией.
    instantAnchor: Boolean = false // Мгновенное крепление.
) : ArModelNode(placementMode, hitPosition, followHitPosition, instantAnchor) {

    /**
     * Устанавливает новый экземпляр модели в сцену и запускает все анимации,
     * если в модели их больше одной.
     *
     * @param modelInstance Новый экземпляр модели для загрузки в узел.
     * @param autoAnimate Запустить ли анимации в новой модели автоматически.
     * @param scaleToUnits Масштабировать модель до указанного размера.
     * @param centerOrigin Выровнять модель относительно указанной точки.
     */
    override fun setModelInstance(
        modelInstance: ModelInstance?,
        autoAnimate: Boolean,
        scaleToUnits: Float?,
        centerOrigin: Position?
    ) {
        // Вызов родительского метода
        super.setModelInstance(modelInstance, autoAnimate, scaleToUnits, centerOrigin)
        // Если количество анимаций больше одной
        if (autoAnimate && (modelInstance?.animator?.animationCount ?: 0) > 1) {
            // Остановить текущую анимацию перед запуском новых.
            stopAnimation(0)
            // Запустить все анимации последовательно.
            repeat(modelInstance?.animator?.animationCount ?: 0) { index ->
                playAnimation(index)
            }
        }
    }
}
