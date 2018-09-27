package com.lucasluc4.temptunes.service;

import com.lucasluc4.temptunes.exception.ValidationException;
import com.lucasluc4.temptunes.model.Weather;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class GeoPlaylistsServiceTest {

    @Mock
    private WeatherService weatherService;

    @Mock
    private PlaylistTemperatureService playlistTemperatureService;

    @InjectMocks
    private GeoPlaylistsService geoPlaylistsService;

    @Test(expected = ValidationException.class)
    public void validatesCityName() {
        geoPlaylistsService.getByCity(null);
    }

    @Test(expected = ValidationException.class)
    public void validatesLatLongName() {
        geoPlaylistsService.getByLatLng(200.0, 200.0);
    }

    @Test
    public void callWeatherServiceAndPlaylistTemperatureService () {

        String cityName = "London";

        Weather weather = new Weather();
        weather.setTemperature(300.0);

        when(weatherService.getByCity(cityName)).thenReturn(weather);

        geoPlaylistsService.getByCity(cityName);

        verify(playlistTemperatureService, times(1)).getByTemperature(weather.getTemperature());

    }

}
