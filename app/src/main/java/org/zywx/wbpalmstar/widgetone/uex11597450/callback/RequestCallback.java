package org.zywx.wbpalmstar.widgetone.uex11597450.callback;


public interface RequestCallback<T> {

    /**
     * 请求之前调用
     */
    void beforeRequest();

    /**
     * 请求错误调用
     *
     * @param msg 错误信息
     */
    void requestFail(String msg);

//    /**
//     * 请求完成调用
//     */
//    void requestComplete();

    /**
     * 请求成功调用
     *
     * @param t 数据
     */
    void requestSuccess(T t);

    /**
     * 登录成功,其他处理
     */
    void otherDeal(T t);
}
