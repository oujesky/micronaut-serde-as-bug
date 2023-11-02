package com.example;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class MicronautSerdeAsBugTest {

    private static final String JSON = "{\"list\": [1, 2, 3]}";

    @Inject
    private io.micronaut.serde.ObjectMapper micronautObjectMapper;

    @Inject
    private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper;

    @Serdeable.Deserializable
    record MicronautRecord(
        @Serdeable.Deserializable(as = LinkedList.class) List<Integer> list
    ) {}

    @Test
    void testMicronautObjectMapperWithMicronautAnnotations() throws IOException {
        var result = micronautObjectMapper.readValue(JSON, MicronautRecord.class);

        assertThat(result.list())
            .containsExactly(1, 2, 3)
            .isInstanceOf(LinkedList.class);
    }

    @Serdeable.Deserializable
    record MicronautJacksonRecord(
        @JsonDeserialize(as = LinkedList.class) List<Integer> list
    ) {}

    @Test
    void testMicronautObjectMapperWithJacksonAnnotations() throws IOException {
        var result = micronautObjectMapper.readValue(JSON, MicronautJacksonRecord.class);

        assertThat(result.list())
            .containsExactly(1, 2, 3)
            .isInstanceOf(LinkedList.class);
    }

    record JacksonRecord(
        @JsonDeserialize(as = LinkedList.class) List<Integer> list
    ) {}

    @Test
    void testJacksonObjectMapperWithJacksonAnnotations() throws IOException {
        var result = jacksonObjectMapper.readValue(JSON, JacksonRecord.class);

        assertThat(result.list())
            .containsExactly(1, 2, 3)
            .isInstanceOf(LinkedList.class);
    }

}
