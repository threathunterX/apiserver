package com.threathunter.web.http.server;

import com.threathunter.web.common.utils.ObjUtils;
import com.threathunter.web.http.constant.HttpConstant;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.DataBinder;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);

    static {
        DiskFileUpload.deleteOnExitTemporaryFile = true;
        DiskFileUpload.baseDirectory = null;
        DiskAttribute.deleteOnExitTemporaryFile = true;
        DiskAttribute.baseDirectory = null;
    }

    private String requestId;
    private String ip;

    private FullHttpRequest request;
    private QueryStringDecoder queryStringDecoder;
    private HttpPostRequestDecoder postRequestDecoder;

    private Map<String, String> headerMap = new CaseInsensitiveMap<String, String>();
    private Map<String, Cookie> cookieMap = Maps.newLinkedHashMap();
    private Map<String, String> parameterMap = Maps.newLinkedHashMap();
    private Map<String, FileUpload> fileMap = Maps.newLinkedHashMap();
    private Map<String, Object> attributeMap = Maps.newLinkedHashMap();
    private String content;

    public HttpRequest(FullHttpRequest request) {
        this.requestId = UUID.randomUUID().toString();
        this.request = request;
        logger.info("request uri : {}", getUri());
        initRequestHeader();
   /*     if(HttpMethod.POST.equals(request.method())) {
            decodeByteBuff();
        }*/
        decodeQueryString();
        decodePost();
    }

    public Map<String,Object> getAttributeMap() {
        return attributeMap;
    }


    private void decodeByteBuff() {
        ByteBuf byteBuf = this.request.content();
        byte[] byteArray = new byte[byteBuf.capacity()];
        byteBuf.readBytes(byteArray);
        this.content = new String(byteArray);
    }

    protected void initRequestHeader() {
        for (Map.Entry<String, String> entry : request.headers()) {
            headerMap.put(entry.getKey(), entry.getValue());
        }
        String cookieString = request.headers().get(HttpHeaderNames.COOKIE);
        if (StringUtils.isNotEmpty(cookieString)) {
            Set<Cookie> cookieSet = ServerCookieDecoder.STRICT.decode(cookieString);
            for (Cookie cookie : cookieSet) {
                cookieMap.put(cookie.name(), cookie);
            }
        }
    }

    protected void decodeQueryString() {
        queryStringDecoder = new QueryStringDecoder(getUri());
        Map<String, List<String>> uriAttributes = queryStringDecoder.parameters();
        for (Map.Entry<String, List<String>> attr : uriAttributes.entrySet()) {
            for (String attrVal : attr.getValue()) {
                parameterMap.put(attr.getKey(), attrVal);
            }
        }
    }

    public Map<String, List<String>> getRawQueryString() {
        this.queryStringDecoder = new QueryStringDecoder(this.getUri());
        final Map<String, List<String>> uriAttributes = (Map<String, List<String>>)this.queryStringDecoder.parameters();
        return uriAttributes;
    }

    protected void decodePost() {
        if (!HttpMethod.POST.equals(request.method())) {
            return;
        }
        if(! HttpPostRequestDecoder.isMultipart(request)) {
            decodeByteBuff();
        } else {
            try {
                postRequestDecoder = new HttpPostRequestDecoder(factory, request);
                List<InterfaceHttpData> dataList = postRequestDecoder.getBodyHttpDatas();
                for (InterfaceHttpData data : dataList) {
                    if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                        Attribute attribute = (Attribute) data;
                        parameterMap.put(attribute.getName(), attribute.getValue());
                    } else if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
                        FileUpload fileUpload = (FileUpload) data;
                        fileMap.put(fileUpload.getName(), fileUpload);
                    }
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public String getUri() {
        return request.uri();
    }

    public String getMethod() {
        return request.method().toString();
    }

    public String getLookupPath() {
        return queryStringDecoder.path();
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public FullHttpRequest getFullHttpRequest() {
        return request;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public String getHeader(String key) {
        return headerMap.get(key);
    }

    public void setHeader(String key, String value) {
        headerMap.put(key, value);
    }

    public Map<String, Cookie> getCookieMap() {
        return cookieMap;
    }

    public String getCookie(String key) {
        Cookie cookie = cookieMap.get(key);
        if (cookie == null) {
            return null;
        }
        return cookie.value();
    }

    public Map<String, String> getParameterMap() {
        return parameterMap;
    }

    public String getParameter(String key) {
        return parameterMap.get(key);
    }

    public String getParameter(String key, String defaultValue) {
        String value = getParameter(key);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        return value;
    }

    public Integer getIntParameter(String key) {
        return ObjUtils.toInteger(getParameter(key));
    }

    public Integer getIntParameter(String key, Integer defaultValue) {
        return ObjUtils.toInteger(getParameter(key), defaultValue);
    }

    public Long getLongParameter(String key) {
        return ObjUtils.toLong(getParameter(key));
    }

    public Long getLongParameter(String key, Long defaultValue) {
        return ObjUtils.toLong(getParameter(key), defaultValue);
    }

    public BigDecimal getBigDecimalParameter(String key) {
        return ObjUtils.toBigDecimal(getParameter(key));
    }

    public BigDecimal getBigDecimalParameter(String key, BigDecimal defaultValue) {
        return ObjUtils.toBigDecimal(getParameter(key), defaultValue);
    }

    public Boolean getBooleanParameter(String key) {
        return ObjUtils.toBoolean(getParameter(key));
    }

    public Boolean getBooleanParameter(String key, Boolean defaultValue) {
        return ObjUtils.toBoolean(getParameter(key), defaultValue);
    }

    public Map<String, FileUpload> getFileMap() {
        return fileMap;
    }

    public FileUpload getFile(String key) {
        return fileMap.get(key);
    }

    public Object getAttribute(String key) {
        return attributeMap.get(key);
    }

    public void setAttribute(String key, Object value) {
        attributeMap.put(key, value);
    }

    public String getPathValue(String key) {
        Map<String, String> uriVariables = (Map<String, String>) attributeMap.get(HttpConstant.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (uriVariables == null) {
            return null;
        }
        return uriVariables.get(key);
    }

    public Integer getIntPathValue(String key) {
        return ObjUtils.toInteger(getPathValue(key));
    }

    public Long getLongPathValue(String key) {
        return ObjUtils.toLong(getPathValue(key));
    }

    public String getBody() {
        ByteBuf content = request.content();
        if (content != null) {
            return content.toString(CharsetUtil.UTF_8);
        }
        return "";
    }

    public <T> T bindObj(T obj) {
        DataBinder dataBinder = new DataBinder(obj);
        dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(DATE_FORMAT), true));
        dataBinder.bind(new MutablePropertyValues(parameterMap));
        return obj;
    }

    public void cleanFiles() {
        if (postRequestDecoder != null) {
            postRequestDecoder.cleanFiles();
        }
    }

    public void destroy() {
        if (postRequestDecoder != null) {
            postRequestDecoder.destroy();
            postRequestDecoder = null;
        }
    }

    public String getContent() {
        return content;
    }
}
