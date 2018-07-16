package com.d53;

import com.google.common.collect.Maps;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        Map<String, String> previous = new HashMap();
        previous.put("google.com", "hello world");
        previous.put("yandex.ru", "searching");
        previous.put("mail.ru", "making games p2w");
        previous.put("gmail.com", "mails here");

        Map<String, String> current = new HashMap();
        current.put("google.com", "hello, world!");
        current.put("yandex.ru", "searching");
        current.put("youtube.com", "new videos");
        current.put("gmail.com", "mails there");
        current.put("stackoverflow.com", "where coders are born");

        Map oldSites = Maps.difference(previous, current).entriesOnlyOnLeft();
        Map changedSites = Maps.difference(previous, current).entriesDiffering();
        Map newSites = Maps.difference(previous, current).entriesOnlyOnRight();

        SendMail(oldSites, changedSites, newSites);
    }

    public static String MakeReport (Map oldSites, Map changedSites, Map newSites) {
        String Report = "<p>Здравствуйте, дорогая и.о. секретаря!</p>";

        boolean nothingChanged = oldSites.isEmpty() && changedSites.isEmpty() && newSites.isEmpty();

        if (!nothingChanged) {
            Report += "<p>За последние сутки во вверенных Вам сайтах произошли следущие изменения:</p>";

            if (!oldSites.isEmpty()) {
                Report += "<p>Исчезли следующие страницы:";
                for (Object key : oldSites.keySet()) {
                    Report += "<br>" + key;
                }
                Report += "</p>";
            }

            if (!newSites.isEmpty()) {
                Report += "<p>Появились следующие новые страницы:";
                for (Object key : newSites.keySet()) {
                    Report += "<br>" + key;
                }
                Report += "</p>";
            }

            if (!changedSites.isEmpty()) {
                Report += "<p>Изменились следующие страницы:";
                for (Object key : changedSites.keySet()) {
                    Report += "<br>" + key;
                }
                Report += "</p>";
            }
        } else {
            Report += "<p>За последние сутки во вверенных Вам сайтах изменений не произошло.</p>";
        }

        Report += "<p>С уважением,</p>" +
                "<p>Автоматизированная система мониторинга.</p>";

        return Report;
    }

    public static void SendMail(Map oldSites, Map changedSites, Map newSites){
        FileInputStream fStream;
        Properties conf = new Properties();

        String senderEmail = new String();
        String senderPassword = new String();
        String recipientEmail = new String();

        try {
            fStream = new FileInputStream("src/main/resources/config.properties");
            conf.load(fStream);
            senderEmail = conf.getProperty("fromEmail");
            senderPassword = conf.getProperty("password");
            recipientEmail = conf.getProperty("toEmail");
            fStream.close();
        } catch (IOException e) {
            System.err.println("Ошибка: файл не найден!");
            return;
        }

        final String fromEmail = senderEmail; // requires valid gmail id
        final String password = senderPassword; // correct password for gmail id
        final String toEmail =  recipientEmail; // can be any email id

        System.out.println("TLSEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);

        String bodyContent = MakeReport(oldSites, changedSites, newSites);

        EmailUtil.sendEmail(session, toEmail,"Суточный отчет", bodyContent);
    }
}