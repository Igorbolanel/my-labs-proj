package com.bolanel.labs;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CharacterReader {

    public List<Charapter> readCharacterList(Path filePath) throws IOException {
        List<Charapter> characterList = new ArrayList<>();

        try (BufferedReader bufferedReader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String headerLine = bufferedReader.readLine();
            if (headerLine == null) {
                return characterList;
            }

            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                Charapter character = parseLine(currentLine);
                if (character != null) {
                    characterList.add(character);
                }
            }
        }

        return characterList;
    }

    private Charapter parseLine(String line) {
        String[] columnArray = line.split(",", -1);
        if (columnArray.length < 3) {
            return null;
        }

        long id = Long.parseLong(columnArray[0]);
        String name = columnArray[1];
        String status = columnArray[2];

        return new Charapter(id, name, status);
    }
}