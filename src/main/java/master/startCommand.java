package master;

import org.json.JSONArray;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class startCommand extends TelegramLongPollingBot {

    String email="";
    public static String getMd5(String input) {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] randomString() {
        String[] res = new String[2];
        int n = 4;
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        String md = sb.toString();
        md = md + "@promail1.net";
        res[0] = md;
        md = getMd5(md);
        res[1] = md;
        return res;
    }

    public static String emailGenerator() throws IOException, InterruptedException {

        String[] result = randomString();
        System.out.println(result[0]);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://privatix-temp-mail-v1.p.rapidapi.com/request/mail/id/" + result[1] + "/"))
                .header("X-RapidAPI-Host", "HOST")
                .header("X-RapidAPI-Key", "KEY")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return result[0];
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        String hash = getMd5("a2cs@promail1.net");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://privatix-temp-mail-v1.p.rapidapi.com/request/mail/id/" + hash + "/"))
                .header("X-RapidAPI-Host", "HOST")
                .header("X-RapidAPI-Key", "KEY")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        String js = response.body().toString();
        JSONArray ja = new JSONArray(js);
        System.out.println(ja);



    }



    @Override
    public String getBotUsername() {
        return System.getenv("USER_N");
    }

    @Override
    public String getBotToken() {
        return System.getenv("BOT_KEY");
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            if(update.getMessage().getText().equals("/start")) {

                SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(" \uD83D\uDCE7 Welcome to disposable email service \uD83D\uDCE7.\n ⏰ This bot will generate temporary emails that will last for a short duration.\n" +
                        "\uD83D\uDE0AForget about spam, advertising mailings, hacking and attacking robots.\n✨Keep your real mailbox clean and secure. " +
                        "\n\uD83D\uDD12Temp Mail Bot provides temporary, secure, anonymous, free, disposable email address.\n" +
                        "The following are the commands available\n" +
                        "\uD83D\uDD34/start - start the bot from the beginning\n" +
                        "\uD83D\uDD34/email - generate a new email disposing of the previous email\n" +
                        "\uD83D\uDD34/message - see the message delivered to the email ");

                try {
                    execute(message); // Call method to send the message
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            else if(update.getMessage().getText().equals("/email")) {


                try {
                     email = emailGenerator();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText("The previous email if generated is now disposed \uD83D\uDDD1️.\n" +
                        " \uD83D\uDCE7 The new email generated is "+email);


                try {
                    execute(message); // Call method to send the message
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }




        }
    }


}
