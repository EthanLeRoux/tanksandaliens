module tanksandaliens {
    requires com.almasb.fxgl.all;

    opens com.holidaysoft.tanksandaliens to com.almasb.fxgl.all;
    opens assets.textures;

    exports com.holidaysoft.tanksandaliens;
}