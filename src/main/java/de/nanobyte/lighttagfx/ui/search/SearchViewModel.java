package de.nanobyte.lighttagfx.ui.search;

import de.saxsys.mvvmfx.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class SearchViewModel implements ViewModel {

    private List<String> tags = new ArrayList<>();

    List<String> getTags() {
        return tags;
    }
}
