package com.qcloud.ut_result_sender.ut_result_parse;

import java.io.File;

public interface LanguageParser {

    Boolean isReportParseable(File report);

    Object parseReport(File report);

}
