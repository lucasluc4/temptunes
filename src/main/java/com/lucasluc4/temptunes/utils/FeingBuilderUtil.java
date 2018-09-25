package com.lucasluc4.temptunes.utils;

import feign.Feign;
import feign.Logger;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public class FeingBuilderUtil<T> {

    private final Class<T> apiTypeClass;

    public FeingBuilderUtil(Class<T> apiTypeClass) {
        this.apiTypeClass  = apiTypeClass;
    }

    public T build (String endpoint) {

        return Feign.builder().encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(apiTypeClass, endpoint);
    }

    public T build (String endpoint, Encoder encoder) {

        return Feign.builder().encoder(encoder)
                .decoder(new JacksonDecoder())
                .logLevel(Logger.Level.FULL)
                .target(apiTypeClass, endpoint);
    }
}
