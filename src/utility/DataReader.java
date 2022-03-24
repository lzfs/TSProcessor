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

public class DataReader implements Reader {
    private final static Logger LOGGER = Logger.getLogger("DataReaderLogger");
    private List<RecordImpl> records = new ArrayList<>();
    private final String prefix;
    private final String separator;
    List<String> attributes;

    public DataReader(String prefix, String separator, List<String> attributes) {
        this.prefix = prefix;
        this.separator = separator;
        this.attributes = attributes;
    }

    @Override
    public List<RecordImpl> read(String path) {
        listFiles(new File(prefix + path));
        return this.records;
    }

    private void listFiles(final File directory) {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            RecordImpl record = new RecordImpl(file.getName(), true);
            List<FrameImpl> frames = readFrames(file, record);
            record.setFrames(frames);
            this.records.add(record);
        }
    }

    private List<FrameImpl> readFrames(File file, RecordImpl record) {
        List<FrameImpl> frames = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                line = line.replaceAll(",", ".");
                String[] parts = line.split(separator);
                Map<String, String> attributesMap = new HashMap<>();
                for (String attribute : this.attributes) {
                    attributesMap.put(attribute, parts[this.attributes.indexOf(attribute)]);
                }
                frames.add(new FrameImpl(attributesMap, record));
            }
            scanner.close();
        }
        catch (FileNotFoundException e) {
            LOGGER.log(Level.INFO, "file not found");
        }
        return frames;
    }
}
