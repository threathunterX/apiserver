package com.threathunter.web.manager.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
public class WidgetTest {
    @Test
    public void test() {

        List<String> params = new ArrayList<>();
        params.add("str1");
        params.add("str2");
        params.add("str3");
        params.add("str4");
        params.add("str5");
        params.add("str6");
        StringBuilder str = new StringBuilder(params.toString());
        System.out.println(str);

        //str1, str2, str3, str4, str5, str6
    }

    @Test
    public void testGson() {
        String str = "[str1, str2, str3, str4, str5, str6]";
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        List list = gson.fromJson(str, List.class);
        assertThat(list).isNotNull();
        str = "[str1,str2,str3,str4,str5,str6]";
        List list2 = gson.fromJson(str, List.class);
        assertThat(list2).isNotNull();
        String s = gson.toJson(list);
        assertThat(s).isEqualTo(str);
    }

    @Test
    public void testInstanceOf() {
        assertFalse(null instanceof Map);
    }
}
