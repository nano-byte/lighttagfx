package de.nanobyte.lighttagfx.ui.search;

import de.nanobyte.javafx.util.LambdaStringConverter;
import de.nanobyte.lighttagfx.model.Tag;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.JavaView;
import java.io.File;
import java.net.URL;
import java.util.*;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.controlsfx.control.CheckTreeView;
import org.controlsfx.tools.Borders;
import org.fxmisc.easybind.EasyBind;

public class SearchView extends VBox implements JavaView<SearchViewModel>, Initializable {

    @InjectViewModel
    private SearchViewModel viewModel;

    private final TextField searchFolder = new TextField();
    private final Button chooseSearchFolderButton = new Button("...");
    private final DirectoryChooser searchFolderChooser = new DirectoryChooser();
    private final TextField tagFilter = new TextField();
    // todo: replace with CheckListView
    private final CheckTreeView<Tag> tagSelection = new CheckTreeView<>(new CheckBoxTreeItem<>());
    private final FilteredList<TreeItem<Tag>> visibleTags = new FilteredList<>(FXCollections.observableArrayList());
    // must be a class field because else it will be garbage collected to soon
    private final FilteredList<Tag> selectedTags = new FilteredList<>(
            EasyBind.map(tagSelection.getCheckModel().getCheckedItems(), TreeItem<Tag>::getValue), Objects::nonNull);
    private final Button searchButton = new Button("Search for files with selected tags");

    private final static double DEFAULT_SPACING = 7;

    public SearchView() {
        setSpacing(DEFAULT_SPACING);
        setPadding(new Insets(DEFAULT_SPACING));
        getChildren().addAll(new HBox(DEFAULT_SPACING, searchFolder, chooseSearchFolderButton));
        HBox.setHgrow(searchFolder, Priority.ALWAYS);
        final Node tagSelectionNodes = Borders.wrap(new VBox(DEFAULT_SPACING, tagFilter, tagSelection)).lineBorder()
                .outerPadding(DEFAULT_SPACING, 0, 0, 0).innerPadding(DEFAULT_SPACING).title("Tags").buildAll();
        VBox.setVgrow(tagSelectionNodes, Priority.ALWAYS);
        getChildren().addAll(tagSelectionNodes, searchButton);
        VBox.setVgrow(tagSelection, Priority.ALWAYS);

        tagSelection.setShowRoot(false);
        // used CheckTreeView constructor as implementation base
        tagSelection.setCellFactory(tree -> {
            return new CheckBoxTreeCell<Tag>(item -> {
                if (item instanceof CheckBoxTreeItem<?>) {
                    return ((CheckBoxTreeItem<?>) item).selectedProperty();
                }
                return null;
            }, new LambdaStringConverter<>(value -> new CheckBoxTreeItem<>(new Tag(value)),
                    treeItem -> treeItem.getValue().getText())) {
                @Override
                public void updateItem(Tag item, boolean empty) {
                    super.updateItem(item, empty);
                    Optional.ofNullable(item).ifPresent(tag -> setTextFill(tag.getTextColor()));
                }
            };
        });
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

        EasyBind.subscribe(tagFilter.textProperty(), (final String newValue)
                -> visibleTags.setPredicate(treeItem -> StringUtils.containsIgnoreCase(treeItem.getValue().getText(), newValue)));
        EasyBind.listBind(tagSelection.getRoot().getChildren(), visibleTags);

        searchButton.disableProperty().bind(Bindings.isEmpty(tagSelection.getCheckModel().getCheckedItems()));

        EasyBind.listBind(viewModel.getSearchTags(), selectedTags);
        EasyBind.listBind((List<? super TreeItem<Tag>>) visibleTags.getSource(),
                EasyBind.map(viewModel.getPredefinedTags(), CheckBoxTreeItem<Tag>::new));
    }
}
