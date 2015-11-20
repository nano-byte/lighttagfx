package de.nanobyte.lighttagfx;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(final String[] rawArguments) {
        launch(Main.class, rawArguments);
    }
    
    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("LightTagFX");
        primaryStage.show();
    }
}
