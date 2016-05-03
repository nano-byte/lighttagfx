package de.nanobyte.platform.gui;

import de.nanobyte.platform.UnsupportedOperationSystemException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import static java.util.Objects.requireNonNull;
import org.apache.commons.lang3.SystemUtils;

public final class OsAwareFileOpener implements FileOpener {

    private final FileOpener fileOpener;

    public OsAwareFileOpener(final Path filePathToOpen) throws UnsupportedOperationSystemException {
        requireNonNull(filePathToOpen);
        if (SystemUtils.IS_OS_LINUX) {
            fileOpener = new XdgFileOpener(filePathToOpen);
        } else if (SystemUtils.IS_OS_WINDOWS) {
            fileOpener = new WindowsFileOpener(filePathToOpen);
        } else if (SystemUtils.IS_OS_MAC_OSX) {
            fileOpener = new MacOsXFileOpener(filePathToOpen);
        } else {
            throw new UnsupportedOperationSystemException(SystemUtils.OS_NAME + " " + SystemUtils.OS_VERSION);
        }
    }

    @Override
    public void openWithDefaultApplication() throws UncheckedIOException {
        fileOpener.openWithDefaultApplication();
    }

    @Override
    public void setFilePathToOpen(final Path filePathToOpen) {
        fileOpener.setFilePathToOpen(filePathToOpen);
    }
}
