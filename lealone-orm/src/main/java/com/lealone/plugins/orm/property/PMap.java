/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package com.lealone.plugins.orm.property;

import java.util.Map;

import com.lealone.db.value.Value;
import com.lealone.db.value.ValueMap;

import com.lealone.plugins.orm.Model;
import com.lealone.plugins.orm.format.JsonFormat;
import com.lealone.plugins.orm.format.MapFormat;
import com.lealone.plugins.orm.json.Json;

/**
 * Map property.
 */
public class PMap<M extends Model<M>, K, V> extends PBase<M, Map<K, V>> {

    private final Class<?> keyClass;

    public PMap(String name, M model, Class<?> keyClass) {
        super(name, model);
        this.keyClass = keyClass;
    }

    public Class<?> getKeyClass() {
        return keyClass;
    }

    @Override
    protected MapFormat<K, V> getValueFormat(JsonFormat format) {
        return format.getMapFormat();
    }

    @Override
    protected Value createValue(Map<K, V> values) {
        return ValueMap.get(values);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void deserialize(Value v) {
        if (v instanceof ValueMap) {
            this.value = (Map<K, V>) v.getObject();
        }
    }

    @Override
    protected void decodeAndSet(Object v, JsonFormat format) {
        v = Json.convertToMap(v, keyClass);
        super.decodeAndSet(v, format);
    }
}
