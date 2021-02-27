package com.netflix.repositories.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.MediaType;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.StringDecoder;
import feign.jackson.JacksonDecoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

public class JsonOrTextDecoder implements Decoder {

    private JacksonDecoder jacksonDecoder;
    private StringDecoder stringDecoder;

    public JsonOrTextDecoder() {
        jacksonDecoder = new JacksonDecoder(new ObjectMapper());
        this.stringDecoder = new StringDecoder();
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        if (isTextResponse(response)) {
            return stringDecoder.decode(response, type);
        } else {
            return jacksonDecoder.decode(response, type);
        }
    }

    static boolean isTextResponse(Response response) {
        Collection<String> contentTypeHeaders = response.headers().get("content-type");
        if (contentTypeHeaders != null) {
            for (String header : contentTypeHeaders) {
                MediaType actualMediaType = MediaType.parse(header);
                if (actualMediaType.is(MediaType.ANY_TEXT_TYPE)) {
                    return true;
                }
            }
        }
        return false;
    }

}
