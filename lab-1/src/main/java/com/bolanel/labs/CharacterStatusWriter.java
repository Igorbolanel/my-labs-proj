package com.bolanel.labs;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class CharacterStatusWriter {

    private static final String HEADER = "status,count";

    public void writeStatusCount(Path filePath, Map<String, Integer> statusCountMap) throws IOException {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            bufferedWriter.write(HEADER);
            bufferedWriter.newLine();

            for (Map.Entry<String, Integer> entry : statusCountMap.entrySet()) {
                String status = entry.getKey();
                Integer count = entry.getValue();
                bufferedWriter.write(status + "," + count);
                bufferedWriter.newLine();
            }
        }
    }
}