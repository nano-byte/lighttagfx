package de.nanobyte.lighttagfx.ui.searchresult;

import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.JavaView;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;

public class SearchResultView extends Parent implements JavaView<SearchResultViewModel>, Initializable {

    @InjectViewModel
    private SearchResultViewModel viewModel;

    private final TreeView<Path> foundFilesTreeView = new TreeView<>(new TreeItem<>());

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        foundFilesTreeView.setShowRoot(false);
        foundFilesTreeView.setCellFactory(treeView -> new TreeCell<Path>() {
            @Override
            protected void updateItem(Path item, boolean empty) {
                super.updateItem(item, empty);
            }
        });
        viewModel.foundFiles().forEach(path -> {
            TreeItem<Path> currentRoot = foundFilesTreeView.getRoot();
            Iterator<Path> iterator = path.iterator();
            do {
                final Path next = iterator.next();
                Optional<TreeItem<Path>> findAny = currentRoot.getChildren().stream().filter(child -> next.equals(child.getValue())).findAny();
                if (findAny.isPresent()) {
                    currentRoot = findAny.get();
                } else {
                    final TreeItem<Path> newRoot = new TreeItem<>(iterator.hasNext() ? next : path);
                    currentRoot.getChildren().add(newRoot);
                    currentRoot = newRoot;
                }
            } while (iterator.hasNext());
        });
        foundFilesTreeView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                Path value = foundFilesTreeView.getSelectionModel().getSelectedItem().getValue();
                if (value.getNameCount() != 1) {
                    viewModel.openFileCommand(value).execute();
                }
            }
        });
        getChildren().add(foundFilesTreeView);
    }
}
