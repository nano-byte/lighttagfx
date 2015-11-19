package de.nanobyte.javafx.util;

import static java.util.Objects.requireNonNull;
import java.util.function.Function;
import javafx.util.StringConverter;

public class LambdaStringConverter<T> extends StringConverter<T> {

    private final Function<T, String> toString;
    private final Function<String, T> fromString;

    public LambdaStringConverter(final Function<T, String> toString, final Function<String, T> fromString) {
        this.toString = requireNonNull(toString);
        this.fromString = requireNonNull(fromString);
    }

    @Override
    public String toString(final T object) {
        return toString.apply(object);
    }

    @Override
    public T fromString(final String string) {
        return fromString.apply(string);
    }
}
