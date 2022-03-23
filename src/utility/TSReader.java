package utility;

import model.KinectFrame;
import model.KinectRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TSReader implements Reader {
    private final static Logger LOGGER = Logger.getLogger("TSReaderLogger");
    private List<KinectRecord> records = new ArrayList<>();

    @Override
    public List<KinectRecord> read(String path) {
        listFiles(new File("D:" + path));
        return this.records;
    }

    private void listFiles(final File directory) {
        for (File file : directory.listFiles()) {
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
