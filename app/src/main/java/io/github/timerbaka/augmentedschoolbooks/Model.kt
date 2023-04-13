package io.github.timerbaka.augmentedschoolbooks

import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Rotation

/**
 * Класс, представляющий 3D-модель, включающий в себя ее местоположение, масштаб и поведение при размещении.
 *
 * @param fileLocation Путь к файлу модели.
 * @param scaleUnits Единица измерения масштаба модели.
 * @param placementMode Режим размещения модели.
 * @param applyPoseRotation Показывает, применять ли поворот к положению модели.
 */
data class Model(
    val fileLocation: String,
    val scaleUnits: Float? = null,
    val placementMode: PlacementMode = PlacementMode.BEST_AVAILABLE,
    val applyPoseRotation: Boolean = true,
    val rotation: Rotation = Rotation(0F, 0F, 0F)
)