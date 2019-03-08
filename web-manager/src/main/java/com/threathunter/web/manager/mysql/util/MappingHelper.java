package com.threathunter.web.manager.mysql.util;

import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Helper converting list to map.
 *
 * @param <K> key
 * @param <V> value
 */
@Getter
public class MappingHelper<K, V> {
    private K key;
    private V value;

    /**
     * Return map from {@link MappingHelper} list.
     *
     * @param list DTO list
     * @param <K>  key
     * @param <V>  value
     * @return map
     */
    public static <K, V> Map<K, V> toMap(List<MappingHelper<K, V>> list) {
        if (list == null) {
            return Collections.emptyMap();
        }
        return list.parallelStream().collect(Collectors.toMap(MappingHelper::getKey, MappingHelper::getValue));
    }
}