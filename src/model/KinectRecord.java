package model;

import java.util.List;

public class KinectRecord extends Record {
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
        this.consider = consider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<KinectFrame> getFrames() {
        return frames;
    }

    public void setFrames(List<KinectFrame> frames) {
        this.frames = frames;
    }

    public boolean isConsider() {
        return consider;
    }

    public void setConsider(boolean consider) {
        this.consider = consider;
    }

    @Override
    public void addFrame(Frame frame) {
        // TODO no cast
        this.frames.add((KinectFrame) frame);
    }
}
