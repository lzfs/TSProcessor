package utility;

import model.KinectFrame;
import model.KinectRecord;

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

public class KinectDataReader implements Reader {
    private final static Logger LOGGER = Logger.getLogger("KinectDataReaderLogger");
    private List<KinectRecord> records = new ArrayList<>();
    private final String prefix;
    List<String> attributes;

    public KinectDataReader(String prefix, List<String> attributes) {
        this.prefix = prefix;
        this.attributes = attributes;
    }

    @Override
    public List<KinectRecord> read(String path) {
        listFiles(new File(prefix + path));
        return this.records;
    }

    private void listFiles(final File directory) {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            KinectRecord record = new KinectRecord(file.getName(), true);
            List<KinectFrame> frames = readFrames(file, record);
            record.setFrames(frames);
            this.records.add(record);
        }
    }

    private List<KinectFrame> readFrames(File file, KinectRecord record) {
        List<KinectFrame> frames = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("#");
                Map<String, String> attributesMap = new HashMap<>();
                for (String attribute : this.attributes) {
                    attributesMap.put(attribute, parts[this.attributes.indexOf(attribute)]);
                }
                frames.add(new KinectFrame(attributesMap, record));
            }
            scanner.close();
        }
        catch (FileNotFoundException e) {
            LOGGER.log(Level.INFO, "file not found");
        }
        return frames;
    }
}
