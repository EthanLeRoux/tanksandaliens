module tanksandaliens {
    requires com.almasb.fxgl.all;

    opens com.holidaysoft.tanksandaliens to com.almasb.fxgl.all;
    opens assets.textures;
    opens assets.sounds;

    exports com.holidaysoft.tanksandaliens;
}