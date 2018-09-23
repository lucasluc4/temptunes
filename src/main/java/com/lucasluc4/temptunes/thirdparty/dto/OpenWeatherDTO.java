package com.lucasluc4.temptunes.thirdparty.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherDTO {

    private OpenWeatherMainDTO main;
    private OpenWeatherSysDTO sys;

    public OpenWeatherMainDTO getMain() {
        return main;
    }

    public void setMain(OpenWeatherMainDTO main) {
        this.main = main;
    }

    public OpenWeatherSysDTO getSys() {
        return sys;
    }

    public void setSys(OpenWeatherSysDTO sys) {
        this.sys = sys;
    }
}
