package de.proximity.kinoshka.binding;


import java.util.List;

public interface BindListAdapter<T extends BindListItem> {
    void replaceItems(List<T> items);
}
