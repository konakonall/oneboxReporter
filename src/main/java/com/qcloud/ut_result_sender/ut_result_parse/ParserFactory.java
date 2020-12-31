package com.qcloud.ut_result_sender.ut_result_parse;

import com.qcloud.ut_result_sender.meta.CodeCoverage;
import com.qcloud.ut_result_sender.meta.UnitTestSummary;

import java.util.Map;
import java.util.TreeMap;

public class ParserFactory {

    private static final Map<String, LanguageParser> parsers = new TreeMap<>();

    static {
        parsers.put("Android", new GroupParser(
                new ParseConfig<UnitTestSummary>("TEST-.*\\.xml", new JUnitParser()),
                new ParseConfig<CodeCoverage>("jacocoCreateNormalDebugCoverageReport.xml", new JacocoParser())
        ));

        parsers.put(".NET", new GroupParser(
                new ParseConfig<UnitTestSummary>("results.xml", new CSTestRunParser()),
                new ParseConfig<CodeCoverage>("coverage.cobertura.xml", new CoberturaParser())
        ));

        parsers.put("Golang", new GroupParser(
                new ParseConfig<UnitTestSummary>("ci-test.stdout", new GolangTestParser()),
                new ParseConfig<CodeCoverage>("cover.stdout", new GolangCoverParser())
        ));

        parsers.put("iOS", new GroupParser(
                new ParseConfig<UnitTestSummary>("junit.xml", new JUnitParser()),
                new ParseConfig<CodeCoverage>("cov.json", new IOSTestParser())
        ));

        parsers.put("Java", new GroupParser(
                new ParseConfig<UnitTestSummary>("TEST-.*\\.xml", new JUnitParser()),
                new ParseConfig<CodeCoverage>("coverage.xml", new CoberturaParser())
        ));

        parsers.put("Javascript", new GroupParser(
                new ParseConfig<UnitTestSummary>("log.txt", new JSTestParser())
        ));

        parsers.put("Node.js", new GroupParser(
                new ParseConfig<UnitTestSummary>("mocha.xml", new JUnitParser()),
                new ParseConfig<CodeCoverage>("cobertura-coverage.xml", new CoberturaParser())
        ));

        parsers.put("PHP", new GroupParser(
                new ParseConfig<UnitTestSummary>("report.xml", new JUnitParser()),
                new ParseConfig<CodeCoverage>("coverage.xml", new CloverCoverageParser())
        ));

        parsers.put("Python", new GroupParser(
                new ParseConfig<UnitTestSummary>("nosetests.xml", new JUnitParser()),
                new ParseConfig<CodeCoverage>("coverage.xml", new CoberturaParser())
        ));
    }

    public static LanguageParser findParser(String language) {
        return parsers.get(language);
    }
}
