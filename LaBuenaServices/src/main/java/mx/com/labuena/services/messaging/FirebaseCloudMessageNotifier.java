package mx.com.labuena.services.messaging;

import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.appengine.repackaged.org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.com.labuena.services.utils.PropertyReader;

/**
 * Firebase Cloud Message notifier.
 * Created by moracl6 on 8/4/2016.
 */

public class FirebaseCloudMessageNotifier implements MessageNotifier {

    private static final Logger log = Logger.getLogger(FirebaseCloudMessageNotifier.class.getName());

    public void sendMessage(Message message) throws InternalServerErrorException {
        try {

            String apiKey = PropertyReader.readProperty("la_buena.properties", "FCM_SERVER_AUTHORIZATION_KEY");

            URL url = new URL("https://fcm.googleapis.com/fcm/send");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key=" + apiKey);

            conn.setDoOutput(true);

            OutputStreamWriter dataOutputStream = new OutputStreamWriter(conn.getOutputStream());

            ObjectMapper mapper = new ObjectMapper();
            String bodyPayLoad = mapper.writeValueAsString(message);

            dataOutputStream.write(bodyPayLoad);

            // send request
            dataOutputStream.flush();

            dataOutputStream.close();

            int responseCode = conn.getResponseCode();
            log.log(Level.INFO, "POST request send to " + url);
            log.log(Level.INFO, "Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            log.log(Level.INFO, response.toString());

        } catch (MalformedURLException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new InternalServerErrorException(e);
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new InternalServerErrorException(e);
        }
    }
}
