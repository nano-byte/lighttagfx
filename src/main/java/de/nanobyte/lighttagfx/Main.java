package de.nanobyte.lighttagfx;

import de.nanobyte.lighttagfx.ui.search.SearchView;
import de.nanobyte.lighttagfx.ui.search.SearchViewModel;
import de.saxsys.mvvmfx.FluentViewLoader;
import java.util.Collections;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(final String[] rawArguments) {
        Application.launch(Main.class, rawArguments);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("LightTagFX - Search");
        primaryStage.setScene(new Scene(FluentViewLoader.javaView(SearchView.class).viewModel(new SearchViewModel(Collections.EMPTY_LIST)).load().getView()));
        primaryStage.getScene().getStylesheets().add("css/common_layout.css");
        primaryStage.show();
    }
}
