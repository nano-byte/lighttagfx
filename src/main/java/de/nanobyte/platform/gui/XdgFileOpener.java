package de.nanobyte.platform.gui;

import java.nio.file.Path;

final class XdgFileOpener extends AbstractFileOpener {

    XdgFileOpener(final Path filePathToOpen) {
        super(filePathToOpen);
    }

    @Override
    String[] fileOpenCommands() {
        return new String[]{"xdg-open"};
    }
}
