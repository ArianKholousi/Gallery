package com.arian.gandomgallery;

import java.util.List;

/**
 * Created by Payami on 04/17/2018.
 */

public class GalleryItem {

    private String description;
    private List<String> images;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
