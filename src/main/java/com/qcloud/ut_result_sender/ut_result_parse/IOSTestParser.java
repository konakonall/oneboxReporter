package com.qcloud.ut_result_sender.ut_result_parse;

import com.qcloud.ut_result_sender.meta.CodeCoverage;
import org.json.JSONObject;

import java.io.File;

public class IOSTestParser implements Parser<CodeCoverage> {

    @Override
    public CodeCoverage parse(File report) {
        String content = Util.loadContent(report.getAbsolutePath());
        CodeCoverage coverage = new CodeCoverage();

        if (content.length() > 0) {
            JSONObject jsonObject = new JSONObject(content);
            double lineRate = jsonObject.getDouble("lineCoverage");
            coverage.setLineRate(lineRate);
        }

        return coverage;
    }
}
