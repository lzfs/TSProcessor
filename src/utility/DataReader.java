package utility;

import model.FrameImpl;
import model.RecordImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements the reader interface.
 * It can be used to read time series data.
 */
public class DataReader implements Reader {
    private final static Logger LOGGER = Logger.getLogger("DataReaderLogger");
    /**
     * The list of the records that the reader found.
     */
    private List<RecordImpl> records = new ArrayList<>();
    /**
     * The prefix of the path.
     * e.g. "D:".
     */
    private final String prefix;
    /**
     * The separator that separates the attributes in the files.
     * e.g. "#'.
     */
    private final String separator;
    /**
     * The attributes that this dataset provides.
     * e.g. [timestamp, kinectId, recordId, x, z, engaged].
     */
    private List<String> attributes;

    /**
     * Whether if every third frame should be ignored to increase performance.
     */
    private boolean skipFrames = false;

    public DataReader(String prefix, String separator, List<String> attributes, boolean skipFrames) {
        this.prefix = prefix;
        this.separator = separator;
        this.attributes = attributes;
        this.skipFrames = skipFrames;
    }

    /**
     * This method reads the time series data.
     *
     * @param path the location of the directory with the data.
     * @return a list of the found records.
     */
    @Override
    public List<RecordImpl> read(String path) {
        listFiles(new File(prefix + path));
        return this.records;
    }

    /**
     * This method adds the found frames to a new record and adds this record to the records list.
     *
     * @param directory the directory to search in.
     */
    private void listFiles(final File directory) {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            RecordImpl record = new RecordImpl(file.getName(), true);
            List<FrameImpl> frames = readFrames(file, record);
            record.setFrames(frames);
            this.records.add(record);
        }
    }

    /**
     * This method reads and creates the frames of each record.
     *
     * @param file   the file to search for frames in it.
     * @param record the record the frames belongs to.
     * @return the newly created frame.
     */
    private List<FrameImpl> readFrames(File file, RecordImpl record) {
        List<FrameImpl> frames = new ArrayList<>();
        int counter = 0;

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // replace "," with "." to avoid issues with decimal numbers
                line = line.replaceAll(",", ".");
                // split the string at each found separator to identidy each attribute
                String[] parts = line.split(separator);
                Map<String, String> attributesMap = new HashMap<>();
                // add all found attributes for this frame to a map
                for (String attribute : this.attributes) {
                    attributesMap.put(attribute, parts[this.attributes.indexOf(attribute)]);
                }
                if (this.skipFrames) {
                    if (counter < 2) {
                        frames.add(new FrameImpl(attributesMap, record));
                        counter += 1;
                    }
                    else {
                        // skip this frame
                        counter = 0;
                    }
                }
                else {
                    frames.add(new FrameImpl(attributesMap, record));
                }
            }
            scanner.close();
        }
        catch (FileNotFoundException e) {
            LOGGER.log(Level.INFO, "file not found");
        }
        return frames;
    }
}
