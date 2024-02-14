package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CsvUtilsTest {

    @Test
    @DisplayName("should throw illegal state exception when initialized")
    void shouldThrowIllegalStateExceptionWhenInitialized() throws NoSuchMethodException {
        var constructor = CsvUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        assertThatExceptionOfType(InvocationTargetException.class)
                .isThrownBy(constructor::newInstance)
                .withCauseInstanceOf(IllegalAccessException.class);
    }

    @TempDir
    private Path tempDir;

    @Test
    @DisplayName("should read csv")
    void shouldReadCsv() throws IOException {

        var csv = """
                name,age
                foo,10
                bar,20
                """;

        var csvPath = tempDir.resolve("test.csv");
        Files.writeString(csvPath, csv);
        var records = CsvUtils.readCsv(csvPath);

        assertThat(records).hasSize(3);

        var header = records.getFirst();
        assertThat(header.get(0)).isEqualTo("name");
        assertThat(header.get(1)).isEqualTo("age");

        var rec1 = records.get(1);
        assertThat(rec1.get(0)).isEqualTo("foo");
        assertThat(rec1.get(1)).isEqualTo("10");

        var rec2 = records.get(2);
        assertThat(rec2.get(0)).isEqualTo("bar");
        assertThat(rec2.get(1)).isEqualTo("20");
    }

    @Test
    @DisplayName("should write csv")
    void shouldWriteCsv() throws IOException {
        var tempFile = tempDir.resolve("test.csv");
        var data = List.of(
                new String[]{"Name", "Age", "Location"},
                new String[]{"John", "23", "New York"},
                new String[]{"Jane", "25", "San Francisco"}
        );

        CsvUtils.writeCsv(data, tempFile);
        try (var reader = Files.newBufferedReader(tempFile)) {

            var lines = reader.lines()
                    .toList();
            lines.forEach(System.out::println);
            assertThat(lines).hasSize(3);
        }
    }
}