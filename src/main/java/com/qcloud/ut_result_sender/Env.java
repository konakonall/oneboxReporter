package com.qcloud.ut_result_sender;

public class Env {

    public final int buildNumber;

    public final String consoleHost;

    public final String emailServer;

    public final String emailFrom;

    public final String emailName;

    public final String emailPassed;

    public final String emailRecipients;

    public final String emailCCs;

    public Env(int buildNumber,
                String consoleHost,
               String emailServer,
               String emailName,
               String emailPassed,
               String emailFrom,
               String emailRecipients,
               String emailCCs) {
        this.buildNumber = buildNumber;
        this.consoleHost = consoleHost;
        this.emailServer = emailServer;
        this.emailFrom = emailFrom;
        this.emailName = emailName;
        this.emailPassed = emailPassed;
        this.emailRecipients = emailRecipients;
        this.emailCCs = emailCCs;
    }

    public String getConsoleUrl() {
        return String.format("%s/blue/organizations/jenkins/OneBox/detail/OneBox/%d/pipeline",
                consoleHost, buildNumber);
    }

    public String getSDKConsoleUrl(String lang, String langBuildNumber) {
        return String.format("%s/blue/organizations/jenkins/%s/detail/%s/%s/pipeline",
                consoleHost, lang, lang, langBuildNumber);
    }
}
