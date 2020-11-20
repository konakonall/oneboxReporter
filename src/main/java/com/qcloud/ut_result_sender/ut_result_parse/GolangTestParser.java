package com.qcloud.ut_result_sender.ut_result_parse;

import com.qcloud.ut_result_sender.meta.CodeCoverage;
import com.qcloud.ut_result_sender.meta.UnitTestSummary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GolangTestParser implements Parser<UnitTestSummary> {

    @Override
    public UnitTestSummary parse(File report) {
        UnitTestSummary summary = new UnitTestSummary();
        int failCount = 0, passCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(report))) {
            String line;
            while ((line = br.readLine()) != null) {
                // process the line.
                if (line.trim().startsWith("--- FAIL")) {
                    failCount++;
                } else if (line.trim().startsWith("--- PASS")) {
                    passCount++;
                }
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }

        summary.setFailures(failCount);
        summary.setTests(failCount + passCount);

        return summary;
    }
}

class GolangCoverParser implements Parser<CodeCoverage> {

    @Override
    public CodeCoverage parse(File report) {
        CodeCoverage coverage = new CodeCoverage();

        try (BufferedReader br = new BufferedReader(new FileReader(report))) {
            String line;
            while ((line = br.readLine()) != null) {
                // process the line.
                if (line.trim().startsWith("total:")) {
                    Matcher matcher = Pattern.compile("[0-9.]+%").matcher(line);
                    if (matcher.find()) {
                        String rateString = matcher.group(0).replace("%", "");
                        coverage.setLineRate(Double.parseDouble(rateString) / 100);
                        break;
                    }
                }
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }

        return coverage;
    }
}
