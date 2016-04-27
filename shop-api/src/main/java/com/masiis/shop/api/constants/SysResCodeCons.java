package com.masiis.shop.api.constants;

/**
 * @Date:2016/4/25
 * @auth:lzh
 */
public class SysResCodeCons {
    public static final String RES_CODE_NOT_KNOWN = "-1";
    public static final String RES_CODE_NOT_KNOWN_MSG = "请求繁忙,请稍候再试";

    public static final String RES_CODE_SUCCESS = "0";
    public static final String RES_CODE_SUCCESS_MSG = "请求成功";

    /**
     * 请求参数校验
     */
    public static final String RES_CODE_REQ_STRUCT_INVALID = "100001";
    public static final String RES_CODE_REQ_STRUCT_INVALID_MSG = "请求数据结构不合法";

    public static final String RES_CODE_REQ_SIGN_INVALID = "100002";
    public static final String RES_CODE_REQ_SIGN_INVALID_MSG = "请求签名错误";

    /**
     * 获取手机验证码返回码
     */
    public static final String RES_CODE_PHONENUM_BLANK = "100101";
    public static final String RES_CODE_PHONENUM_BLANK_MSG = "电话号码为空";

    public static final String RES_CODE_PHONENUM_INVALID = "100102";
    public static final String RES_CODE_PHONENUM_INVALID_MSG = "电话号码格式不正确";

    public static final String RES_CODE_VALIDCODE_SMS_FAIL = "100103";
    public static final String RES_CODE_VALIDCODE_SMS_FAIL_MSG = "验证码短信发送失败";

    public static final String RES_CODE_VALIDCODE_REQ_OFTEN = "100104";
    public static final String RES_CODE_VALIDCODE_REQ_OFTEN_MSG = "验证码请求过于频繁";

    /**
     * 手机号登录返回码
     */
    public static final String RES_CODE_VALIDCODE_IS_BLANK = "100201";
    public static final String RES_CODE_VALIDCODE_IS_BLANK_MSG = "验证码为空";

    public static final String RES_CODE_VALIDCODE_IS_INVALID = "100202";
    public static final String RES_CODE_VALIDCODE_IS_INVALID_MSG = "验证码不匹配";

    public static final String RES_CODE_VALIDCODE_IS_EXPIRED = "100203";
    public static final String RES_CODE_VALIDCODE_IS_EXPIRED_MSG = "验证码不存在或已过期";

    public static final String RES_CODE_USER_ISNOT_SIGNUP = "100204";
    public static final String RES_CODE_USER_ISNOT_SIGNUP_MSG = "该手机号未绑定用户";

    /**
     * 微信登录
     */
    public static final String RES_CODE_WXLOGIN_OPENID_NULL = "100301";
    public static final String RES_CODE_WXLOGIN_OPENID_NULL_MSG = "openid为空";

    public static final String RES_CODE_WXLOGIN_UNIONID_NULL = "100302";
    public static final String RES_CODE_WXLOGIN_UNIONID_NULL_MSG = "unionid为空";

    public static final String RES_CODE_WXLOGIN_APPID_NULL = "100303";
    public static final String RES_CODE_WXLOGIN_APPID_NULL_MSG = "appid为空";
}