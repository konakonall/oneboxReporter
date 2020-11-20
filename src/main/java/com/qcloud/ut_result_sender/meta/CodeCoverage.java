package com.qcloud.ut_result_sender.meta;

public class CodeCoverage implements Comparable<CodeCoverage> {

    private long lineMissed;
    private long lineCovered;
    private double lineRate;

    private long branchMissed;
    private long branchCovered;
    private double branchRate;

    private long methodMissed;
    private long methodCovered;
    private double methodRate;

    private String consoleUrl = "";

    @Override
    public int compareTo(CodeCoverage o) {
        return lineRate > o.lineRate ? -1 : 1;
    }

    public void setLineCovered(long lineCovered, long lineMissed) {
        this.lineCovered = lineCovered;
        this.lineMissed = lineMissed;
        this.lineRate = (double)lineCovered / (lineCovered + lineMissed);
    }

    public void setBranchCovered(long branchCovered, long branchMissed) {
        this.branchCovered = branchCovered;
        this.branchMissed = branchMissed;
        this.branchRate = (double)branchCovered / (branchCovered + branchMissed);
    }

    public void setMethodCovered(long methodCovered, long methodMissed) {
        this.methodCovered = methodCovered;
        this.methodMissed = methodMissed;
        this.methodRate =  (double)methodCovered / (methodCovered + methodMissed);
    }

    public void setLineRate(double lineRate) {
        this.lineRate = lineRate;
    }

    public void setBranchRate(double branchRate) {
        this.branchRate = branchRate;
    }

    public void setMethodRate(double methodRate) {
        this.methodRate = methodRate;
    }


    public String getLineRate() {
        return Formatter.toRatePercent(lineRate);
    }

    public double getLineRateNumerical() {
        return lineRate;
    }

    public String getBranchRate() {
        return Formatter.toRatePercent(branchRate);
    }

    public double getBranchRateNumerical() {
        return branchRate;
    }

    public String getMethodRate() {
        return Formatter.toRatePercent(methodRate);
    }

    public double getMethodRateNumerical() {
        return methodRate;
    }



    public String getConsoleUrl() {
        return consoleUrl;
    }

    public void setConsoleUrl(String consoleUrl) {
        this.consoleUrl = consoleUrl;
    }
}
