package com.lucasluc4.temptunes.thirdparty;

import com.lucasluc4.temptunes.thirdparty.dto.OpenWeatherDTO;
import com.lucasluc4.temptunes.thirdparty.dto.OpenWeatherMainDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class OpenWeatherApiServiceTest {

    @Mock
    private Environment environment;

    @Mock
    private ThirdPartyApisComponent thirdPartyApisComponent;

    @InjectMocks
    private OpenWeatherApiService openWeatherApiService;

    private OpenWeatherApi openWeatherApi;

    @Before
    public void init() throws Exception {

        OpenWeatherDTO openWeatherDTO = new OpenWeatherDTO();
        OpenWeatherMainDTO openWeatherMainDTO = new OpenWeatherMainDTO();
        openWeatherDTO.setMain(openWeatherMainDTO);

        openWeatherApi = mock(OpenWeatherApi.class);

        when(environment.getProperty(anyString())).thenReturn("");

        when(openWeatherApi.getWeatherByCityName(anyString(), anyString())).thenReturn(openWeatherDTO);

        when(openWeatherApi.getWeatherByLatLng(anyDouble(), anyDouble(), anyString()))
                .thenReturn(openWeatherDTO);

        when(thirdPartyApisComponent.getOpenWeatherApi()).thenReturn(openWeatherApi);
    }

    @Test
    public void getWeatherByOpenWeatherApi () {
        openWeatherApiService.getWeatherByCity("London");

        verify(openWeatherApi, times(1)).getWeatherByCityName(eq("London"), anyString());
    }

    @Test
    public void getWeatherByOpenWeatherApiUsingLatLong () {
        openWeatherApiService.getWeatherByLatLng(-3.0, -30.0);

        verify(openWeatherApi, times(1)).getWeatherByLatLng(eq(-3.0), eq(-30.0),
                anyString());
    }


}
