package com.lucasluc4.temptunes.thirdparty.dto;

import java.util.List;

public class SpotifyTracksDTO {

    private List<SpotifyItemDTO> items;

    public List<SpotifyItemDTO> getItems() {
        return items;
    }

    public void setItems(List<SpotifyItemDTO> items) {
        this.items = items;
    }
}
