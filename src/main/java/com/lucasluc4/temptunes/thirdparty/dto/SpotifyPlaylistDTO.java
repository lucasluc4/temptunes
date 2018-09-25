package com.lucasluc4.temptunes.thirdparty.dto;

public class SpotifyPlaylistDTO {

    private Boolean collaborative;
    private String description;
    private String name;

    private SpotifyTracksDTO tracks;

    public Boolean getCollaborative() {
        return collaborative;
    }

    public void setCollaborative(Boolean collaborative) {
        this.collaborative = collaborative;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SpotifyTracksDTO getTracks() {
        return tracks;
    }

    public void setTracks(SpotifyTracksDTO tracks) {
        this.tracks = tracks;
    }
}
