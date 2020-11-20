package com.qcloud.ut_result_sender.ut_result_parse;

import com.qcloud.ut_result_sender.meta.UnitTestSummary;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JSTestParser implements Parser<UnitTestSummary> {

    @Override
    public UnitTestSummary parse(File report) {
        UnitTestSummary summary = new UnitTestSummary();
        int failCount = 0, passCount = 0;
        boolean getResult = false;

        try (BufferedReader br = new BufferedReader(new FileReader(report))) {
            String line;
            while ((line = br.readLine()) != null) {
                // process the line.
                if (getResult) {
                    JSONObject jsonObject = new JSONObject(line);
                    summary.setFailures(jsonObject.getInt("failed"));
                    summary.setTests(jsonObject.getInt("total"));
                } else if (line.trim().equalsIgnoreCase("==[TESTING-ENDS]==")) {
                    getResult = true;
                }
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }

        return summary;
    }
}
