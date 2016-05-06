package de.nanobyte.platform.gui;

import java.nio.file.Path;

final class WindowsFileOpener extends AbstractFileOpener {

    public WindowsFileOpener(final Path filePathToOpen) {
        super(filePathToOpen);
    }

    @Override
    String[] fileOpenCommands() {
        return new String[]{"open", ""};
    }
}
