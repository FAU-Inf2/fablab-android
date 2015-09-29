package de.fau.cs.mad.fablab.android.model.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

public class JsonInputStringOutputConverter implements Converter {

    private static final String MIME_TYPE = "application/json; charset=UTF-8";

    private final ObjectMapper objectMapper;

    public JsonInputStringOutputConverter() {
        this(new ObjectMapper());
    }

    public JsonInputStringOutputConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object fromBody(TypedInput typedInput, Type type) throws ConversionException {
        String text = null;
        try {
            text = fromStream(typedInput.in());
        } catch (IOException ignored) {/*NOP*/ }

        return text;
    }

    @Override public TypedOutput toBody(Object object) {
        try {
            String json = objectMapper.writeValueAsString(object);
            return new TypedByteArray(MIME_TYPE, json.getBytes("UTF-8"));
        } catch (JsonProcessingException e) {
            throw new AssertionError(e);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

     public static String fromStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        return out.toString();
     }
}