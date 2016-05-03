package de.nanobyte.platform.gui;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

abstract class AbstractFileOpener implements FileOpener {

    private Path filePathToOpen;

    public AbstractFileOpener(final Path filePathToOpen) {
        this.filePathToOpen = filePathToOpen;
    }
    
    abstract String[] fileOpenCommands();
    
    @Override
    public final void openWithDefaultApplication() throws UncheckedIOException {
        final ProcessBuilder processBuilder = new ProcessBuilder(fileOpenCommands());
        processBuilder.command().add(filePathToOpen.toAbsolutePath().toString());
        try {
            processBuilder.start();
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }
    
    @Override
    public final void setFilePathToOpen(final Path filePathToOpen) {
        this.filePathToOpen = filePathToOpen;
    }
}
