package de.nanobyte.lighttagfx.ui.search;

import de.nanobyte.javafx.util.LambdaStringConverter;
import de.nanobyte.lighttagfx.model.Tag;
import java.util.Objects;
import java.util.Optional;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.CheckTreeView;
import org.fxmisc.easybind.EasyBind;

public class TagSelectionControl extends Control {

    private final TextField tagFilter = new TextField();
    private final CheckTreeView<Tag> tagSelector = new CheckTreeView<>(new CheckBoxTreeItem<>());

    private final ObservableList<Tag> tags = FXCollections.observableArrayList();
    private final ObservableMap<Tag, CheckBoxTreeItem<Tag>> wrappedTagsCache = FXCollections.observableHashMap();
    private final ObservableList<CheckBoxTreeItem<Tag>> wrappedTags = EasyBind.map(tags, tag -> wrappedTagsCache.computeIfAbsent(tag, CheckBoxTreeItem<Tag>::new));
    private final FilteredList<CheckBoxTreeItem<Tag>> visibleTags = new FilteredList<>(wrappedTags);
    // must be a class field because else it will be garbage collected to soon
    private final FilteredList<Tag> selectedTags = new FilteredList<>(
            EasyBind.map(tagSelector.getCheckModel().getCheckedItems(), TreeItem<Tag>::getValue), Objects::nonNull);

    public TagSelectionControl() {
        super();
        setSkin(new SkinBase<TagSelectionControl>(this) {
        });

        tagSelector.setShowRoot(false);
        // used CheckTreeView constructor as implementation base
        tagSelector.setCellFactory(tree -> {
            return new CheckBoxTreeCell<Tag>(item -> {
                if (item instanceof CheckBoxTreeItem<?>) {
                    return ((CheckBoxTreeItem<?>) item).selectedProperty();
                }
                return null;
            }, new LambdaStringConverter<>(value -> new CheckBoxTreeItem<>(new Tag(value)),
                    treeItem -> treeItem.getValue().getText())) {
                @Override
                public void updateItem(Tag item, boolean empty) {
                    super.updateItem(item, empty);
                    Optional.ofNullable(item).ifPresent(tag -> setTextFill(tag.getTextColor()));
                }
            };
        });

        setupBindings();
        getChildren().addAll(new VBox(tagFilter, tagSelector));
    }

    private void setupBindings() {
        EasyBind.subscribe(tagFilter.textProperty(), (final String newValue)
                -> visibleTags.setPredicate(treeItem -> StringUtils.containsIgnoreCase(treeItem.getValue().getText(), newValue)));
        EasyBind.listBind(tagSelector.getRoot().getChildren(), visibleTags);
        tags.addListener((ListChangeListener.Change<? extends Tag> change) -> {
            while (change.next()) {
                if (change.wasRemoved() || change.wasReplaced()) {
                    change.getRemoved().forEach(removedTag -> wrappedTagsCache.remove(removedTag));
                } else if (change.wasUpdated()) {
                    change.getAddedSubList().forEach(removedTag -> wrappedTagsCache.remove(removedTag));
                }
            }
        });
    }

    public ObservableList<Tag> tagsProperty() {
        return tags;
    }

    public ReadOnlyListProperty<Tag> selectedTagsProperty() {
        return new ReadOnlyListWrapper<>(selectedTags);
    }
}
