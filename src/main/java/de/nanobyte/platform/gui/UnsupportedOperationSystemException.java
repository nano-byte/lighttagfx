package de.nanobyte.platform.gui;

public class UnsupportedOperationSystemException extends RuntimeException {

    UnsupportedOperationSystemException(final String operatingSystemName) {
        super("The operating system " + operatingSystemName + " is not supported.");
    }
}
