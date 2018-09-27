package com.lucasluc4.temptunes.thirdparty;

import com.lucasluc4.temptunes.thirdparty.dto.ResponseInfo;
import com.lucasluc4.temptunes.thirdparty.dto.ResponseStatus;
import com.lucasluc4.temptunes.thirdparty.dto.SpotifyPlaylistDTO;
import com.lucasluc4.temptunes.thirdparty.dto.SpotifyPlaylistResponse;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SpotifyApi {

    private ThirdPartyApisComponent thirdPartyApisComponent;

    @Autowired
    public SpotifyApi(ThirdPartyApisComponent thirdPartyApisComponent) {
        this.thirdPartyApisComponent = thirdPartyApisComponent;
    }

    public SpotifyPlaylistResponse getPlaylistById (String id, String bearerToken) {

        SpotifyApiFeign spotifyApiFeign = thirdPartyApisComponent.getSpotifyApi();

        try {

            Map<String, Object> headerMap = new HashMap<>();
            headerMap.put("Authorization", "Bearer " + bearerToken);

            SpotifyPlaylistDTO spotifyPlaylistDTO = spotifyApiFeign.getPlaylistById(id, headerMap);

            SpotifyPlaylistResponse response = new SpotifyPlaylistResponse();
            response.setPlaylist(spotifyPlaylistDTO);
            response.setInfo(createSuccessResponseInfo());

            return response;

        } catch (FeignException e) {
            return handleFeignException(e);
        }

    }

    private ResponseInfo createSuccessResponseInfo () {
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setCode(HttpStatus.OK.value());
        responseInfo.setStatus(ResponseStatus.SUCCESS);

        return responseInfo;
    }

    private SpotifyPlaylistResponse handleFeignException(FeignException e) {

        SpotifyPlaylistResponse response = new SpotifyPlaylistResponse();

        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setStatus(ResponseStatus.ERROR);
        responseInfo.setCode(e.status());
        responseInfo.setMessage(e.getMessage());

        return response;
    }
}
