package com.qcloud.ut_result_sender.ut_result_parse;

import com.qcloud.ut_result_sender.Env;
import com.qcloud.ut_result_sender.meta.CodeCoverage;
import com.qcloud.ut_result_sender.meta.UnitTestSummary;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.github.biezhi.ome.OhMyEmail;
import io.github.biezhi.ome.SendMailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Reporter {

    private static final Logger log = LoggerFactory.getLogger(Reporter.class);

    private String artifactsPath;
    private Env environment;

    private UnitTestSummary totalInfo = new UnitTestSummary();

    private Map<String, UnitTestSummary> utSummary = new TreeMap<>();
    private Map<String, CodeCoverage> codeCoverage = new TreeMap<>();

    public Reporter(String artifactsPath, Env environment) {
        super();
        this.artifactsPath = artifactsPath;
        this.environment = environment;
    }

    public void scanLocalFolder() {
        final URI parentURI = new File(artifactsPath).toURI();
        SimpleFileVisitor<Path> finder = new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                String relativePath = parentURI.relativize(file.toUri()).getPath();
                String[] pathParts = relativePath.split("/");
                String language = pathParts[0];
                String version = "";
                String buildNumber = "";
                if (pathParts.length > 1) {
                    version = pathParts[1];
                }
                if (pathParts.length > 2) {
                    buildNumber = pathParts[2];
                }

                LanguageParser parser = ParserFactory.findParser(language);
                if (parser != null && parser.isReportParseable(file.toFile())) {
                    Object result = parser.parseReport(file.toFile());

                    if (result instanceof CodeCoverage) {
                        CodeCoverage coverage = (CodeCoverage) result;
                        coverage.setConsoleUrl(environment.getSDKConsoleUrl(language, buildNumber));
                        codeCoverage.put(language, coverage);

                    } else if (result instanceof UnitTestSummary) {
                        UnitTestSummary summary = (UnitTestSummary) result;
                        summary.setVersion(version);
                        summary.setConsoleUrl(environment.getSDKConsoleUrl(language, buildNumber));
                        utSummary.put(language, summary);
                    }
                }

                return super.visitFile(file, attrs);
            }
        };

        try {
            java.nio.file.Files.walkFileTree(Paths.get(artifactsPath), finder);
        } catch (IOException e) {
            log.error("walk file tree error", e);
        }
    }

    private void printStaticsInfo(String key, UnitTestSummary staticsInfo) {
        String printStr = String.format(
                "%s : [tests: %d], [failures: %d], [errors: %d], [skipped: %d], [time: %f]</br>",
                key, staticsInfo.getTests(), staticsInfo.getFailures(), staticsInfo.getErrors(),
                staticsInfo.getSkipped(), staticsInfo.getTime());
        System.out.println(printStr);
    }

    private void buildEmailContent() {
        // 创建一个合适的Configration对象
        try {
            Configuration configuration = new Configuration();
            configuration.setDirectoryForTemplateLoading(new File("HtmlTemplate"));
            configuration.setObjectWrapper(new DefaultObjectWrapper());
            configuration.setDefaultEncoding("UTF-8"); // 这个一定要设置，不然在生成的页面中 会乱码
            Template template = configuration.getTemplate("mail.tpl.html");

            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("buildNumber", environment.buildNumber);
            paramMap.put("buildConsoleUrl", environment.getConsoleUrl());
            paramMap.put("testTime", currentTime);
            paramMap.put("totalInfo", totalInfo);
            paramMap.put("utSummary", utSummary);
            paramMap.put("codeCoverage", codeCoverage);

            File outputDir = new File("output");
            outputDir.mkdirs();
            Writer writer = new OutputStreamWriter(
                    new FileOutputStream("output/email_content.html"), "UTF-8");
            template.process(paramMap, writer);
            writer.close();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        // 获取或创建一个模版。
    }

    private String getEmailContent() {
        StringBuffer strBuf = new StringBuffer();
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(new FileInputStream("output/email_content.html"), "UTF-8");
            BufferedReader reader = new BufferedReader(in);
            String line = reader.readLine();
            while (line != null) {
                strBuf.append(line);
                strBuf.append("\n");
                line = reader.readLine();
            }
            reader.close();
            in.close();
        } catch (UnsupportedEncodingException e) {
            log.error("getEmailContent", e);
        } catch (FileNotFoundException e) {
            log.error("getEmailContent", e);
        } catch (IOException e) {
            log.error("getEmailContent", e);
        }
        return strBuf.toString();
    }

    private void sendEmail() {

        String subject = "[OneBox] COS SDK 质量报告 " + Util.getCurrentDay();
        // 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
        String content = getEmailContent();
        String[] recipients = environment.emailRecipients.split(",");
        String[] ccs = environment.emailCCs.split(",");

        try {
            // 不要使用SimpleEmail,会出现乱码问题
            HtmlEmail email = new HtmlEmail();
            email.setSSLOnConnect(true);
            // 这里是SMTP发送服务器的名字：
            email.setHostName(environment.emailServer);
            // 字符编码集的设置
            email.setCharset("UTF-8");
            // 收件人的邮箱
            for (String emailSendTo : recipients) {
                log.info("add email receiver {}", emailSendTo);
                email.addTo(emailSendTo);
            }
            // CC 收件人的邮箱
            for (String cc : ccs) {
                log.info("add email cc {}", cc);
                email.addCc(cc);
            }
            // 发送人的邮箱
            email.setFrom(environment.emailName, environment.emailFrom);
            // 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和密码
            email.setAuthentication(environment.emailName, environment.emailPassed);

            email.setSubject(subject);
            email.setHtmlMsg(content);

            // 发送
            log.info("ready to send email");
            email.send();
            log.info("send success");
        } catch (Exception e) {
            log.error("send email failed", e);
        }

//        OhMyEmail.config(OhMyEmail.SMTP_QQ(true),
//                "584040188@qq.com",
//                "wxyhxkexomisbbac");
//
//        try {
//            OhMyEmail.subject("[OneBox] SDK UT REPORT")
//                    .from("iengineering2020@qq.com")
//                    .to("wjielai@tencent.com")
//                    .html(getEmailContent())
//                    .send();
//        } catch (SendMailException e) {
//            e.printStackTrace();
//        }
    }

    public void run() {
        scanLocalFolder();
        utSummary = Util.sortByValue(utSummary);
        codeCoverage = Util.sortByValue(codeCoverage);
        buildEmailContent();
        sendEmail();
    }
}
