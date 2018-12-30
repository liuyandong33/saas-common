package build.dream.common.utils;

import build.dream.common.api.ApiRest;
import build.dream.common.beans.WebResponse;
import build.dream.common.constants.Constants;
import build.dream.common.models.notify.SaveNotifyRecordModel;
import build.dream.common.models.weixinpay.*;
import build.dream.common.saas.domains.WeiXinPayAccount;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class WeiXinPayUtils {
    private static final String WEI_XIN_PAY_API_URL = "https://api.mch.weixin.qq.com";

    /**
     * 获取微信支付账号
     *
     * @param tenantId
     * @param branchId
     * @return
     */
    private static WeiXinPayAccount obtainWeiXinPayAccount(String tenantId, String branchId) {
        String weiXinPayAccountJson = CacheUtils.hget(Constants.KEY_WEI_XIN_PAY_ACCOUNTS, tenantId + "_" + branchId);
        WeiXinPayAccount weiXinPayAccount = null;
        if (StringUtils.isNotBlank(weiXinPayAccountJson)) {
            weiXinPayAccount = GsonUtils.fromJson(weiXinPayAccountJson, WeiXinPayAccount.class);
        }
        return weiXinPayAccount;
    }

    /**
     * 生成签名
     *
     * @param callWeiXinSystemRequestParameters
     * @param weiXinPayKey
     * @param signType
     * @return
     */
    public static String generateSign(Map<String, String> callWeiXinSystemRequestParameters, String weiXinPayKey, String signType) {
        Map<String, String> sortedCallWeiXinSystemRequestParameters = null;
        if (callWeiXinSystemRequestParameters instanceof TreeMap) {
            sortedCallWeiXinSystemRequestParameters = callWeiXinSystemRequestParameters;
        } else {
            sortedCallWeiXinSystemRequestParameters = new TreeMap<String, String>(callWeiXinSystemRequestParameters);
        }
        Set<Map.Entry<String, String>> entries = sortedCallWeiXinSystemRequestParameters.entrySet();
        StringBuilder stringSignTemp = new StringBuilder();
        for (Map.Entry<String, String> entry : entries) {
            if (StringUtils.isNotBlank(entry.getValue())) {
                stringSignTemp.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        stringSignTemp.append("key=");
        stringSignTemp.append(weiXinPayKey);
        String sign = null;
        if (Constants.MD5.equals(signType)) {
            sign = DigestUtils.md5Hex(stringSignTemp.toString()).toUpperCase();
        } else if (Constants.HMAC_SHA256.equals(signType)) {
            sign = HmacUtils.hmacSha256Hex(weiXinPayKey, stringSignTemp.toString()).toUpperCase();
        }
        return sign;
    }

    /**
     * 验证签名
     *
     * @param weiXinSystemResult
     * @param weiXinPayKey
     * @param signType
     * @return
     */
    public static boolean checkSign(Map<String, String> weiXinSystemResult, String weiXinPayKey, String signType) {
        Map<String, String> sortedWeiXinSystemResult = new TreeMap<String, String>();
        sortedWeiXinSystemResult.putAll(weiXinSystemResult);
        String sign = sortedWeiXinSystemResult.remove("sign");
        return sign.equals(generateSign(sortedWeiXinSystemResult, weiXinPayKey, signType));
    }

    /**
     * 生成代签名字符串
     *
     * @param callWeiXinSystemRequestParameters
     * @return
     */
    public static String generateFinalData(Map<String, String> callWeiXinSystemRequestParameters) {
        StringBuffer weiXinPayFinalData = new StringBuffer();
        weiXinPayFinalData.append("<xml>");
        Set<Map.Entry<String, String>> entries = callWeiXinSystemRequestParameters.entrySet();

        for (Map.Entry<String, String> entry : entries) {
            String key = entry.getKey();
            weiXinPayFinalData.append("<").append(key).append(">").append(String.format(Constants.CDATA_FORMAT, entry.getValue())).append("</").append(key).append(">");
        }

        weiXinPayFinalData.append("</xml>");
        return weiXinPayFinalData.toString();
    }

    /**
     * 调用维系系统
     *
     * @param url
     * @param finalData
     * @param certificate
     * @param password
     * @return
     * @throws DocumentException
     */
    public static Map<String, String> callWeiXinPaySystem(String url, String finalData, String certificate, String password) throws DocumentException {
        WebResponse webResponse = OutUtils.doPostWithRequestBody(url, null, finalData, certificate, password, Constants.PKCS12, Constants.TRUST_MANAGERS);
        return XmlUtils.xmlStringToMap(webResponse.getResult());
    }

    /**
     * 调用微信系统
     *
     * @param url
     * @param finalData
     * @return
     * @throws DocumentException
     */
    public static Map<String, String> callWeiXinPaySystem(String url, String finalData) throws DocumentException {
        return callWeiXinPaySystem(url, finalData, null, null);
    }

    /**
     * 保存异步通知记录
     *
     * @param uuid
     * @param notifyUrl
     * @param weiXinPayApiSecretKey
     * @param weiXinPaySignType
     * @throws IOException
     */
    private static void saveNotifyRecord(String uuid, String notifyUrl, String weiXinPayApiSecretKey, String weiXinPaySignType) {
        String serviceName = ConfigurationUtils.getConfiguration(Constants.SERVICE_NAME);
        if (Constants.SERVICE_NAME_PLATFORM.equals(serviceName)) {
            SaveNotifyRecordModel saveNotifyRecordModel = SaveNotifyRecordModel.builder()
                    .uuid(uuid)
                    .notifyUrl(notifyUrl)
                    .weiXinPayApiSecretKey(weiXinPayApiSecretKey)
                    .weiXinPaySignType(weiXinPaySignType)
                    .build();

            DataSourceTransactionManager dataSourceTransactionManager = ApplicationHandler.getBean(DataSourceTransactionManager.class);
            DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
            defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(defaultTransactionDefinition);
            try {
                NotifyUtils.saveNotifyRecord(saveNotifyRecordModel);
                dataSourceTransactionManager.commit(transactionStatus);
            } catch (Exception e) {
                dataSourceTransactionManager.rollback(transactionStatus);
                throw e;
            }

        } else {
            Map<String, String> saveNotifyRecordRequestParameters = new HashMap<String, String>();
            saveNotifyRecordRequestParameters.put("uuid", uuid);
            saveNotifyRecordRequestParameters.put("notifyUrl", notifyUrl);
            saveNotifyRecordRequestParameters.put("weiXinPayApiSecretKey", weiXinPayApiSecretKey);
            saveNotifyRecordRequestParameters.put("weiXinPaySignType", weiXinPaySignType);
            ApiRest saveNotifyRecordResult = ProxyUtils.doPostWithRequestParameters(Constants.SERVICE_NAME_PLATFORM, "notify", "saveNotifyRecord", saveNotifyRecordRequestParameters);
            ValidateUtils.isTrue(saveNotifyRecordResult.isSuccessful(), saveNotifyRecordResult.getError());
        }
    }

    /**
     * 刷卡支付
     *
     * @param microPayModel
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    public static Map<String, String> microPay(MicroPayModel microPayModel) throws DocumentException {
        microPayModel.validateAndThrow();
        String tenantId = microPayModel.getTenantId();
        String branchId = microPayModel.getBranchId();
        String deviceInfo = microPayModel.getDeviceInfo();
        String signType = microPayModel.getSignType();
        String body = microPayModel.getBody();
        String detail = microPayModel.getDetail();
        String attach = microPayModel.getAttach();
        String outTradeNo = microPayModel.getOutTradeNo();
        Integer totalFee = microPayModel.getTotalFee();
        String feeType = microPayModel.getFeeType();
        String spbillCreateIp = microPayModel.getSpbillCreateIp();
        String goodsTag = microPayModel.getGoodsTag();
        String limitPay = microPayModel.getLimitPay();
        String timeStart = microPayModel.getTimeStart();
        String timeExpire = microPayModel.getTimeExpire();
        String authCode = microPayModel.getAuthCode();
        MicroPayModel.SceneInfoModel sceneInfoModel = microPayModel.getSceneInfoModel();

        WeiXinPayAccount weiXinPayAccount = obtainWeiXinPayAccount(tenantId, branchId);
        ValidateUtils.notNull(weiXinPayAccount, "未配置微信支付账号！");

        String appId = weiXinPayAccount.getAppId();
        String mchId = weiXinPayAccount.getMchId();
        boolean acceptanceModel = weiXinPayAccount.isAcceptanceModel();
        String subPublicAccountAppId = weiXinPayAccount.getSubPublicAccountAppId();
        String subMchId = weiXinPayAccount.getSubMchId();
        String apiSecretKey = weiXinPayAccount.getApiSecretKey();

        Map<String, String> microPayRequestParameters = new HashMap<String, String>();
        microPayRequestParameters.put("appid", appId);
        microPayRequestParameters.put("mch_id", mchId);

        if (acceptanceModel) {
            ApplicationHandler.ifNotBlankPut(microPayRequestParameters, "sub_appid", subPublicAccountAppId);
            microPayRequestParameters.put("sub_mch_id", subMchId);
        }

        ApplicationHandler.ifNotBlankPut(microPayRequestParameters, "device_info", deviceInfo);
        microPayRequestParameters.put("nonce_str", RandomStringUtils.randomAlphanumeric(32));

        microPayRequestParameters.put("sign_type", signType);

        microPayRequestParameters.put("body", body);
        ApplicationHandler.ifNotBlankPut(microPayRequestParameters, "detail", detail);
        ApplicationHandler.ifNotBlankPut(microPayRequestParameters, "attach", attach);
        microPayRequestParameters.put("out_trade_no", outTradeNo);
        microPayRequestParameters.put("total_fee", totalFee.toString());
        ApplicationHandler.ifNotBlankPut(microPayRequestParameters, "fee_type", feeType);
        microPayRequestParameters.put("spbill_create_ip", spbillCreateIp);
        ApplicationHandler.ifNotBlankPut(microPayRequestParameters, "goods_tag", goodsTag);
        ApplicationHandler.ifNotBlankPut(microPayRequestParameters, "limit_pay", limitPay);
        ApplicationHandler.ifNotBlankPut(microPayRequestParameters, "time_start", timeStart);
        ApplicationHandler.ifNotBlankPut(microPayRequestParameters, "time_expire", timeExpire);
        microPayRequestParameters.put("auth_code", authCode);
        if (sceneInfoModel != null) {
            microPayRequestParameters.put("scene_info", GsonUtils.toJson(sceneInfoModel, false));
        }

        String sign = generateSign(microPayRequestParameters, apiSecretKey, Constants.MD5);
        microPayRequestParameters.put("sign", sign);

        String microPayFinalData = generateFinalData(microPayRequestParameters);
        Map<String, String> microPayResult = callWeiXinPaySystem(WEI_XIN_PAY_API_URL + "/pay/micropay", microPayFinalData);

        String returnCode = microPayResult.get("return_code");
        ValidateUtils.isTrue(Constants.SUCCESS.equals(returnCode), microPayResult.get("return_msg"));

        ValidateUtils.isTrue(checkSign(microPayResult, apiSecretKey, Constants.MD5), "微信系统返回结果签名校验未通过！");

        String resultCode = microPayResult.get("result_code");
        String errCode = microPayResult.get("err_code");
        ValidateUtils.isTrue((Constants.SUCCESS.equals(resultCode) || (Constants.FAIL.equals(resultCode) && "USERPAYING".equals(errCode))), microPayResult.get("err_code_des"));

        return microPayResult;
    }

    /**
     * 统一下单
     *
     * @param unifiedOrderModel
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    public static Map<String, String> unifiedOrder(UnifiedOrderModel unifiedOrderModel) throws DocumentException {
        unifiedOrderModel.validateAndThrow();

        String tenantId = unifiedOrderModel.getTenantId();
        String branchId = unifiedOrderModel.getBranchId();
        String deviceInfo = unifiedOrderModel.getDeviceInfo();
        String signType = unifiedOrderModel.getSignType();
        String body = unifiedOrderModel.getBody();
        String detail = unifiedOrderModel.getDetail();
        String attach = unifiedOrderModel.getAttach();
        String outTradeNo = unifiedOrderModel.getOutTradeNo();
        String feeType = unifiedOrderModel.getFeeType();
        Integer totalFee = unifiedOrderModel.getTotalFee();
        String spbillCreateIp = unifiedOrderModel.getSpbillCreateIp();
        String timeStart = unifiedOrderModel.getTimeStart();
        String timeExpire = unifiedOrderModel.getTimeExpire();
        String goodsTag = unifiedOrderModel.getGoodsTag();
        String notifyUrl = unifiedOrderModel.getNotifyUrl();
        String tradeType = unifiedOrderModel.getTradeType();
        String productId = unifiedOrderModel.getProductId();
        String limitPay = unifiedOrderModel.getLimitPay();
        String openId = unifiedOrderModel.getOpenId();
        String subOpenId = unifiedOrderModel.getSubOpenId();
        UnifiedOrderModel.SceneInfoModel sceneInfoModel = unifiedOrderModel.getSceneInfoModel();

        WeiXinPayAccount weiXinPayAccount = obtainWeiXinPayAccount(tenantId, branchId);
        ValidateUtils.notNull(weiXinPayAccount, "未配置微信支付账号！");
        String appId = weiXinPayAccount.getAppId();
        String mchId = weiXinPayAccount.getMchId();
        boolean acceptanceModel = weiXinPayAccount.isAcceptanceModel();
        String subPublicAccountAppId = weiXinPayAccount.getSubPublicAccountAppId();
        String subMchId = weiXinPayAccount.getSubMchId();
        String subMiniProgramAppId = weiXinPayAccount.getSubMiniProgramAppId();
        String apiSecretKey = weiXinPayAccount.getApiSecretKey();

        Map<String, String> unifiedOrderRequestParameters = new HashMap<String, String>();
        unifiedOrderRequestParameters.put("appid", appId);
        unifiedOrderRequestParameters.put("mch_id", mchId);

        ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "device_info", deviceInfo);
        unifiedOrderRequestParameters.put("nonce_str", RandomStringUtils.randomAlphanumeric(32));

        unifiedOrderRequestParameters.put("sign_type", signType);

        unifiedOrderRequestParameters.put("body", body);
        ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "detail", detail);
        ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "attach", attach);

        unifiedOrderRequestParameters.put("out_trade_no", outTradeNo);

        ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "fee_type", feeType);
        unifiedOrderRequestParameters.put("total_fee", totalFee.toString());
        unifiedOrderRequestParameters.put("spbill_create_ip", spbillCreateIp);
        ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "time_start", timeStart);
        ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "time_expire", timeExpire);
        ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "goods_tag", goodsTag);

        unifiedOrderRequestParameters.put("notify_url", notifyUrl);

        if (Constants.WEI_XIN_PAY_TRADE_TYPE_MINI_PROGRAM.equals(tradeType)) {
            unifiedOrderRequestParameters.put("trade_type", Constants.WEI_XIN_PAY_TRADE_TYPE_JSAPI);
        } else {
            unifiedOrderRequestParameters.put("trade_type", tradeType);
        }

        ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "product_id", productId);
        ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "limit_pay", limitPay);

        if (Constants.WEI_XIN_PAY_TRADE_TYPE_JSAPI.equals(tradeType)) {
            if (acceptanceModel) {
                ApplicationHandler.isTrue(StringUtils.isNotBlank(openId) || StringUtils.isNotBlank(subOpenId), "参数openId和subOpenId不能同时为空！");
                if (StringUtils.isNotBlank(subOpenId)) {
                    ValidateUtils.notBlank(subPublicAccountAppId, "支付账号未配置子商户公众账号APPID！");
                }

                ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "sub_appid", subPublicAccountAppId);
                unifiedOrderRequestParameters.put("sub_mch_id", subMchId);
                ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "openid", openId);
                ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "sub_openid", subOpenId);
            } else {
                ApplicationHandler.notBlank(openId, "openId");
                unifiedOrderRequestParameters.put("openid", openId);
            }
        } else if (Constants.WEI_XIN_PAY_TRADE_TYPE_NATIVE.equals(tradeType)) {
            ApplicationHandler.notBlank(productId, "productId");
            if (acceptanceModel) {
                if (StringUtils.isNotBlank(subOpenId)) {
                    ValidateUtils.notBlank(subPublicAccountAppId, "支付账号未配置子商户公众账号APPID！");
                }
                ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "sub_appid", subPublicAccountAppId);
                unifiedOrderRequestParameters.put("sub_mch_id", subMchId);
                ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "openid", openId);
                ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "sub_openid", subOpenId);
            } else {
                ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "openid", openId);
            }
        } else if (Constants.WEI_XIN_PAY_TRADE_TYPE_APP.equals(tradeType)) {
            if (acceptanceModel) {
                ValidateUtils.notBlank(subPublicAccountAppId, "支付账号未配置微信开放平台APPID！");
                unifiedOrderRequestParameters.put("sub_appid", subPublicAccountAppId);
                unifiedOrderRequestParameters.put("sub_mch_id", subMchId);
            }
        } else if (Constants.WEI_XIN_PAY_TRADE_TYPE_MWEB.equals(tradeType)) {
            if (acceptanceModel) {
                if (StringUtils.isNotBlank(subOpenId)) {
                    ValidateUtils.notBlank(subPublicAccountAppId, "支付账号未配置子商户公众账号APPID！");
                }
                ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "sub_appid", subPublicAccountAppId);
                unifiedOrderRequestParameters.put("sub_mch_id", subMchId);
                ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "openid", openId);
                ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "sub_openid", subOpenId);
            } else {
                ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "openid", openId);
            }
        } else if (Constants.WEI_XIN_PAY_TRADE_TYPE_MINI_PROGRAM.equals(tradeType)) {
            if (acceptanceModel) {
                ValidateUtils.notBlank(subMiniProgramAppId, "支付账号未配置小程序APPID！");
                unifiedOrderRequestParameters.put("sub_appid", subMiniProgramAppId);
                unifiedOrderRequestParameters.put("sub_mch_id", subMchId);

                ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "openid", openId);
                ApplicationHandler.ifNotBlankPut(unifiedOrderRequestParameters, "sub_openid", subOpenId);
            } else {
                ApplicationHandler.notBlank(openId, "openId");
                unifiedOrderRequestParameters.put("openid", openId);
            }
        }

        if (sceneInfoModel != null) {
            unifiedOrderRequestParameters.put("scene_info", GsonUtils.toJson(sceneInfoModel, false));
        }

        String sign = generateSign(unifiedOrderRequestParameters, apiSecretKey, signType);
        unifiedOrderRequestParameters.put("sign", sign);

        String unifiedOrderFinalData = generateFinalData(unifiedOrderRequestParameters);
        Map<String, String> unifiedOrderResult = callWeiXinPaySystem(WEI_XIN_PAY_API_URL + "/pay/unifiedorder", unifiedOrderFinalData);

        String returnCode = unifiedOrderResult.get("return_code");
        ValidateUtils.isTrue(Constants.SUCCESS.equals(returnCode), unifiedOrderResult.get("return_msg"));

        ValidateUtils.isTrue(checkSign(unifiedOrderResult, apiSecretKey, Constants.MD5), "微信系统返回结果签名校验未通过！");
        String resultCode = unifiedOrderResult.get("result_code");

        ValidateUtils.isTrue(Constants.SUCCESS.equals(resultCode), unifiedOrderResult.get("err_code_des"));

        // 保存异步通知记录
        saveNotifyRecord(outTradeNo, notifyUrl, apiSecretKey, signType);

        Map<String, String> data = new HashMap<String, String>();
        if (Constants.WEI_XIN_PAY_TRADE_TYPE_APP.equals(tradeType)) {
            if (acceptanceModel) {
                data.put("appid", unifiedOrderResult.get("sub_appid"));
                data.put("partnerid", unifiedOrderResult.get("sub_mch_id"));
            } else {
                data.put("appid", unifiedOrderResult.get("appid"));
                data.put("partnerid", unifiedOrderResult.get("mch_id"));
            }
            data.put("prepayid", unifiedOrderResult.get("prepay_id"));
            data.put("package", "Sign=WXPay");
            data.put("noncestr", RandomStringUtils.randomAlphanumeric(32));
            data.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
            data.put("sign", generateSign(data, apiSecretKey, Constants.MD5));
        } else if (Constants.WEI_XIN_PAY_TRADE_TYPE_MWEB.equals(tradeType)) {
            data.put("mwebUrl", unifiedOrderResult.get("mweb_url"));
        } else if (Constants.WEI_XIN_PAY_TRADE_TYPE_NATIVE.equals(tradeType)) {
            data.put("codeUrl", unifiedOrderResult.get("code_url"));
        } else if (Constants.WEI_XIN_PAY_TRADE_TYPE_JSAPI.equals(tradeType)) {
            data.put("appId", unifiedOrderResult.get("appid"));
            data.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
            data.put("nonceStr", RandomStringUtils.randomAlphanumeric(32));
            data.put("package", "prepay_id=" + unifiedOrderResult.get("prepay_id"));
            data.put("signType", signType);
            data.put("paySign", generateSign(data, apiSecretKey, signType));
        } else if (Constants.WEI_XIN_PAY_TRADE_TYPE_MINI_PROGRAM.equals(tradeType)) {
            if (acceptanceModel) {
                data.put("appId", unifiedOrderResult.get("sub_appid"));
            } else {
                data.put("appId", unifiedOrderResult.get("appid"));
            }
            data.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
            data.put("nonceStr", RandomStringUtils.randomAlphanumeric(32));
            data.put("package", "prepay_id=" + unifiedOrderResult.get("prepay_id"));
            data.put("signType", signType);
            data.put("paySign", generateSign(data, apiSecretKey, signType));
        }

        return data;
    }

    /**
     * 退款
     *
     * @param refundModel
     * @return
     * @throws DocumentException
     */
    public static Map<String, String> refund(RefundModel refundModel) throws DocumentException {
        refundModel.validateAndThrow();

        String tenantId = refundModel.getTenantId();
        String branchId = refundModel.getBranchId();
        String transactionId = refundModel.getTransactionId();
        String outTradeNo = refundModel.getOutTradeNo();
        String outRefundNo = refundModel.getOutRefundNo();
        Integer totalFee = refundModel.getTotalFee();
        Integer refundFee = refundModel.getRefundFee();
        String refundFeeType = refundModel.getRefundFeeType();
        String refundDesc = refundModel.getRefundDesc();
        String refundAccount = refundModel.getRefundAccount();
        String tradeType = refundModel.getTradeType();

        WeiXinPayAccount weiXinPayAccount = obtainWeiXinPayAccount(tenantId, branchId);
        ValidateUtils.notNull(weiXinPayAccount, "未配置微信支付账号！");

        Map<String, String> refundRequestParameters = new HashMap<String, String>();
        refundRequestParameters.put("appid", weiXinPayAccount.getAppId());
        refundRequestParameters.put("mch_id", weiXinPayAccount.getMchId());

        if (weiXinPayAccount.isAcceptanceModel()) {
            if (Constants.WEI_XIN_PAY_TRADE_TYPE_JSAPI.equals(tradeType) || Constants.WEI_XIN_PAY_TRADE_TYPE_NATIVE.equals(tradeType) || Constants.WEI_XIN_PAY_TRADE_TYPE_MWEB.equals(tradeType) || Constants.WEI_XIN_PAY_TRADE_TYPE_MICROPAY.equals(tradeType)) {
                ApplicationHandler.ifNotBlankPut(refundRequestParameters, "sub_appid", weiXinPayAccount.getSubPublicAccountAppId());
            } else if (Constants.WEI_XIN_PAY_TRADE_TYPE_APP.equals(tradeType)) {
                refundRequestParameters.put("sub_appid", weiXinPayAccount.getSubOpenPlatformAppId());
            }
            refundRequestParameters.put("sub_mch_id", weiXinPayAccount.getSubMchId());
        }

        refundRequestParameters.put("nonce_str", RandomStringUtils.randomAlphanumeric(32));
        ApplicationHandler.ifNotBlankPut(refundRequestParameters, "transaction_id", transactionId);
        ApplicationHandler.ifNotBlankPut(refundRequestParameters, "out_trade_no", outTradeNo);
        refundRequestParameters.put("out_refund_no", outRefundNo);
        refundRequestParameters.put("total_fee", totalFee.toString());
        refundRequestParameters.put("refund_fee", refundFee.toString());
        ApplicationHandler.ifNotBlankPut(refundRequestParameters, "refund_fee_type", refundFeeType);
        ApplicationHandler.ifNotBlankPut(refundRequestParameters, "refund_desc", refundDesc);
        ApplicationHandler.ifNotBlankPut(refundRequestParameters, "refund_account", refundAccount);

        String sign = generateSign(refundRequestParameters, weiXinPayAccount.getApiSecretKey(), Constants.MD5);
        refundRequestParameters.put("sign", sign);

        String refundFinalData = generateFinalData(refundRequestParameters);
        Map<String, String> refundResult = callWeiXinPaySystem(WEI_XIN_PAY_API_URL + "/secapi/pay/refund", refundFinalData, weiXinPayAccount.getOperationCertificate(), weiXinPayAccount.getOperationCertificatePassword());

        String returnCode = refundResult.get("return_code");
        ValidateUtils.isTrue(Constants.SUCCESS.equals(returnCode), refundResult.get("return_msg"));

        ValidateUtils.isTrue(checkSign(refundResult, weiXinPayAccount.getApiSecretKey(), Constants.MD5), "微信系统返回结果签名校验未通过！");

        String resultCode = refundResult.get("result_code");
        ValidateUtils.isTrue(Constants.SUCCESS.equals(resultCode), refundResult.get("err_code_des"));

        return refundResult;
    }

    /**
     * 根据支付场景获取交易类型
     *
     * @param paidScene
     * @return
     */
    public static String obtainTradeType(Integer paidScene) {
        String tradeType = null;
        if (paidScene == Constants.PAID_SCENE_WEI_XIN_MICROPAY) {
            tradeType = Constants.WEI_XIN_PAY_TRADE_TYPE_MICROPAY;
        } else if (paidScene == Constants.PAID_SCENE_WEI_XIN_JSAPI_PUBLIC_ACCOUNT) {
            tradeType = Constants.WEI_XIN_PAY_TRADE_TYPE_JSAPI;
        } else if (paidScene == Constants.PAID_SCENE_WEI_XIN_NATIVE) {
            tradeType = Constants.WEI_XIN_PAY_TRADE_TYPE_NATIVE;
        } else if (paidScene == Constants.PAID_SCENE_WEI_XIN_APP) {
            tradeType = Constants.WEI_XIN_PAY_TRADE_TYPE_APP;
        } else if (paidScene == Constants.PAID_SCENE_WEI_XIN_MWEB) {
            tradeType = Constants.WEI_XIN_PAY_TRADE_TYPE_MWEB;
        } else if (paidScene == Constants.PAID_SCENE_WEI_XIN_JSAPI_MINI_PROGRAM) {
            tradeType = Constants.WEI_XIN_PAY_TRADE_TYPE_JSAPI;
        }
        return tradeType;
    }

    /**
     * 服务商特约商户关注功能配置
     *
     * @param addRecommendConfModel
     * @return
     * @throws DocumentException
     */
    public static Map<String, String> addRecommendConf(AddRecommendConfModel addRecommendConfModel) throws DocumentException {
        addRecommendConfModel.validateAndThrow();
        String tenantId = addRecommendConfModel.getTenantId();
        String branchId = addRecommendConfModel.getBranchId();
        String mchId = addRecommendConfModel.getMchId();
        String subMchId = addRecommendConfModel.getSubMchId();
        String subAppId = addRecommendConfModel.getSubAppId();
        String subscribeAppId = addRecommendConfModel.getSubscribeAppId();
        String receiptAppId = addRecommendConfModel.getReceiptAppId();
        String signType = addRecommendConfModel.getSignType();

        WeiXinPayAccount weiXinPayAccount = obtainWeiXinPayAccount(tenantId, branchId);
        ValidateUtils.notNull(weiXinPayAccount, "未配置微信支付账号！");

        String apiSecretKey = weiXinPayAccount.getApiSecretKey();
        String operationCertificate = weiXinPayAccount.getOperationCertificate();
        String operationCertificatePassword = weiXinPayAccount.getOperationCertificatePassword();

        Map<String, String> addRecommendConfRequestParameters = new HashMap<String, String>();
        addRecommendConfRequestParameters.put("mch_id", mchId);
        addRecommendConfRequestParameters.put("sub_mch_id", subMchId);
        addRecommendConfRequestParameters.put("sub_appid", subAppId);

        if (StringUtils.isNotBlank(subscribeAppId)) {
            addRecommendConfRequestParameters.put("subscribe_appid", subscribeAppId);
        }

        if (StringUtils.isNotBlank(receiptAppId)) {
            addRecommendConfRequestParameters.put("receipt_appid", receiptAppId);
        }

        addRecommendConfRequestParameters.put("nonce_str", RandomStringUtils.randomAlphanumeric(32));

        addRecommendConfRequestParameters.put("sign_type", signType);
        addRecommendConfRequestParameters.put("sign", generateSign(addRecommendConfRequestParameters, apiSecretKey, signType));

        String addRecommendConfFinalData = generateFinalData(addRecommendConfRequestParameters);
        Map<String, String> addRecommendConfResult = callWeiXinPaySystem(WEI_XIN_PAY_API_URL + "/secapi/mkt/addrecommendconf", addRecommendConfFinalData, operationCertificate, operationCertificatePassword);

        String returnCode = addRecommendConfResult.get("return_code");
        ValidateUtils.isTrue(Constants.SUCCESS.equals(returnCode), addRecommendConfResult.get("return_msg"));

        ValidateUtils.isTrue(checkSign(addRecommendConfResult, apiSecretKey, signType), "微信系统返回结果签名校验未通过！");
        ValidateUtils.isTrue(Constants.SUCCESS.equals(addRecommendConfResult.get("result_code")), addRecommendConfResult.get("err_code_des"));
        return addRecommendConfResult;
    }

    /**
     * 服务商子商户开发配置新增支付目录
     *
     * @param addSubDevConfigModel
     * @return
     * @throws DocumentException
     */
    public static Map<String, String> addSubDevConfig(AddSubDevConfigModel addSubDevConfigModel) throws DocumentException {
        addSubDevConfigModel.validateAndThrow();
        String tenantId = addSubDevConfigModel.getTenantId();
        String branchId = addSubDevConfigModel.getBranchId();
        String jsApiPath = addSubDevConfigModel.getJsApiPath();
        String subAppId = addSubDevConfigModel.getSubAppId();

        WeiXinPayAccount weiXinPayAccount = obtainWeiXinPayAccount(tenantId, branchId);
        ValidateUtils.notNull(weiXinPayAccount, "未配置微信支付账号！");
        String appId = weiXinPayAccount.getAppId();
        String mchId = weiXinPayAccount.getMchId();
        String apiSecretKey = weiXinPayAccount.getApiSecretKey();
        String operationCertificate = weiXinPayAccount.getOperationCertificate();
        String operationCertificatePassword = weiXinPayAccount.getOperationCertificatePassword();

        Map<String, String> addSubDevConfigRequestParameters = new HashMap<String, String>();
        addSubDevConfigRequestParameters.put("appid", appId);
        addSubDevConfigRequestParameters.put("mch_id", mchId);

        if (StringUtils.isNotBlank(jsApiPath)) {
            addSubDevConfigRequestParameters.put("jsapi_path", jsApiPath);
        }

        if (StringUtils.isNotBlank(subAppId)) {
            addSubDevConfigRequestParameters.put("sub_appid", subAppId);
        }

        addSubDevConfigRequestParameters.put("sign", generateSign(addSubDevConfigRequestParameters, apiSecretKey, Constants.MD5));
        String addSubDevConfigFinalData = generateFinalData(addSubDevConfigRequestParameters);
        Map<String, String> addSubDevConfigResult = callWeiXinPaySystem(WEI_XIN_PAY_API_URL + "/secapi/mch/addsubdevconfig", addSubDevConfigFinalData, operationCertificate, operationCertificatePassword);

        String returnCode = addSubDevConfigResult.get("return_code");
        ValidateUtils.isTrue(Constants.SUCCESS.equals(returnCode), addSubDevConfigResult.get("return_msg"));

        ValidateUtils.isTrue(checkSign(addSubDevConfigResult, apiSecretKey, Constants.MD5), "微信系统返回结果签名校验未通过！");
        ValidateUtils.isTrue(Constants.SUCCESS.equals(addSubDevConfigResult.get("result_code")), addSubDevConfigResult.get("err_code_des"));
        return addSubDevConfigResult;
    }

    /**
     * 服务商子商户开发配置查询
     *
     * @param querySubDevConfigModel
     * @return
     * @throws DocumentException
     */
    public static Map<String, String> querySubDevConfig(QuerySubDevConfigModel querySubDevConfigModel) throws DocumentException {
        querySubDevConfigModel.validateAndThrow();
        String tenantId = querySubDevConfigModel.getTenantId();
        String branchId = querySubDevConfigModel.getBranchId();

        WeiXinPayAccount weiXinPayAccount = obtainWeiXinPayAccount(tenantId, branchId);
        ValidateUtils.notNull(weiXinPayAccount, "未配置微信支付账号！");

        String apiSecretKey = weiXinPayAccount.getApiSecretKey();
        String operationCertificate = weiXinPayAccount.getOperationCertificate();
        String operationCertificatePassword = weiXinPayAccount.getOperationCertificatePassword();

        Map<String, String> querySubDevConfigRequestParameters = new HashMap<String, String>();
        querySubDevConfigRequestParameters.put("appid", querySubDevConfigModel.getAppId());
        querySubDevConfigRequestParameters.put("mch_id", querySubDevConfigModel.getMchId());
        querySubDevConfigRequestParameters.put("sub_mch_id", querySubDevConfigModel.getSubMchId());

        querySubDevConfigRequestParameters.put("sign", generateSign(querySubDevConfigRequestParameters, apiSecretKey, Constants.MD5));
        String querySubDevConfigFinalData = generateFinalData(querySubDevConfigRequestParameters);
        Map<String, String> querySubDevConfigResult = callWeiXinPaySystem(WEI_XIN_PAY_API_URL + "/secapi/mch/querysubdevconfig", querySubDevConfigFinalData, operationCertificate, operationCertificatePassword);

        String returnCode = querySubDevConfigResult.get("return_code");
        ValidateUtils.isTrue(Constants.SUCCESS.equals(returnCode), querySubDevConfigResult.get("return_msg"));

        ValidateUtils.isTrue(checkSign(querySubDevConfigResult, apiSecretKey, Constants.MD5), "微信系统返回结果签名校验未通过！");
        ValidateUtils.isTrue(Constants.SUCCESS.equals(querySubDevConfigResult.get("result_code")), querySubDevConfigResult.get("err_code_des"));
        return querySubDevConfigResult;
    }

    /**
     * 银行特约商户录入
     *
     * @param addSubMchModel
     * @return
     * @throws DocumentException
     */
    public static Map<String, String> addSubMch(AddSubMchModel addSubMchModel) throws DocumentException {
        addSubMchModel.validateAndThrow();

        String tenantId = addSubMchModel.getTenantId();
        String branchId = addSubMchModel.getBranchId();
        String merchantName = addSubMchModel.getMerchantName();
        String merchantShortName = addSubMchModel.getMerchantShortName();
        String servicePhone = addSubMchModel.getServicePhone();
        String contact = addSubMchModel.getContact();
        String contactPhone = addSubMchModel.getContactPhone();
        String contactEmail = addSubMchModel.getContactEmail();
        String channelId = addSubMchModel.getChannelId();
        String business = addSubMchModel.getBusiness();
        String contactWeChatIdType = addSubMchModel.getContactWeChatIdType();
        String contactWeChatId = addSubMchModel.getContactWeChatId();
        String merchantRemark = addSubMchModel.getMerchantRemark();

        WeiXinPayAccount weiXinPayAccount = obtainWeiXinPayAccount(tenantId, branchId);
        ValidateUtils.notNull(weiXinPayAccount, "未配置微信支付账号！");

        String appId = weiXinPayAccount.getAppId();
        String mchId = weiXinPayAccount.getMchId();
        String apiSecretKey = weiXinPayAccount.getApiSecretKey();
        String operationCertificate = weiXinPayAccount.getOperationCertificate();
        String operationCertificatePassword = weiXinPayAccount.getOperationCertificatePassword();

        Map<String, String> addSubMchRequestParameters = new HashMap<String, String>();
        addSubMchRequestParameters.put("appid", appId);
        addSubMchRequestParameters.put("mch_id", mchId);
        addSubMchRequestParameters.put("merchant_name", merchantName);
        addSubMchRequestParameters.put("merchant_shortname", merchantShortName);
        addSubMchRequestParameters.put("service_phone", servicePhone);

        if (StringUtils.isNotBlank(contact)) {
            addSubMchRequestParameters.put("contact", contact);
        }

        if (StringUtils.isNotBlank(contactPhone)) {
            addSubMchRequestParameters.put("contact_phone", contactPhone);
        }

        if (StringUtils.isNotBlank(contactEmail)) {
            addSubMchRequestParameters.put("contact_email", contactEmail);
        }

        addSubMchRequestParameters.put("channel_id", channelId);
        addSubMchRequestParameters.put("business", business);

        if (StringUtils.isNotBlank(contactWeChatIdType)) {
            addSubMchRequestParameters.put("contact_wechatid_type", contactWeChatIdType);
        }

        if (StringUtils.isNotBlank(contactWeChatId)) {
            addSubMchRequestParameters.put("contact_wechatid", contactWeChatId);
        }

        addSubMchRequestParameters.put("merchant_remark", merchantRemark);

        addSubMchRequestParameters.put("sign", generateSign(addSubMchRequestParameters, apiSecretKey, Constants.MD5));
        String addSubMchFinalData = generateFinalData(addSubMchRequestParameters);
        Map<String, String> addSubMchResult = callWeiXinPaySystem(WEI_XIN_PAY_API_URL + "/secapi/mch/submchmanage?action=add", addSubMchFinalData, operationCertificate, operationCertificatePassword);

        String returnCode = addSubMchResult.get("return_code");
        ValidateUtils.isTrue(Constants.SUCCESS.equals(returnCode), addSubMchResult.get("return_msg"));

        ValidateUtils.isTrue(checkSign(addSubMchResult, apiSecretKey, Constants.MD5), "微信系统返回结果签名校验未通过！");
        ValidateUtils.isTrue(Constants.SUCCESS.equals(addSubMchResult.get("result_code")), addSubMchResult.get("err_code_des"));
        return addSubMchResult;
    }

    /**
     * 银行特约商户信息修改
     *
     * @param modifyMchInfoModel
     * @return
     * @throws DocumentException
     */
    public static Map<String, String> modifyMchInfo(ModifyMchInfoModel modifyMchInfoModel) throws DocumentException {
        modifyMchInfoModel.validateAndThrow();

        String tenantId = modifyMchInfoModel.getTenantId();
        String branchId = modifyMchInfoModel.getBranchId();
        String merchantShortName = modifyMchInfoModel.getMerchantShortName();
        String servicePhone = modifyMchInfoModel.getServicePhone();

        WeiXinPayAccount weiXinPayAccount = obtainWeiXinPayAccount(tenantId, branchId);
        ValidateUtils.notNull(weiXinPayAccount, "未配置微信支付账号！");
        String mchId = weiXinPayAccount.getMchId();
        String subMchId = weiXinPayAccount.getSubMchId();
        String apiSecretKey = weiXinPayAccount.getApiSecretKey();
        String operationCertificate = weiXinPayAccount.getOperationCertificate();
        String operationCertificatePassword = weiXinPayAccount.getOperationCertificatePassword();

        Map<String, String> modifyMchInfoRequestParameters = new HashMap<String, String>();
        modifyMchInfoRequestParameters.put("mch_id", mchId);
        modifyMchInfoRequestParameters.put("sub_mch_id", subMchId);

        if (StringUtils.isNotBlank(merchantShortName)) {
            modifyMchInfoRequestParameters.put("merchant_shortname", merchantShortName);
        }

        if (StringUtils.isNotBlank(servicePhone)) {
            modifyMchInfoRequestParameters.put("service_phone", servicePhone);
        }

        modifyMchInfoRequestParameters.put("sign", generateSign(modifyMchInfoRequestParameters, apiSecretKey, Constants.MD5));
        String modifyMchInfoFinalData = generateFinalData(modifyMchInfoRequestParameters);
        Map<String, String> modifyMchInfoResult = callWeiXinPaySystem(WEI_XIN_PAY_API_URL + "/secapi/mch/modifymchinfo", modifyMchInfoFinalData, operationCertificate, operationCertificatePassword);

        String returnCode = modifyMchInfoResult.get("return_code");
        ValidateUtils.isTrue(Constants.SUCCESS.equals(returnCode), modifyMchInfoResult.get("return_msg"));

        ValidateUtils.isTrue(checkSign(modifyMchInfoResult, apiSecretKey, Constants.MD5), "微信系统返回结果签名校验未通过！");
        ValidateUtils.isTrue(Constants.SUCCESS.equals(modifyMchInfoResult.get("result_code")), modifyMchInfoResult.get("err_code_des"));
        return modifyMchInfoResult;
    }

    public Map<String, String> querySubMch(QuerySubMchModel querySubMchModel) {
        return null;
    }
}
