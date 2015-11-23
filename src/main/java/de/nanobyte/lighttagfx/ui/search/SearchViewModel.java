package de.nanobyte.lighttagfx.ui.search;

import de.nanobyte.lighttagfx.model.Tag;
import de.saxsys.mvvmfx.ViewModel;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SearchViewModel implements ViewModel {

    private final ObservableList<Tag> predefinedTags = FXCollections.observableArrayList();
    private final List<Tag> searchTags = new ArrayList<>();

    public SearchViewModel(final List<Tag> predefinedTags) {
        this.predefinedTags.addAll(predefinedTags);
    }
    
    ObservableList<? extends Tag> getPredefinedTags() {
        return predefinedTags;
    }
    
    List<Tag> getSearchTags() {
        return searchTags;
    }
}
