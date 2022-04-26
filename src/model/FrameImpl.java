package model;

import java.util.Map;

/**
 * This class implements the frame interface.
 * It represents a frame with it parent record and its attributes.
 */
public class FrameImpl implements Frame<RecordImpl> {
    /**
     * The record this frame belongs to.
     */
    private RecordImpl record;
    /**
     * The attributes this frame has.
     */
    private Map<String, String> attributes;

    public FrameImpl(Map<String, String> attributesMap, RecordImpl record) {
        this.attributes = attributesMap;
        this.record = record;
    }

    @Override
    public RecordImpl getRecord() {
        return record;
    }

    /**
     * This method will return the value of a specified attribute.
     *
     * @param key the name of the attribute.
     * @return the value of the attribute.
     */
    @Override
    public String getValue(String key) {
        return this.attributes.get(key);
    }
}
