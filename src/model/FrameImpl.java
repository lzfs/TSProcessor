package model;

import java.util.HashMap;
import java.util.Map;

public class FrameImpl implements Frame<RecordImpl> {
    private RecordImpl record;
    private Map<String, String> attributes = new HashMap<>();

    public FrameImpl(Map<String, String> attributesMap, RecordImpl record) {
        this.attributes = attributesMap;
        this.record = record;
    }

    @Override
    public RecordImpl getRecord() {
        return record;
    }

    public String getValue(String key) {
        return this.attributes.get(key);
    }
}
