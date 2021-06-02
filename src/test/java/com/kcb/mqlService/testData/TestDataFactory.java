package com.kcb.mqlService.testData;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.util.*;

public class TestDataFactory {

    public static List<Map<String, Object>> tableOf(String tableName) {

        List<Map<String, String>> testData = new ArrayList<>();
        tableName = tableName.toLowerCase();
        File input = new File("src/main/resources/" + tableName + ".csv");

        try {
            CsvSchema csv = CsvSchema.emptySchema().withHeader();
            CsvMapper csvMapper = new CsvMapper();
            MappingIterator<Map<String, String>> mapMappingIterator = csvMapper.reader().forType(Map.class).with(csv).readValues(input);

            testData = mapMappingIterator.readAll();

        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Map<String, Object>> convertTable = new ArrayList<>();
        for (Map<String, String> eachRow : testData) {
            Map<String, Object> copied = new HashMap<>();
            for (String eachKey : eachRow.keySet()) {
                try {
                    Double.parseDouble(eachRow.get(eachKey));
                    copied.put(eachKey, Double.parseDouble(eachRow.get(eachKey)));
                } catch (Exception e) {
                    copied.put(eachKey, eachRow.get(eachKey));
                }
            }
            convertTable.add(copied);
        }



        return convertTable;
    }
}
