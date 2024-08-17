package com.spring.to_do_app.constants;

import lombok.Getter;

import java.net.URL;

public class Mails {

    @Getter
    public static class Email{
        String to;
        String subject;
        String body;

        Email(String to, String subject, String body){
            this.to = to;
            this.subject = subject;
            this.body = body;
        }
    }

    public static Email registerationToken(String email, String token) {
        String subject = "Registration of the user";
        String url = "http://localhost:5173/verification/" + email  + "/" + token;
        try{
            URL URL = new URL(url);
            String body = "You have registered to our application and we have send you the token for verification\n"
                + "Click on this link " + URL + " to verify your account";
            return new Email(email,subject,body);
        }catch (Exception e){
            return null;
        }
    }

    public static Email addedToGroup(String to, String project, String author){
        String subject = "Added to the group";
        String body = "You have been added to the project " + project + "by\n" + author + ".";
        return new Email(to,subject,body);
    }
}
