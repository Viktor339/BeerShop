package com.shop.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JSONParseServiceTest {
    private final ObjectMapper objectMapper = mock(ObjectMapper.class);
    private JSONParseService jsonParseService;

    @BeforeEach
    public void setUp() {

        jsonParseService = new JSONParseService(objectMapper);

    }

    @Test
    void testParseFromJson() throws IOException {

        when(objectMapper.readValue((InputStream) any(), (Class<Object>) any())).thenReturn("Value");

        assertEquals("Value", jsonParseService.parseFromJson(new ByteArrayInputStream("a".getBytes()), Object.class));
        verify(objectMapper, times(1)).readValue((InputStream) any(), (Class<Object>) any());
    }
}

