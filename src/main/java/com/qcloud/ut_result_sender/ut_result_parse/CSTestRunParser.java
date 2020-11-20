package com.qcloud.ut_result_sender.ut_result_parse;

import com.qcloud.ut_result_sender.meta.UnitTestSummary;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

public class CSTestRunParser implements Parser<UnitTestSummary> {

    @Override
    public UnitTestSummary parse(File report) {
        Element rootElement = Util.loadXml(report.getAbsolutePath());

        NodeList children = rootElement.getChildNodes();
        UnitTestSummary summary = new UnitTestSummary();

        for (int i = 0; i < children.getLength(); i++) {
            if ("ResultSummary".equalsIgnoreCase(children.item(i).getNodeName())) {
                NodeList subChildren = children.item(i).getChildNodes();
                for (int j = 0; j < subChildren.getLength(); j++) {
                    if ("Counters".equalsIgnoreCase(subChildren.item(j).getNodeName())) {
                        Node counters = subChildren.item(j);
                        summary.setTests(Long.parseLong(counters.getAttributes().getNamedItem("total").getNodeValue()));
                        summary.setFailures(Long.parseLong(counters.getAttributes().getNamedItem("failed").getNodeValue()));
                        summary.setErrors(Long.parseLong(counters.getAttributes().getNamedItem("error").getNodeValue()));
                    }
                }
                break;
            }
        }

        return summary;
    }
}
