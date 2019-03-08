package com.threathunter.web.manager.page.analysis;

import com.threathunter.web.manager.service.PageAnalysisService;

import java.util.HashMap;
import java.util.Map;

public class TestPageanalysisClear {


    private static void report() {
        PageAnalysisService analysisService = new PageAnalysisService();
        Map<String, Integer> pageCounts = new HashMap<>();
        pageCounts.put("threathunter.com/product/b1", 4);
        pageCounts.put("threathunter.com/product/b2", 5);
        pageCounts.put("threathunter.com/product/b1/b1_detail", 3);
        pageCounts.put("threathunter.com/product", 10);
        pageCounts.put("threathunter.com1/produ/c2", 7);
        analysisService.buildUrls(pageCounts);
    }

    public static void main(String[] args) {
        System.out.println("aaaa");
        report();
        System.out.println("bbbb");
        PageAnalysisService service = new PageAnalysisService();
        System.out.println("ccc");
        service.clear();
        System.out.println("ddd");
    }

}
