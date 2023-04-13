"""
Скрипт для Blender, который создает 3D-сцену с тремя сферами разных размеров и цветов
для иллюстрации закона всемирного тяготения.
Большая сфера остается неподвижной, в то время как средняя и малая сферы вращаются вокруг нее.
Скрипт создает анимацию вращения средней и малой сфер и экспортирует сцену в формат GLB.
"""

import bpy


#
# Объявление функций
#
def create_material(name, color):
    """
    Создает материал с заданным именем и цветом.

    :param name: Имя материала
    :param color: Цвет материала (R, G, B, A)
    :return: Созданный материал
    """
    # Создание материала
    material = bpy.data.materials.new(name)
    # Включение узлового редактора
    material.use_nodes = True
    # Установка цвета материала
    # Principled BSDF - это узел, который используется для отображения материала в 3D-представлении
    # Base Color - это цвет материала
    material.node_tree.nodes["Principled BSDF"].inputs["Base Color"].default_value = color

    return material


def create_uv_sphere(name, size, position, color, subsurf_level):
    """
    Создает сферу с заданными параметрами.

    Все размеры указаны в BU (Blender Unit, "единица Blender")
    "Единица Blender" - это стандартная единица измерения внутри программы Blender.
    Она представляет собой базовую единицу для определения размеров, расстояний и пропорций в сцене.
    Является абстрактной единицей измерения и не имеет прямого соответствия в физических единицах, как метры или футы.
    Она применяется для определения относительных размеров и расстояний между объектами в сцене.

    :param name: Имя сферы
    :param size: Размер сферы
    :param position: Положение сферы
    :param color: Цвет сферы
    :param subsurf_level: Уровень сглаживания сферы
    :return: Созданная сфера
    """
    # Создание сферы
    bpy.ops.mesh.primitive_uv_sphere_add()
    # Получение созданной сферы
    sphere = bpy.context.object
    # Установка параметров сферы
    sphere.name = name
    sphere.scale = (size, size, size)
    sphere.location = position

    # Добавление материала
    sphere.data.materials.append(create_material(name, color))
    # Сглаживание сферы
    subsurf_smoothing(sphere, subsurf_level)

    return sphere


def subsurf_smoothing(obj, level):
    """
    Применяет модификатор "Подразделение поверхности" (Subsurf) к объекту для сглаживания всех его полигонов.

    Аргументы:
    :param obj: bpy.types.Object, Объект для применения модификатора.
    :param level: int, Уровень подразделения поверхности для вида в портфолио и рендеринга.
    """
    # Создание модификатора "Подразделение поверхности" (Subsurf) для данного объекта
    modifier = obj.modifiers.new('Subsurf', 'SUBSURF')
    # Установка уровня сглаживания
    modifier.levels = level
    modifier.render_levels = level

    # Получение данных меша объекта
    data = obj.data
    # Цикл по полигонам меша
    for polygon in data.polygons:
        # Включение сглаживания для каждого полигона
        polygon.use_smooth = True


def set_sphere_pivot(obj, name, type='PLAIN_AXES'):
    """
    Устанавливает точку вращения объекта.

    Аргументы:
    :param name: str, Имя точки вращения.
    :param obj: bpy.types.Object, Объект для установки точки вращения.
    :param type: str, Тип точки вращения. Допустимые значения: 'PLAIN_AXES', 'BOUNDING_BOX_CENTER', 'CURSOR',
    'INDIVIDUAL_ORIGINS', 'MEDIAN_POINT', 'ACTIVE_ELEMENT'.

    :return: bpy.types.Object, Точка вращения объекта.
    """
    # Добавление точки вращения
    bpy.ops.object.empty_add(type=type)
    # Получение созданной точки вращения
    pivot = bpy.context.object
    pivot.name = name
    # Установка точки вращения объекта для данного объекта
    obj.parent = pivot

    return pivot


def export_glb(filepath):
    """
    Экспортирует сцену в формате glb.

    :param filepath: str, Путь к файлу
    """
    bpy.ops.export_scene.gltf(
        filepath=filepath,
        export_format='GLB',
        use_selection=False,
    )


#
# Очистка сцены
#

# Выбор всех объектов
bpy.ops.object.select_all(action='SELECT')
# Удаление всех объектов
bpy.ops.object.delete(use_global=False)

#
# Создание сфер
#

# Добавление большой сферы
central_sphere = create_uv_sphere(
    "Большая сфера", 1, (0, 0, 0), (0.604, 0.835, 0.965, 1), 2)

# Добавление средней сферы
medium_sphere = create_uv_sphere(
    "Средняя сфера", 0.5, (0, 3, 0), (0.518, 0.333, 0.180, 1), 2)

# Добавление малой сферы
small_sphere = create_uv_sphere(
    "Малая сфера", 0.25, (0, 5, 0), (0.215, 0.437, 0.600, 1), 2)

#
# Центры вращения сфер
#

# Центр вращения средней сферы
medium_pivot = set_sphere_pivot(medium_sphere, "Центр вращения средней сферы")

# Центр вращения малой сферы
small_pivot = set_sphere_pivot(small_sphere, "Центр вращения малой сферы")

#
# Анимация орбит
#

# Номер финального кадра анимации
end_frame = 250

# Цикл анимации для каждой сферы
for pivot, rotation_speed in [(medium_pivot, 1), (small_pivot, 2)]:
    # Устанавливаем текущий кадр на 1
    bpy.context.scene.frame_set(1)
    # Сбрасываем вращение якоря на начальное значение (0, 0, 0)
    pivot.rotation_euler = (0, 0, 0)
    # Вставляем ключевой кадр для начального вращения якоря
    pivot.keyframe_insert(data_path="rotation_euler", frame=1)

    # Устанавливаем текущий кадр на последний кадр анимации
    bpy.context.scene.frame_set(end_frame)
    # Задаем вращение якоря вокруг оси Z на конечный кадр анимации
    pivot.rotation_euler = (0, 0, rotation_speed * 2 * 3.14159)
    # Вставляем ключевой кадр для конечного вращения якоря
    pivot.keyframe_insert(data_path="rotation_euler", frame=end_frame)

    # Добавление модификатора "Циклы" (Cycles) для кривой вращения по оси Z
    # Это позволяет зациклить анимацию, чтобы она бесконечно повторялась
    fcurve = pivot.animation_data.action.fcurves.find(
        "rotation_euler", index=2)
    cycles_modifier = fcurve.modifiers.new(type='CYCLES')

# Установка конечного кадра для анимации
bpy.context.scene.frame_end = end_frame

#
# экспорт сцены в виде GLB-файла
#

# использовать только в консоли
# запускать из директории app приложения:
# blender -b -P ../contrib/blender/gravity_law.py
export_glb("src/main/assets/models/gravity_law.glb")