package com.qcloud.ut_result_sender.ut_result_parse;

import com.qcloud.ut_result_sender.meta.CodeCoverage;
import com.qcloud.ut_result_sender.meta.UnitTestSummary;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface Parser<T> {

    T parse(File report);
}

class ParseConfig<T> {
    final String namePattern;
    final Parser<T> parser;

    ParseConfig(String namePattern, Parser parser) {
        this.namePattern = namePattern;
        this.parser = parser;
    }
}

class GroupParser implements LanguageParser {

    protected final List<ParseConfig<?>> parseConfigs;

    GroupParser(ParseConfig<UnitTestSummary> unitTestSummaryParseConfig) {
        parseConfigs = new ArrayList<>();
        parseConfigs.add(unitTestSummaryParseConfig);
    }

    GroupParser(ParseConfig<UnitTestSummary> unitTestSummaryParseConfig,
                ParseConfig<CodeCoverage> codeCoverageParseConfig) {
        parseConfigs = new ArrayList<>();
        parseConfigs.add(unitTestSummaryParseConfig);
        parseConfigs.add(codeCoverageParseConfig);
    }

    @Override
    public Boolean isReportParseable(File report) {
        for (ParseConfig<?> config : parseConfigs) {
            if (report.getName().matches(config.namePattern)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Object parseReport(File report) {
        for (ParseConfig<?> config : parseConfigs) {
            if (report.getName().matches(config.namePattern)) {
                return config.parser.parse(report);
            }
        }

        return null;
    }
}
