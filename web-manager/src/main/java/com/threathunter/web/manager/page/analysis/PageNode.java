package com.threathunter.web.manager.page.analysis;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@Slf4j
@EqualsAndHashCode(of = {"fullPath"})
@ToString(of = {"fullPath", "urlVisit", "pathVisit"})
public class PageNode {

    //当前结点的内容
    private String text;

    //从父结点到当前结点的内容
    private String fullPath;

    private boolean isUrl;

    //作为url的访问次数
    private int urlVisit = 0;

    //作为路径的访问次数
    private int pathVisit = 0;

    //人工标签
    private String manTag;

    public PageNode() {
        this.text = "";
        this.fullPath = "";
    }

    //当前结点的子结点
    private ConcurrentHashMap<String, PageNode> subNodes = new ConcurrentHashMap<>();

    public void build(PageNode parentNode, String parentText,  List<String> parts, int cnt) {
        if(parts.size() == 0) {
            return;
        }
        String text = parts.remove(0);
        PageNode subNode = parentNode.getSubNodes().get(text);
        if(null == subNode) {
            subNode = new PageNode();
            subNode.fullPath = StringUtils.isBlank(parentText) ? text :  parentText + "/" + text;
            subNode.text = text;
            parentNode.getSubNodes().put(subNode.getText(), subNode);
        }
        if(parts.size() == 0) {
            subNode.urlVisit = subNode.urlVisit + cnt;
            subNode.isUrl = true;
        } else {
            subNode.pathVisit = subNode.pathVisit + cnt;
        }
        subNode.build(subNode, subNode.getFullPath(), parts, cnt);
    }

    public PageNode locate(PageNode parentNode, List<String> parts) {
        if(parts.size() == 0) {
            return null;
        }
        String part = parts.remove(0);
        //遍历当前parent下的part部分，如果包含
        if(parentNode.getSubNodes().containsKey(part)) {
            //如果当前为最后一个part
            PageNode current = parentNode.getSubNodes().get(part);
            if(parts.size() == 0) {
                return current;
            } else {
                return locate(current, parts);
            }
        } else {
            return null;
        }
    }


    //搜索关键字
    public void search(String keyword, Set<PageNode> results) {
        if(this.fullPath.contains(keyword)) {
            results.add(this);
        }

        if(this.getSubNodes().size() == 0) {
            return;
        }

        for(PageNode node : getSubNodes().values()) {
            node.search(keyword, results);
        }

    }


    //查找当前结点下的url结点
    public void foundChildrenUrl(Set<PageNode> results) {
        if(this.isUrl() == true) {
            results.add(this);
        }
        if(this.getSubNodes().size() == 0) {
            return;
        }
        for(PageNode node : getSubNodes().values()) {
            node.foundChildrenUrl(results);
        }
    }


}
