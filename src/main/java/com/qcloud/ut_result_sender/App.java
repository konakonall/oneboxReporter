package com.qcloud.ut_result_sender;

import com.qcloud.ut_result_sender.ut_result_parse.Reporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("missing arguments: " + args);
        }

        String artifactsPath = args[0];
        String buildNumber = args[1];

        Properties prop = new Properties();
        File configFile = new File("onebox.properties");
        try {
            prop.load(new FileInputStream(configFile));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("cannot found properties");
        }

        Env env = new Env(
                Integer.parseInt(buildNumber),
                prop.getProperty("onebox.console"),
                prop.getProperty("onebox.email.server"),
                prop.getProperty("onebox.email.name"),
                prop.getProperty("onebox.email.passed"),
                prop.getProperty("onebox.email.from"),
                prop.getProperty("onebox.email.recipients"),
                prop.getProperty("onebox.email.cc")
        );

        Reporter taskExecutor = new Reporter(artifactsPath, env);
        taskExecutor.run();

    }
}
