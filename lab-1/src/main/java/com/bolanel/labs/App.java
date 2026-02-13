package com.bolanel.labs;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class App {

    private static final String INPUT_FILE_NAME = "src/characters.csv";
    private static final String OUTPUT_FILE_NAME = "status-count.csv";

    public static void main(String[] args) throws IOException {
        Path inputFilePath = Path.of(INPUT_FILE_NAME);
        Path outputFilePath = Path.of(OUTPUT_FILE_NAME);

        CharacterReader characterReader = new CharacterReader();
        List<Charapter> characterList = characterReader.readCharacterList(inputFilePath);

        CharacterStatusCounter characterStatusCounter = new CharacterStatusCounter();
        Map<String, Integer> statusCountMap = characterStatusCounter.countStatus(characterList);

        CharacterStatusWriter characterStatusWriter = new CharacterStatusWriter();
        characterStatusWriter.writeStatusCount(outputFilePath, statusCountMap);
    }
}
