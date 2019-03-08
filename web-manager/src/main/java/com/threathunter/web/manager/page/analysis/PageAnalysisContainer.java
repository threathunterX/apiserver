package com.threathunter.web.manager.page.analysis;

public class PageAnalysisContainer {

    //根结点
    private volatile static PageNode rootNode;

    //返回单例
    public static PageNode getRootNode() {
        if (null != rootNode) {
            return rootNode;
        }
        synchronized (PageAnalysisContainer.class) {
            //double check,多个线程同时block在synchronized中，避免第二个线程将每一个线程覆盖
            if (null != rootNode) {
                return rootNode;
            } else {
                rootNode = new PageNode();
            }
        }
        return rootNode;
    }

    //清空结点
    public static void clear() {
        doClear(rootNode);
    }

    private static void doClear(PageNode node) {
        for(PageNode subNode : node.getSubNodes().values()) {
            subNode.setPathVisit(0);
            subNode.setUrlVisit(0);
            doClear(subNode);
        }
    }

    public static void setRootNode(PageNode pageNode) {
        PageAnalysisContainer.rootNode = pageNode;
    }

}
