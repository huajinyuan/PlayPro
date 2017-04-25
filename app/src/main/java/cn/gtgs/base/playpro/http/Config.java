package cn.gtgs.base.playpro.http;

/**
 * Created by gtgs on 2017/2/16.
 */

public interface Config {
    String BASE_URL = "http://foruo2o.com";
    String REGISTER_URL = BASE_URL + "/wp-json/login/v1/wordpress/register";
    String LOGIN_URL = BASE_URL + "/wp-json/login/v1/wordpress";
    /**
     * 产品列表
     * */
    String PRODUCTS_LIST_URL = BASE_URL + "/wp-json/wc/v1/products";
    /**
    * 单个产品
    * */
    String PRODUCTS_URL = PRODUCTS_LIST_URL + "/{id}";
    /**
     * 购物车
     * */
    String SHOPPINGCAR_URL = BASE_URL + "/wp-json/wc/v1/cart";
    /**
     * 当前用户
     * */
    String CURRENT_URL = BASE_URL + "/wp-json/wc/v1/customers/current";
    /**
     * 订单
     * */
    String ORDER_URL = BASE_URL + "/wp-json/wc/v1/orders";
    /**
     * 地址列表
     */
    String ADDRESS_URL = BASE_URL + "/wp-json/wc/v1/customers/address";
    /**
     * 单个地址
     */
    String ADDRESS_ACTION_URL = ADDRESS_URL + "/{id}";


}
