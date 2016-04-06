package com.masiis.shop.web.platform.beans.sysbeans;

import net.sf.xsshtmlfilter.HTMLFilter;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Map;

/**
 * @Date:2016/4/6
 * @auth:lzh
 */
public class XSSRequestWrapper extends HttpServletRequestWrapper {
    private HttpServletRequest request = null;

    public XSSRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    @Override
    public String getParameter(String parameter){
        String value = new HTMLFilter().filter(super.getParameter(parameter));
        return value;
    }

    @Override
    public String getHeader(String name) {
        String value = new HTMLFilter().filter(super.getHeader(name));
        return value;
    }

    @Override
    public Map<String, String[]> getParameterMap(){
        return super.getParameterMap();
    }

    public HttpServletRequest getOriRequest(){
        return this.request;
    }

}
