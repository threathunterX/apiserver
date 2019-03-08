package com.threathunter.web.manager.service;

import com.threathunter.web.manager.page.analysis.PageAnalysisContainer;
import com.threathunter.web.manager.page.analysis.PageNode;
import com.xiaoleilu.hutool.io.FileUtil;
import org.xson.core.XSON;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TestSerializer {
    public static void main(String[] args) {
        Map<String, Integer> pageCounts = new HashMap<>();
        pageCounts.put("threathunter.com/product/b1", 4);
        pageCounts.put("threathunter.com/product/b2", 5);
        pageCounts.put("threathunter.com/product/b1/b1_detail", 3);
        pageCounts.put("threathunter.com/product", 10);
        pageCounts.put("threathunter.com1/produ/c2", 7);

        PageAnalysisService service = new PageAnalysisService();
        service.buildUrls(pageCounts);

        byte[] data = XSON.encode(PageAnalysisContainer.getRootNode());

        File destFile = new File("/tmp/123");

        FileUtil.writeBytes(data, destFile);

        byte[] destByte = FileUtil.readBytes(destFile);

        PageNode rePageNode = XSON.decode(destByte);

        System.out.println(rePageNode.getSubNodes().size());
    }
}
