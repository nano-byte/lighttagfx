package de.nanobyte.platform.gui;

import java.nio.file.Path;

final class MacOsXFileOpener extends AbstractFileOpener {

    public MacOsXFileOpener(final Path filePathToOpen) {
        super(filePathToOpen);
    }

    @Override
    String[] fileOpenCommands() {
        return new String[] {"open"};
    }
}
