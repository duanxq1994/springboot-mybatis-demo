package com.xinge.demo.model.entity;

import java.util.HashMap;
import java.util.List;

/**
 * AJAX结果返回实体
 *
 * @author wgyi
 * @vision 2015年8月16日
 */
public class ResultEntity extends HashMap<String, Object> {
    private static final long serialVersionUID = 457278793578226564L;

    public static final int SUCCESS = 0;
    public static final int ERROR = -1;
    public static final int NOT_LOGIN = 1001;


    public ResultEntity() {
        this.setCode(ERROR);
    }

    public ResultEntity(int code) {
        this.setCode(code);
    }

    /**
     * 设置结果代码
     *
     * @param code
     */
    public void setCode(int code) {
        this.put("code", code);
    }

    public int getCode() {
        return (Integer) this.get("code");
    }

    /**
     * 设置结果集总数量
     *
     * @param count
     */
    public void setCount(Integer count) {
        this.put("count", count);
    }

    public Integer getCount() {
        return (Integer) this.get("count");
    }

    /**
     * 设置结果集
     *
     * @param list
     */
    public void setList(List<?> list) {
        this.put("list", list);
    }

    /**
     * 设置返回实体（不是list的时候）
     *
     * @param obj
     */
    public void setObject(Object obj) {
        this.put("obj", obj);
    }

    public Object getObject() {
        return (this.get("obj"));
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getList() {
        return (List<T>) this.get("list");
    }

    /**
     * 设置错误信息
     *
     * @param error
     */
    public void setError(String error) {
        this.put("error", error);
    }

    public String getError() {
        return (String) this.get("error");
    }

    /**
     * 设置需要跳转的页面地址
     *
     * @param redirect
     */
    public void setRedirect(String redirect) {
        this.put("redirect", redirect);
    }

    public String getRedirect() {
        return (String) this.get("redirect");
    }

    /**
     * 设置需要显示的消息
     *
     * @param msg
     */
    public void setMsg(String msg) {
        this.put("msg", msg);
    }

    public String getMsg() {
        return (String) this.get("msg");
    }
}
