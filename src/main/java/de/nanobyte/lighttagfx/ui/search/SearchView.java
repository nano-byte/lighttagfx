package de.nanobyte.lighttagfx.ui.search;

import de.nanobyte.javafx.util.LambdaStringConverter;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.JavaView;
import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import org.apache.commons.lang3.SystemUtils;
import org.controlsfx.tools.Borders;
import org.fxmisc.easybind.EasyBind;

public class SearchView extends VBox implements JavaView<SearchViewModel>, Initializable {

    @InjectViewModel
    private SearchViewModel viewModel;

    private final TextField searchFolder = new TextField();
    private final Button chooseSearchFolderButton = new Button("...");
    private final DirectoryChooser searchFolderChooser = new DirectoryChooser();
    private final TagSelectionControl tagSelector = new TagSelectionControl();
    private final Button searchButton = new Button("Search for files with selected tags");

    public SearchView() {
        getChildren().addAll(new HBox(searchFolder, chooseSearchFolderButton));
        HBox.setHgrow(searchFolder, Priority.ALWAYS);
        final Node tagSelectionNodes = Borders.wrap(tagSelector).lineBorder().title("Tags").buildAll();
        VBox.setVgrow(tagSelectionNodes, Priority.ALWAYS);
        getChildren().addAll(tagSelectionNodes, searchButton);
        VBox.setVgrow(tagSelector, Priority.ALWAYS);
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        chooseSearchFolderButton.setOnAction((final ActionEvent actionEvent) -> Optional.ofNullable(
                searchFolderChooser.showDialog(((Node) actionEvent.getTarget()).getScene().getWindow()))
                .ifPresent(searchFolderChooser::setInitialDirectory));

        Bindings.bindBidirectional(searchFolder.textProperty(), searchFolderChooser.initialDirectoryProperty(),
                new LambdaStringConverter<>(stringRepresentation -> Optional.ofNullable(new File(stringRepresentation))
                        .filter(File::isDirectory).orElseGet(SystemUtils::getUserHome),
                        file -> Optional.ofNullable(file).map(File::toString).orElse("")));

        searchButton.disableProperty().bind(Bindings.isEmpty(tagSelector.selectedTagsProperty()));

        EasyBind.listBind(viewModel.getSearchTags(), tagSelector.selectedTagsProperty());
        EasyBind.listBind(tagSelector.tagsProperty(), viewModel.getPredefinedTags());
    }
}
