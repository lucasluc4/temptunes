package com.lucasluc4.temptunes.thirdparty;

import com.lucasluc4.temptunes.thirdparty.dto.SpotifyPlaylistDTO;
import feign.HeaderMap;
import feign.Param;
import feign.RequestLine;

import java.util.Map;

public interface SpotifyApi {

    @RequestLine("GET /playlists/{playlistId}")
    SpotifyPlaylistDTO getPlaylistById(@Param("playlistId") String playlistId, @HeaderMap Map<String, Object> headerMap);

}
