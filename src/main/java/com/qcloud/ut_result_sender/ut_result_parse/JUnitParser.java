package com.qcloud.ut_result_sender.ut_result_parse;

import com.qcloud.ut_result_sender.meta.UnitTestSummary;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

public class JUnitParser implements Parser<UnitTestSummary> {

    private static final String tests_attr = "tests";
    private static final String errors_attr = "errors";
    private static final String skipped_attr = "skipped";
    private static final String failures_attr = "failures";
    private static final String time_attr = "time";

    public UnitTestSummary parse(File report) {
        Element rootElement = Util.loadXml(report.getAbsolutePath() );
        if (rootElement == null) {
            return null;
        }

        UnitTestSummary summary = new UnitTestSummary();

        if (rootElement.getTagName().equals("testsuites") || rootElement.getTagName().equals("testsuite")) {
            parseTestSuite(rootElement, summary);
        }

        if (summary.getTests() == 0 && rootElement.getTagName().equals("testsuites")) {
            NodeList list = rootElement.getElementsByTagName("testsuite");
            if (list.getLength() > 0) {
                if (list.item(0).getNodeType() == Node.ELEMENT_NODE) {
                    parseTestSuite((Element) list.item(0), summary);
                }

            }
        }

        return summary;
    }

    private static void parseTestSuite(Element rootElement, UnitTestSummary summary) {
        if (rootElement.hasAttribute(tests_attr)) {
            String testAttrStr = rootElement.getAttribute(tests_attr);
            summary.setTests(Long.parseLong(testAttrStr));
        }
        if (rootElement.hasAttribute(errors_attr)) {
            String errorAttrStr = rootElement.getAttribute(errors_attr);
            summary.setErrors(Long.parseLong(errorAttrStr));
        }
        if (rootElement.hasAttribute(skipped_attr)) {
            String skippedAttrStr = rootElement.getAttribute(skipped_attr);
            summary.setSkipped(Long.parseLong(skippedAttrStr));
        }
        if (rootElement.hasAttribute(failures_attr)) {
            String failAttrStr = rootElement.getAttribute(failures_attr);
            summary.setFailures(Long.parseLong(failAttrStr));
        }
        if (rootElement.hasAttribute(time_attr)) {
            String timeAttrStr = rootElement.getAttribute(time_attr);
            summary.setTime(Double.parseDouble(timeAttrStr));
        }
    }
}
