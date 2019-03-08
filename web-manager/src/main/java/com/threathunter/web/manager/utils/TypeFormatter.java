package com.threathunter.web.manager.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
public class TypeFormatter {
    public static List<Integer> convertDoubleToPercentageInteger(List<Double> doubles) {
        List<Integer> ret = new ArrayList<>();
        for (Double d : doubles) {
            Integer i = convertDoubleToPercentageInteger(d);
            ret.add(i);
        }
        return ret;
    }

    public static Integer convertDoubleToPercentageInteger(Double item) {
        Double v = item * 100;
        return v.intValue();
    }
}
