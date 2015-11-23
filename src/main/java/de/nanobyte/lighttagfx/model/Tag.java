package de.nanobyte.lighttagfx.model;

import static java.util.Objects.requireNonNull;
import javafx.scene.paint.Color;

public class Tag {

    private final String text;
    private Color textColor;

    public Tag(final String text) {
        this(text, Color.BLACK);
    }

    public Tag(final String text, final Color textColor) {
        this.text = requireNonNull(text);
        this.textColor = requireNonNull(textColor);
    }

    public String getText() {
        return text;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(final Color textColor) {
        this.textColor = requireNonNull(textColor);
    }
}
