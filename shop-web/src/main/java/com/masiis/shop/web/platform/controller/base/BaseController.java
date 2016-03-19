package com.masiis.shop.web.platform.controller.base;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.masiis.shop.common.util.AESUtils;
import com.masiis.shop.dao.po.ComUser;
import com.masiis.shop.web.platform.constants.SysConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 基础controller,用来编写一些基础方法
 *
 * Created by lzh on 2016/2/27.
 */
public class BaseController {
    public final static String SUCCESS ="success";

    public final static String MSG ="msg";

    public final static String DATA ="data";

    /**
     * 获取IP地址
     * @param request
     * @return
     */
    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    protected String createRedirectRes(String uri){
        return "redirect:" + uri;
    }

    protected String createForwardRes(String uri){
        return "forward:" + uri;
    }

    protected String getRequestBody(HttpServletRequest request) throws IOException {
        InputStream is = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String res = null;
        is = request.getInputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = is.read(buffer)) != -1){
            os.write(buffer, 0, len);
        }

        res = new String(os.toByteArray(), "UTF-8");

        if(is != null){
            is.close();
        }
        os.close();

        return res;
    }

    protected void sendResponseBody(HttpServletResponse response, String res) {
        try {
            OutputStream os = response.getOutputStream();
            ByteArrayInputStream is = new ByteArrayInputStream(res.getBytes("UTF-8"));
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = is.read(buffer)) != -1){
                os.write(buffer, 0, len);
            }
            if(is != null){
                is.close();
            }
            os.flush();
            os.close();
        } catch (UnsupportedEncodingException e) {

        } catch (IOException e) {

        }
    }

    /**
     * 从session中获取登录用户
     *
     * @param request
     * @return
     */
    protected ComUser getComUser(HttpServletRequest request){
        ComUser user = (ComUser) request.getSession().getAttribute(SysConstants.SESSION_LOGIN_USER_NAME);
        if(user == null){
            return null;
        }
        return user;
    }

    protected String aesEncryptBySalt(String content, String password, String salt){
        return AESUtils.encrypt(content + salt, password);
    }

    protected String aesDecryptBySalt(String content, String password, String salt){
        String res = AESUtils.decrypt(content, password);
        int end = res.lastIndexOf(salt);
        if(end > 0){
            res = res.substring(0, end);
        }
        return res;
    }

}
