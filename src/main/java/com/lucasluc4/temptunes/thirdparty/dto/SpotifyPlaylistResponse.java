package com.lucasluc4.temptunes.thirdparty.dto;

public class SpotifyPlaylistResponse {

    private ResponseInfo info;
    private SpotifyPlaylistDTO playlist;

    public ResponseInfo getInfo() {
        return info;
    }

    public void setInfo(ResponseInfo info) {
        this.info = info;
    }

    public SpotifyPlaylistDTO getPlaylist() {
        return playlist;
    }

    public void setPlaylist(SpotifyPlaylistDTO playlist) {
        this.playlist = playlist;
    }
}
