package de.nanobyte.javafx.util;

import static java.util.Objects.requireNonNull;
import java.util.function.Function;
import javafx.util.StringConverter;

public class LambdaStringConverter<T> extends StringConverter<T> {

    private final Function<T, String> toString;
    private final Function<String, T> fromString;

    public LambdaStringConverter(final Function<String, T> fromString, final Function<T, String> toString) {
        this.fromString = requireNonNull(fromString);
        this.toString = requireNonNull(toString);
    }

    @Override
    public T fromString(final String string) {
        return fromString.apply(string);
    }

    @Override
    public String toString(final T object) {
        return toString.apply(object);
    }
}
