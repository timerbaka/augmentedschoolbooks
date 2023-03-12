package io.github.timerbaka.augmentedschoolbooks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.sceneview.ar.node.PlacementMode

/**
 * Основной класс приложения, управляющий сценой ARCore и моделями.
 */
class MainActivity : AppCompatActivity() {
    // Карта ключей-значений, содержащая 3D-модели
    private val models: Map<String, Model> = mapOf(
        "spheres" to Model("models/spheres.glb", scaleUnits = 0.5f, placementMode = PlacementMode.INSTANT),
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
