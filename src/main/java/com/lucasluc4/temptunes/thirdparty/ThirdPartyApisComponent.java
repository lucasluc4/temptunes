package com.lucasluc4.temptunes.thirdparty;

import com.lucasluc4.temptunes.utils.FeignBuilder;
import feign.form.FormEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ThirdPartyApisComponent {

    private static final String OPEN_WEATHER_API_URL = "openweather.url.api";

    private static final String SPOTIFY_URL_API = "spotify.url.api";
    private static final String SPOTIFY_URL_ACCOUNTS = "spotify.url.accounts";

    private OpenWeatherApi openWeatherApi;

    private SpotifyApiFeign spotifyApi;

    private LoginSpotifyApi loginSpotifyApi;

    private Environment environment;

    @Autowired
    public ThirdPartyApisComponent(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        openWeatherApi = new FeignBuilder<>(OpenWeatherApi.class)
                .build(environment.getProperty(OPEN_WEATHER_API_URL));

        spotifyApi = new FeignBuilder<>(SpotifyApiFeign.class)
                .build(environment.getProperty(SPOTIFY_URL_API));

        loginSpotifyApi = new FeignBuilder<>(LoginSpotifyApi.class)
                .build(environment.getProperty(SPOTIFY_URL_ACCOUNTS), new FormEncoder());
    }

    public OpenWeatherApi getOpenWeatherApi() {
        return openWeatherApi;
    }

    public SpotifyApiFeign getSpotifyApi() {
        return spotifyApi;
    }

    public LoginSpotifyApi getLoginSpotifyApi() {
        return loginSpotifyApi;
    }
}
