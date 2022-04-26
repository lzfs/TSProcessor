package utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements the reader interface.
 * It can be used to read a config-file.
 */
public class ConfigReader implements Reader {
    private final static Logger LOGGER = Logger.getLogger("ConfigReaderLogger");

    /**
     * This method reads a config-file a specified location.
     *
     * @param path the location of the properties file.
     * @return a properties object with all the found attributes.
     */
    @Override
    public Properties read(String path) {
        Properties properties = new Properties();

        try (FileInputStream fis = new FileInputStream(path)) {
            properties.load(fis);
        }
        catch (FileNotFoundException ex) {
            LOGGER.log(Level.INFO, "file not found");
        }
        catch (IOException ex) {
            LOGGER.log(Level.INFO, "io exception");
        }
        return properties;
    }
}
