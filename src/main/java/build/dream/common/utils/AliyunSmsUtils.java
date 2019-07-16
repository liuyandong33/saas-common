package build.dream.common.utils;

import build.dream.common.beans.WebResponse;
import build.dream.common.constants.Constants;
import build.dream.common.models.sms.SendSmsModel;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class AliyunSmsUtils {
    private static final String ACCESS_KEY_ID = ConfigurationUtils.getConfiguration(Constants.ALIYUN_ACCESS_KEY_ID);
    private static final String ACCESS_KEY_SECRET = ConfigurationUtils.getConfiguration(Constants.ALIYUN_ACCESS_KEY_SECRET);
    private static final String DY_SMS_API_URL = "https://dysmsapi.aliyuncs.com";

    /**
     * 发送短信
     *
     * @param sendSmsModel
     * @return
     */
    public static Map<String, Object> sendSms(SendSmsModel sendSmsModel) {
        sendSmsModel.validateAndThrow();

        String phoneNumbers = sendSmsModel.getPhoneNumbers();
        String signName = sendSmsModel.getSignName();
        String templateCode = sendSmsModel.getTemplateCode();
        String templateParam = sendSmsModel.getTemplateParam();
        String outId = sendSmsModel.getOutId();
        String smsUpExtendCode = sendSmsModel.getSmsUpExtendCode();

        Map<String, String> requestParameters = new HashMap<String, String>();
        requestParameters.put("AccessKeyId", ACCESS_KEY_ID);
        requestParameters.put("Action", "SendSms");
        requestParameters.put("Format", Constants.LOWER_CASE_JSON);
        requestParameters.put("RegionId", "cn-hangzhou");
        requestParameters.put("SignatureMethod", Constants.HMAC_SHA1);
        requestParameters.put("SignatureNonce", UUID.randomUUID().toString());
        requestParameters.put("SignatureVersion", "1.0");
        requestParameters.put("Timestamp", CustomDateUtils.buildISO8601SimpleDateFormat().format(new Date()));
        requestParameters.put("Version", "2017-05-25");
        requestParameters.put("PhoneNumbers", phoneNumbers);
        requestParameters.put("SignName", signName);
        requestParameters.put("TemplateCode", templateCode);
        requestParameters.put("TemplateParam", templateParam);
        if (StringUtils.isNotBlank(outId)) {
            requestParameters.put("OutId", outId);
        }
        if (StringUtils.isNotBlank(smsUpExtendCode)) {
            requestParameters.put("SmsUpExtendCode", smsUpExtendCode);
        }
        requestParameters.put("Signature", generateSignature(requestParameters, ACCESS_KEY_SECRET));

        WebResponse webResponse = OutUtils.doPostWithRequestParameters(DY_SMS_API_URL, requestParameters);

        Map<String, Object> resultMap = JacksonUtils.readValueAsMap(webResponse.getResult(), String.class, Object.class);
        String code = MapUtils.getString(resultMap, "Code");
        ValidateUtils.isTrue(Constants.OK.equals(code), MapUtils.getString(resultMap, "Message"));
        return resultMap;
    }

    /**
     * 生成签名
     *
     * @param requestParameters
     * @param accessSecret
     * @return
     */
    public static String generateSignature(Map<String, String> requestParameters, String accessSecret) {
        Map<String, String> sortedRequestParameters = new TreeMap<String, String>(requestParameters);
        List<String> requestParameterPairs = new ArrayList<String>();
        for (Map.Entry<String, String> entry : sortedRequestParameters.entrySet()) {
            requestParameterPairs.add(UrlUtils.encode(entry.getKey(), Constants.CHARSET_NAME_UTF_8) + "=" + UrlUtils.encode(entry.getValue(), Constants.CHARSET_NAME_UTF_8));
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constants.REQUEST_METHOD_POST);
        stringBuilder.append("&");
        stringBuilder.append(UrlUtils.encode("/", Constants.CHARSET_NAME_UTF_8));
        stringBuilder.append("&");
        stringBuilder.append(UrlUtils.encode(StringUtils.join(requestParameterPairs, "&"), Constants.CHARSET_NAME_UTF_8));
        return Base64.encodeBase64String(HmacUtils.hmacSha1(accessSecret + "&", stringBuilder.toString()));
    }
}