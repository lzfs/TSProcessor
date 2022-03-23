package model;

import java.util.List;

public abstract class Record {
    String name;
    List<Frame> frames;
    boolean consider;

    public abstract void addFrame(Frame frame);
}
