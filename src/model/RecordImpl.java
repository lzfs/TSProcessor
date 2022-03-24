package model;

import java.util.ArrayList;
import java.util.List;

public class RecordImpl implements Record<FrameImpl> {
    private String name;
    private List<FrameImpl> frames;
    private boolean consider;

    public RecordImpl(String name, List<FrameImpl> frames, boolean consider) {
        this.name = name;
        this.frames = frames;
        this.consider = consider;
    }

    public RecordImpl(String name, boolean consider) {
        this.name = name;
        this.frames = new ArrayList<>();
        this.consider = consider;
    }

    public String getName() {
        return name;
    }

    @Override
    public List<FrameImpl> getFrames() {
        return frames;
    }

    @Override
    public void setFrames(List<FrameImpl> frames) {
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
    public void addFrame(FrameImpl frame) {
        this.frames.add(frame);
    }
}
