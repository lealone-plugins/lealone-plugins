/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.orm.property;

import java.sql.Timestamp;

import com.lealone.db.value.Value;
import com.lealone.db.value.ValueTimestamp;

import com.lealone.plugins.orm.Model;
import com.lealone.plugins.orm.format.JsonFormat;
import com.lealone.plugins.orm.format.TimestampFormat;

/**
 * Property for java sql Timestamp.
 */
public class PTimestamp<M extends Model<M>> extends PBaseDate<M, Timestamp> {

    public PTimestamp(String name, M model) {
        super(name, model);
    }

    @Override
    protected TimestampFormat getValueFormat(JsonFormat format) {
        return format.getTimestampFormat();
    }

    @Override
    protected Value createValue(Timestamp value) {
        return ValueTimestamp.get(value);
    }

    @Override
    protected void deserialize(Value v) {
        value = v.getTimestamp();
    }
}
