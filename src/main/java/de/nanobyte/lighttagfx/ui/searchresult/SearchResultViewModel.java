package de.nanobyte.lighttagfx.ui.searchresult;

import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.*;
import java.nio.file.Path;
import static java.util.Objects.requireNonNull;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import org.apache.commons.lang3.SystemUtils;

public class SearchResultViewModel implements ViewModel {

    private final ObservableSet<Path> foundFiles;

    public SearchResultViewModel(final Set<Path> foundFiles) {
        this.foundFiles = FXCollections.observableSet(requireNonNull(foundFiles));
    }

    public ObservableSet<Path> foundFiles() {
        return foundFiles;
    }

    Command openFileCommand(final Path path) {
        return new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                final String[] openFileCommand;
                if (SystemUtils.IS_OS_LINUX) {
                    openFileCommand = new String[]{"xdg-open", path.toString()};
                } else if (SystemUtils.IS_OS_WINDOWS) {
                    openFileCommand = new String[]{"start", path.toString()};
                } else if (SystemUtils.IS_OS_MAC) {
                    openFileCommand = new String[]{"open", path.toString()};
                } else {
                    throw new UnsupportedOperationException("Can't open file. Your platform is not supported.");
                }
                new ProcessBuilder(openFileCommand).start();
            }
        });
    }
}
