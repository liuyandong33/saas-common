package build.dream.common.utils;

import build.dream.common.api.ApiRest;
import build.dream.common.constants.Constants;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by liuyandong on 2017/7/24.
 */
public class ProxyUtils {
    private static RestTemplate REST_TEMPLATE;
    private static HttpHeaders HTTP_HEADERS;

    public static RestTemplate obtainRestTemplate() {
        if (REST_TEMPLATE == null) {
            REST_TEMPLATE = ApplicationHandler.getBean(RestTemplate.class);
        }
        return REST_TEMPLATE;
    }

    public static HttpHeaders obtainHttpHeaders() {
        if (MapUtils.isEmpty(HTTP_HEADERS)) {
            HTTP_HEADERS = new HttpHeaders();
            HTTP_HEADERS.add(Constants.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=" + Constants.CHARSET_NAME_UTF_8);
        }
        return HTTP_HEADERS;
    }

    public static String obtainUrl(String partitionCode, String serviceName, String controllerName, String actionName) throws IOException {
        String deploymentEnvironment = ConfigurationUtils.getConfiguration(Constants.DEPLOYMENT_ENVIRONMENT);
        StringBuilder stringBuilder = new StringBuilder("http://");
        stringBuilder.append(deploymentEnvironment).append("-");
        if (StringUtils.isNotBlank(partitionCode)) {
            stringBuilder.append(partitionCode).append("-");
        }
        stringBuilder.append(serviceName).append("/").append(controllerName).append("/").append(actionName);
        return stringBuilder.toString();
    }

    public static HttpEntity<String> obtainHttpEntity(Map<String, String> requestParameters) throws UnsupportedEncodingException {
        String requestBody = null;
        if (MapUtils.isNotEmpty(requestParameters)) {
            requestBody = WebUtils.buildRequestBody(requestParameters, Constants.CHARSET_NAME_UTF_8);
        }
        return new HttpEntity<String>(requestBody, obtainHttpHeaders());
    }

    public static String doGetOriginalWithRequestParameters(String partitionCode, String serviceName, String controllerName, String actionName, Map<String, String> requestParameters) throws IOException {
        return WebUtils.doGetWithRequestParameters(SystemPartitionUtils.getUrl(partitionCode, serviceName, controllerName, actionName), requestParameters);
//        return obtainRestTemplate().getForObject(obtainUrl(partitionCode, serviceName, controllerName, actionName), String.class, requestParameters);
    }

    public static String doGetOriginalWithRequestParameters(String serviceName, String controllerName, String actionName, Map<String, String> requestParameters) throws IOException {
        return WebUtils.doGetWithRequestParameters(SystemPartitionUtils.getUrl(serviceName, controllerName, actionName), requestParameters);
    }

    public static String doPostOriginalWithRequestParameters(String partitionCode, String serviceName, String controllerName, String actionName, Map<String, String> requestParameters) throws IOException {
        return WebUtils.doPostWithRequestParameters(SystemPartitionUtils.getUrl(partitionCode, serviceName, controllerName, actionName), requestParameters);
    }

    public static String doPostOriginalWithRequestParameters(String serviceName, String controllerName, String actionName, Map<String, String> requestParameters) throws IOException {
        return WebUtils.doPostWithRequestParameters(SystemPartitionUtils.getUrl(serviceName, controllerName, actionName), requestParameters);
    }

    public static String doPostOriginalWithRequestParametersAndFiles(String partitionCode, String serviceName, String controllerName, String actionName, Map<String, Object> requestParameters) throws IOException {
        return WebUtils.doPostWithRequestParametersAndFiles(SystemPartitionUtils.getUrl(partitionCode, serviceName, controllerName, actionName), requestParameters);
    }

    public static String doPostOriginalWithRequestParametersAndFiles(String serviceName, String controllerName, String actionName, Map<String, Object> requestParameters) throws IOException {
        return WebUtils.doPostWithRequestParametersAndFiles(SystemPartitionUtils.getUrl(serviceName, controllerName, actionName), requestParameters);
    }

    public static String doPostOriginalWithRequestBody(String partitionCode, String serviceName, String controllerName, String actionName, String requestBody) throws IOException {
        return WebUtils.doPostWithRequestBody(SystemPartitionUtils.getUrl(partitionCode, serviceName, controllerName, actionName), requestBody);
    }

    public static String doPostOriginalWithRequestBody(String serviceName, String controllerName, String actionName, String requestBody) throws IOException {
        return WebUtils.doPostWithRequestBody(SystemPartitionUtils.getUrl(serviceName, controllerName, actionName), requestBody);
    }

    public static ApiRest doGetWithRequestParameters(String partitionCode, String serviceName, String controllerName, String actionName, Map<String, String> requestParameters) throws IOException {
        return ApiRest.fromJson(WebUtils.doGetWithRequestParameters(SystemPartitionUtils.getUrl(partitionCode, serviceName, controllerName, actionName), requestParameters));
    }

    public static ApiRest doGetWithRequestParameters(String serviceName, String controllerName, String actionName, Map<String, String> requestParameters) throws IOException {
        return ApiRest.fromJson(WebUtils.doGetWithRequestParameters(SystemPartitionUtils.getUrl(serviceName, controllerName, actionName), requestParameters));
    }

    public static ApiRest doPostWithRequestParameters(String partitionCode, String serviceName, String controllerName, String actionName, Map<String, String> requestParameters) throws IOException {
        return ApiRest.fromJson(WebUtils.doPostWithRequestParameters(SystemPartitionUtils.getUrl(partitionCode, serviceName, controllerName, actionName), requestParameters));
    }

    public static ApiRest doPostWithRequestParameters(String serviceName, String controllerName, String actionName, Map<String, String> requestParameters) throws IOException {
        return ApiRest.fromJson(WebUtils.doPostWithRequestParameters(SystemPartitionUtils.getUrl(serviceName, controllerName, actionName), requestParameters));
    }

    public static ApiRest doPostWithRequestParametersAndFiles(String partitionCode, String serviceName, String controllerName, String actionName, Map<String, Object> requestParameters) throws IOException {
        return ApiRest.fromJson(WebUtils.doPostWithRequestParametersAndFiles(SystemPartitionUtils.getUrl(partitionCode, serviceName, controllerName, actionName), requestParameters));
    }

    public static ApiRest doPostWithRequestParametersAndFiles(String serviceName, String controllerName, String actionName, Map<String, Object> requestParameters) throws IOException {
        return ApiRest.fromJson(WebUtils.doPostWithRequestParametersAndFiles(SystemPartitionUtils.getUrl(serviceName, controllerName, actionName), requestParameters));
    }

    public static ApiRest doPostWithRequestBody(String partitionCode, String serviceName, String controllerName, String actionName, String requestBody) throws IOException {
        return ApiRest.fromJson(WebUtils.doPostWithRequestBody(SystemPartitionUtils.getUrl(partitionCode, serviceName, controllerName, actionName), requestBody));
    }

    public static ApiRest doPostWithRequestBody(String serviceName, String controllerName, String actionName, String requestBody) throws IOException {
        return ApiRest.fromJson(WebUtils.doPostWithRequestBody(SystemPartitionUtils.getUrl(serviceName, controllerName, actionName), requestBody));
    }
}
