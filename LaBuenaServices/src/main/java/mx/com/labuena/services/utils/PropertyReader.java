package mx.com.labuena.services.utils;

import com.google.api.server.spi.response.InternalServerErrorException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by moracl6 on 8/4/2016.
 */

public class PropertyReader {
    private static final Logger log = Logger.getLogger(PropertyReader.class.getName());
    public static final String WEB_INF = "WEB-INF/";

    private PropertyReader() {
    }

    public static String readProperty(String fileName, String property) throws InternalServerErrorException {
        try {
            Properties props = new Properties();
            String filePath = WEB_INF;
            filePath = filePath.concat(fileName);
            FileInputStream fis = new FileInputStream(filePath);
            props.load(fis);
            return props.getProperty(property);
        } catch (FileNotFoundException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new InternalServerErrorException(e);
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new InternalServerErrorException(e);
        }
    }
}
