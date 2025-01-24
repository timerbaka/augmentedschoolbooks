# БукvARik
![logo_with_text.png](contrib/img/logo_with_text.png)

BukvARik je ukázková aplikace demonstrující možnosti rozšířené reality (AR) na Androidu. Aplikace umožňuje načítání a zobrazování 3D modelů v AR s využitím fyzických objektů jako markerů.

## Požadavky na zařízení
Aplikace vyžaduje zařízení s operačním systémem Android 7.0 nebo novějším, které podporuje ARCore. Pro ověření podpory ARCore na vašem zařízení můžete navštívit [stránku podporovaných zařízení ARCore](https://developers.google.com/ar/discover/supported-devices).

## Použité knihovny a nástroje
Aplikace využívá následující knihovny a nástroje:

- [SceneView](https://sceneview.github.io/) - knihovna pro práci s 3D scénami na Androidu.
- [ARCore](https://developers.google.com/ar) - platforma rozšířené reality pro Android od společnosti Google.
- [Material Design Components](https://m3.material.io/components) - knihovna uživatelských komponent podle designového stylu Material Design od Google.

## Instalace a spuštění aplikace
- Naklonujte repozitář do svého počítače:
    ```bash
    git clone https://github.com/timerbaka/augmentedschoolbooks.git
    ```
- Otevřete projekt v Android Studio nebo IntelliJ IDEA.
- Připojte své zařízení Android k počítači a ujistěte se, že je povoleno ladění přes USB.
- Spusťte projekt na zařízení pomocí tlačítka "Run" v IDE.

## Jak používat aplikaci
Po spuštění aplikace je třeba povolit přístup ke kameře, aby ji aplikace mohla využít pro práci s AR.  
Jakmile se inicializují ARCore a SceneView, obrazovka se přepne na zobrazení z kamery zařízení. Aplikace automaticky vyhledává obrázky rozšířené reality s šablonami z adresáře `images` a zobrazuje na nich odpovídající 3D modely.

Aplikace také podporuje práci s vlastními obrázky rozšířené reality ve formátu PNG uloženými ve složce `images`. Pro přidání vlastního obrázku:
1. Přidejte nový obrázek do složky `images`.
2. Přidejte odpovídající 3D model ve [formátu GLB](https://en.wikipedia.org/wiki/GlTF) do složky `models`.
3. Přidejte nový obrázek do databáze AugmentedImageDatabase a použijte jeho název jako klíč.
4. Upravte seznam `models` v souboru `MainActivity.kt`, kde jako první argument uvedete název klíče.

Aplikace podporuje automatickou animaci modelu i manuální ovládání rotace a měřítka modelu.

Po výběru markeru začne proces načítání modelu, který může chvíli trvat. Po načtení bude model umístěn na pozici markeru v AR. Uživatel může s modelem interagovat pomocí gest, přesouvat jej a otáčet v prostoru.

## Licence
BukvARik je distribuován pod licencí MIT. Podrobnosti naleznete v souboru LICENSE.

## Autoři
BukvARik byl vyvinut Timurem Saifievem v roce 2023. Pokud máte jakékoli dotazy, můžete mě kontaktovat na adrese timerbaka@gmail.com.