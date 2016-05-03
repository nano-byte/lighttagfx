package de.nanobyte.lighttagfx.ui.searchresult;

import de.nanobyte.platform.gui.FileOpener;
import de.nanobyte.platform.gui.UnsupportedOperationSystemException;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.*;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Collection;
import static java.util.Objects.requireNonNull;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

public class SearchResultViewModel implements ViewModel {

    private final ReadOnlyListProperty<Path> foundFiles;
    private final ObjectProperty<Path> selectedFilePath = new SimpleObjectProperty<>();
    private final Command openFileCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() throws UnsupportedOperationSystemException, UncheckedIOException {
            fileOpener.setFilePathToOpen(selectedFilePath.get());
            fileOpener.openWithDefaultApplication();
        }
    }, Bindings.isNotNull(selectedFilePath));
    private final FileOpener fileOpener;

    public SearchResultViewModel(final Collection<Path> foundFiles, final FileOpener fileOpener) {
        this.foundFiles = new ReadOnlyListWrapper<>(requireNonNull(foundFiles).stream().distinct()
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));
        this.fileOpener = requireNonNull(fileOpener);
    }

    ReadOnlyListProperty<Path> foundFiles() {
        return foundFiles;
    }

    ObjectProperty<Path> selectedPath() {
        return selectedFilePath;
    }

    Command openFileCommand() {
        return openFileCommand;
    }
}
