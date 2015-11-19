package de.nanobyte.lighttagfx;

import de.nanobyte.lighttagfx.ui.search.SearchView;
import de.saxsys.mvvmfx.FluentViewLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(final String[] rawArguments) {
        Application.launch(Main.class, rawArguments);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("LigthTagFX - Search");
        primaryStage.setScene(new Scene(FluentViewLoader.javaView(SearchView.class).load().getView()));
        primaryStage.show();
    }
}
