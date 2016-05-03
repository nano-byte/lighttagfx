package de.nanobyte.platform.gui;

import java.io.UncheckedIOException;
import java.nio.file.Path;

public interface FileOpener {
    
    public void openWithDefaultApplication() throws UncheckedIOException;
    
    public void setFilePathToOpen(final Path filePathToOpen);
}
