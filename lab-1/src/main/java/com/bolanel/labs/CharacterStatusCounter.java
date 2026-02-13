package com.bolanel.labs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterStatusCounter {

    public Map<String, Integer> countStatus(List<Charapter> characterList) {
        Map<String, Integer> statusCountMap = new HashMap<>();

        for (Charapter character : characterList) {
            String status = character.getStatus();
            if (status == null || status.isEmpty()) {
                continue;
            }

            Integer currentCount = statusCountMap.get(status);
            if (currentCount == null) {
                statusCountMap.put(status, 1);
            } else {
                statusCountMap.put(status, currentCount + 1);
            }
        }

        return statusCountMap;
    }
}