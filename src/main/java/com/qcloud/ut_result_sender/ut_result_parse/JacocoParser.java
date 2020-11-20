package com.qcloud.ut_result_sender.ut_result_parse;

import com.qcloud.ut_result_sender.meta.CodeCoverage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

public class JacocoParser implements Parser<CodeCoverage> {

    public CodeCoverage parse(File report) {
        Element rootElement = Util.loadXml(report.getAbsolutePath());

        NodeList counterList = rootElement.getChildNodes();
        CodeCoverage coverage = new CodeCoverage();

        for (int i = 0; i < counterList.getLength(); i++) {
            Node node = counterList.item(i);
            if (node.getNodeName().equals("counter")) {
                String type = node.getAttributes().getNamedItem("type").getNodeValue();

                if (type.equalsIgnoreCase("BRANCH")) {
                    coverage.setBranchCovered(Long.parseLong(node.getAttributes().getNamedItem("covered").getNodeValue()),
                            Long.parseLong(node.getAttributes().getNamedItem("missed").getNodeValue()));
                } else if (type.equalsIgnoreCase("LINE")) {
                    coverage.setLineCovered(Long.parseLong(node.getAttributes().getNamedItem("covered").getNodeValue()),
                            Long.parseLong(node.getAttributes().getNamedItem("missed").getNodeValue()));
                } else if (type.equalsIgnoreCase("METHOD")) {
                    coverage.setMethodCovered(Long.parseLong(node.getAttributes().getNamedItem("covered").getNodeValue()),
                            Long.parseLong(node.getAttributes().getNamedItem("missed").getNodeValue()));
                }
            }
        }

        return coverage;
    }
}
