package com.qcloud.ut_result_sender.ut_result_parse;

import com.qcloud.ut_result_sender.meta.CodeCoverage;
import org.w3c.dom.Element;

import java.io.File;

public class CoberturaParser implements Parser<CodeCoverage> {

    @Override
    public CodeCoverage parse(File report) {
        CodeCoverage coverage = new CodeCoverage();

        Element rootEle = Util.loadXml(report.getAbsolutePath());

        coverage.setLineRate(Double.parseDouble(rootEle.getAttribute("line-rate")));
        coverage.setBranchRate(Double.parseDouble(rootEle.getAttribute("branch-rate")));

        return coverage;
    }
}
