package com.ivoyant.customerusecase.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StateConverter {
    private static final Map<String, String> stateAbbreviations = new HashMap<>();

    static {
        stateAbbreviations.put("Alabama", "AL");
        stateAbbreviations.put("Alaska", "AK");
        stateAbbreviations.put("Georgia", "GA");
        stateAbbreviations.put("Karnataka","KA");
    }

    public static String convertToAbbreviation(String stateName) {
        String abbreviation = stateAbbreviations.get(stateName);
        if (abbreviation == null) {
            throw new IllegalArgumentException("Invalid state name: " + stateName);
        }
        return abbreviation;
    }
}
