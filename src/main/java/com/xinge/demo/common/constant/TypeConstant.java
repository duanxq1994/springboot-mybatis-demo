package com.xinge.demo.common.constant;

/**
 * 类型常量
 *
 * @author duanxq
 * @date 2017/4/20
 */
public class TypeConstant {

    /**
     * 提现记录状态 1：提现中 2：打款中 3：提现成功 4：提现失败'
     */
    public static final Byte RECORD_STATUS_CASH = 1,
            RECORD_STATUS_MONEY = 2,
            RECORD_STATUS_SUCCESS = 3,
            RECORD_STATUS_FAIL = 4;

    /**
     * 是否删除  0：未删除 1：已删除
     */
    public static final Byte DELETE = 1, NOT_DELETE = 0;

    /**
     * 佣金收入是否已读 0：未读 1：已读
     */
    public static final Byte READ_STATUS_NOT = 0, READ_STATUS_YES = 1;

    /**
     * 佣金冻结状态 0：冻结 1：非冻结
     */
    public static final Byte COMMISSION_STATUS_FROZEN = 0, COMMISSION_STATUS_NOT_FROZEN = 1;

    /**
     * 存证类型 1：图片 2：失败原因
     */
    public static final Byte PROOF_TYPE_IMAGE = 1, PROOF_TYPE_TEXT = 2;
    /**
     * 是否禁用 0：启用 1：禁用
     */
    public static final Byte FORBIDDEN_NO = 0, FORBIDDEN_YES = 1;

    /**
     * 短信发送类型  1：添加银行卡 2：设置交易密码
     */
    public static final Integer SMS_ADD_BANK_CARD = 1, SMS_SET_TRADE_PWD = 2;

    /**
     * 后台角色类型 1：系统管理员 2：操作员 3：财务
     */
    public static final Integer ROLE_SYSTEM = 1, ROLE_OPERATE = 2, ROLE_ACCOUNTANT = 3;

    /**
     * 商品类型 1：商城 2：保险
     */
    public static final Byte Trade_type_mall = 1, trade_type_insurance = 2;

}
