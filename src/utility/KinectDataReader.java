package utility;

import model.KinectFrame;
import model.KinectRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KinectDataReader implements Reader {
    private final static Logger LOGGER = Logger.getLogger("KinectDataReaderLogger");
    private List<KinectRecord> records = new ArrayList<>();
    private final String prefix;

    public KinectDataReader(String prefix) {
        this.prefix = prefix;
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
                // 0 = timestamp, 1 = kinectId, 2 = recordId, 3 = x, 4 = z, 5 = engaged, 6 = record
                frames.add(new KinectFrame(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], record));
            }
            scanner.close();
        }
        catch (FileNotFoundException e) {
            LOGGER.log(Level.INFO, "file not found");
        }
        return frames;
    }
}
