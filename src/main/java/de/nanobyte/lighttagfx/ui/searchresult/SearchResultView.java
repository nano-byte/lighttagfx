package de.nanobyte.lighttagfx.ui.searchresult;

import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.JavaView;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import org.fxmisc.easybind.EasyBind;

public class SearchResultView extends Parent implements JavaView<SearchResultViewModel>, Initializable {

    @InjectViewModel
    private SearchResultViewModel viewModel;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        final TreeView<PathPart> foundFilesTreeView = new TreeView<>(new TreeItem<>());
        foundFilesTreeView.setShowRoot(false);
        viewModel.foundFiles().forEach(fullPath -> {
            TreeItem<PathPart> currentRoot = foundFilesTreeView.getRoot();
            for (int pathDepth = 0; pathDepth < fullPath.getNameCount(); pathDepth++) {
                final PathPart currentPathPart = new PathPart(fullPath, pathDepth);
                final Optional<TreeItem<PathPart>> existingPathPart = currentRoot.getChildren().stream()
                        .filter(child -> currentPathPart.equals(child.getValue())).findAny();
                if (existingPathPart.isPresent()) {
                    currentRoot = existingPathPart.get();
                } else {
                    final TreeItem<PathPart> newRoot = new TreeItem<>(currentPathPart);
                    currentRoot.getChildren().add(newRoot);
                    currentRoot = newRoot;
                }
            }
        });
        EasyBind.select(foundFilesTreeView.selectionModelProperty()).select(MultipleSelectionModel::selectedItemProperty).selectObject(TreeItem::valueProperty);

        viewModel.selectedPath().bind(EasyBind.select(foundFilesTreeView.selectionModelProperty()).
                selectObject(MultipleSelectionModel::selectedItemProperty).selectProperty(TreeItem::valueProperty).
                filter(PathPart::isFileName).map(pathPart -> pathPart.fullPath));

        foundFilesTreeView.setOnMouseClicked(mouseEvent -> {
            if (viewModel.openFileCommand().isExecutable()
                && MouseButton.PRIMARY == mouseEvent.getButton() && mouseEvent.getClickCount() == 2) {
                viewModel.openFileCommand().execute();
            }
        });

        getChildren().add(foundFilesTreeView);
    }

    private class PathPart {

        private final Path fullPath;
        private final int pathIndex;

        public PathPart(final Path fullPath, final int pathIndex) {
            this.fullPath = fullPath;
            this.pathIndex = pathIndex;
        }

        private Path getName() {
            return fullPath.getName(pathIndex);
        }

        public boolean isFileName() {
            return fullPath.getNameCount() - 1 == pathIndex;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final PathPart other = (PathPart) obj;
            if (this.pathIndex != other.pathIndex) {
                return false;
            }
            return Objects.equals(this.getName(), other.getName());
        }

        @Override
        public String toString() {
            return getName().toString();
        }
    }
}
