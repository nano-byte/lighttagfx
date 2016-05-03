package de.nanobyte.platform;

public class UnsupportedOperationSystemException extends RuntimeException {

    public UnsupportedOperationSystemException(final String operatingSystemName) {
        super("The operating system " + operatingSystemName + " is not supported.");
    }
}
