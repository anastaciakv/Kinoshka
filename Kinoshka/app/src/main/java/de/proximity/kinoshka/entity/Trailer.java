package de.proximity.kinoshka.entity;


import de.proximity.kinoshka.binding.BindListItem;

public class Trailer implements BindListItem {
    public String id;
    //    public String iso6391;
    //    public String iso31661;
    public String key;
    public String name;
    public String site;
    public int size;
    public String type;

    public String getYoutubeThumbnailUrl() {
        return "http://img.youtube.com/vi/" + key + "/0.jpg";
    }
}
