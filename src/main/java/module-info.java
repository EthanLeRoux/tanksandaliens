module tanksandaliens {
    requires com.almasb.fxgl.all;

    opens com.holidaysoft.tanksandaliens to com.almasb.fxgl.all;
    opens assets.textures;
    opens assets.sounds;
    opens assets.music;

    exports com.holidaysoft.tanksandaliens;
}