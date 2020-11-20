package com.qcloud.ut_result_sender.ut_result_parse;

import com.qcloud.ut_result_sender.meta.CodeCoverage;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;

public class CloverCoverageParser implements Parser<CodeCoverage> {

    @Override
    public CodeCoverage parse(File report) {
        CodeCoverage coverage = new CodeCoverage();

        Element rootEle = Util.loadXml(report.getAbsolutePath());

        NodeList list = rootEle.getElementsByTagName("metrics");

        if (list.getLength() > 0) {
            Element metrics = (Element) list.item(0);

            long coveredstatements = Long.parseLong(metrics.getAttribute("coveredstatements"));
            long statements = Long.parseLong(metrics.getAttribute("statements"));

            long coveredmethods = Long.parseLong(metrics.getAttribute("coveredmethods"));
            long methods = Long.parseLong(metrics.getAttribute("methods"));

            coverage.setLineCovered(coveredstatements, statements - coveredstatements);
            coverage.setMethodCovered(coveredmethods, methods - coveredmethods);
        }

        return coverage;
    }
}
