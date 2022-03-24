package model;

import java.util.ArrayList;
import java.util.List;

public class KinectRecord implements Record<KinectFrame> {
    private String name;
    private List<KinectFrame> frames;
    private boolean consider;

    public KinectRecord(String name, List<KinectFrame> frames, boolean consider) {
        this.name = name;
        this.frames = frames;
        this.consider = consider;
    }

    public KinectRecord(String name, boolean consider) {
        this.name = name;
        this.frames = new ArrayList<>();
        this.consider = consider;
    }

    public String getName() {
        return name;
    }

    @Override
    public List<KinectFrame> getFrames() {
        return frames;
    }

    @Override
    public void setFrames(List<KinectFrame> frames) {
        this.frames = frames;
    }

    @Override
    public boolean isConsider() {
        return consider;
    }

    @Override
    public void setConsider(boolean consider) {
        this.consider = consider;
    }

    @Override
    public void addFrame(KinectFrame frame) {
        this.frames.add(frame);
    }
}
