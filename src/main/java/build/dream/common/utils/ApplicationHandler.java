package build.dream.common.utils;

import build.dream.common.constants.Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by liuyandong on 2017/3/24.
 */
public class ApplicationHandler {
    public static ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public static HttpServletRequest getHttpServletRequest() {
        return getServletRequestAttributes().getRequest();
    }

    public static HttpServletResponse getHttpServletResponse() {
        return getServletRequestAttributes().getResponse();
    }

    public static Map<String, String> getRequestParameters() {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        Map<String, String> requestParameters = new LinkedHashMap<>();
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        for (Map.Entry<String, String[]> parameterEntry : parameterMap.entrySet()) {
            requestParameters.put(parameterEntry.getKey(), StringUtils.trimToNull(StringUtils.join(parameterEntry.getValue(), ",")));
        }
        return requestParameters;
    }

    public static String getRequestParameter(String requestParameterName) {
        return StringUtils.trimToNull(StringUtils.join(getHttpServletRequest().getParameterValues(requestParameterName), ","));
    }

    public static String getSessionId() {
        return getServletRequestAttributes().getSessionId();
    }

    public static ServletContext getServletContext() {
        return getHttpServletRequest().getServletContext();
    }

    public static String getRemoteAddress() {
        return getHttpServletRequest().getRemoteAddr();
    }

    public static HttpSession getHttpSession() {
        return getHttpServletRequest().getSession();
    }

    public static String getRequestUrl() {
        return getHttpServletRequest().getRequestURL().toString();
    }

    public static <T> T instantiateObject(Class<T> objectClass, Map<String, String> parameters) throws NoSuchFieldException, InstantiationException, ParseException, IllegalAccessException {
        return instantiateObject(objectClass, parameters, Constants.DEFAULT_DATE_PATTERN);
    }

    public static <T> T instantiateObject(Class<T> objectClass, Map<String, String> parameters, String datePattern) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ParseException {
        T object = objectClass.newInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);

        Field[] fields = objectClass.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            String fieldValue = parameters.get(fieldName);
            if (StringUtils.isBlank(fieldValue)) {
                continue;
            }
            ReflectionUtils.makeAccessible(field);
            Class<?> fieldClass = field.getType();
            if (fieldClass == Byte.class || fieldClass == byte.class) {
                field.set(object, Byte.valueOf(fieldValue));
            } else if (fieldClass == Short.class || fieldClass == short.class) {
                field.set(object, Short.valueOf(fieldValue));
            } else if (fieldClass == Integer.class || fieldClass == int.class) {
                field.set(object, Integer.valueOf(fieldValue));
            } else if (fieldClass == Long.class || fieldClass == long.class) {
                field.set(object, Long.valueOf(fieldValue));
            } else if (fieldClass == Float.class || fieldClass == float.class) {
                field.set(object, Float.valueOf(fieldValue));
            } else if (fieldClass == Double.class || fieldClass == double.class) {
                field.set(object, Double.valueOf(fieldValue));
            } else if (fieldClass == Character.class || fieldClass == char.class) {
                field.set(object, fieldValue.charAt(0));
            } else if (fieldClass == String.class) {
                field.set(object, fieldValue);
            } else if (fieldClass == Boolean.class || fieldClass == boolean.class) {
                field.set(object, Boolean.valueOf(fieldValue));
            } else if (fieldClass == Date.class) {
                field.set(object, simpleDateFormat.parse(fieldValue));
            } else if (fieldClass == BigInteger.class) {
                field.set(object, BigInteger.valueOf(Long.valueOf(fieldValue)));
            } else if (fieldClass == BigDecimal.class) {
                field.set(object, BigDecimal.valueOf(Double.valueOf(fieldValue)));
            }
        }
        return object;
    }

    private static ApplicationContext applicationContext = null;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationHandler.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    private static Validator validator = null;

    public static Validator obtainValidator() {
        if (validator == null) {
            validator = (Validator) applicationContext.getBean("validator");
        }
        return validator;
    }

    public static BigInteger obtainBigIntegerFromMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        } else if (value instanceof Integer || value instanceof Long) {
            return BigInteger.valueOf(Long.valueOf(value.toString()));
        } else if (value instanceof Float) {
            return BigInteger.valueOf(Float.valueOf(value.toString()).intValue());
        } else if (value instanceof Double) {
            return BigInteger.valueOf(Double.valueOf(value.toString()).longValue());
        } else if (value instanceof BigDecimal) {
            return BigInteger.valueOf(BigDecimal.valueOf(Double.valueOf(value.toString())).longValue());
        } else if (value instanceof BigInteger) {
            return (BigInteger) value;
        }
        return null;
    }

    public static Long obtainLongFromMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        } else if (value instanceof Number) {
            return Double.valueOf(value.toString()).longValue();
        }
        return null;
    }

    public static Integer obtainIntegerFromMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        } else if (value instanceof Number) {
            return Double.valueOf(value.toString()).intValue();
        }
        return null;
    }

    public static BigDecimal obtainBigDecimalFromMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        } else if (value instanceof Number) {
            return BigDecimal.valueOf(Double.valueOf(value.toString()));
        }
        return null;
    }

    public static String obtainStringFromMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    public static Boolean obtainBooleanFromMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        } else if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof Number) {
            double number = Double.valueOf(value.toString());
            if (number == 1) {
                return true;
            } else {
                return false;
            }
        } else {
            return Boolean.valueOf(value.toString());
        }
    }

    public static void notNullAndPut(Map<String, Object> targetMap, String key, Object value, String message) {
        Validate.notNull(value, message);
        targetMap.put(key, value);
    }

    public static void notBlankAndPut(Map<String, String> targetMap, String key, String value, String message) {
        Validate.isTrue(StringUtils.isNotBlank(value), message);
        targetMap.put(key, value);
    }

    public static void ifNotNullPut(Map<String, Object> targetMap, String key, Object value) {
        if (value != null) {
            targetMap.put(key, value);
        }
    }

    public static void ifNotBlankPut(Map<String, String> targetMap, String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            targetMap.put(key, value);
        }
    }

    public static void ifNotBlankPut(Map<String, String> sourceMap, Map<String, String> targetMap, String... keys) {
        for (String key : keys) {
            String value = sourceMap.get(key);
            if (StringUtils.isNotBlank(key)) {
                targetMap.put(key, value);
            }
        }
    }

    public static void ifNotBlankPut(Map<String, String> sourceMap, Map<String, String> targetMap, Map<String, String> keyMapping) {
        Set<Map.Entry<String, String>> entries = keyMapping.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String value = sourceMap.get(entry.getKey());
            if (StringUtils.isNotBlank(value)) {
                targetMap.put(entry.getValue(), value);
            }
        }
    }

    public static String obtainParameterErrorMessage(String parameterName) {
        return String.format(Constants.PARAMETER_ERROR_MESSAGE_PATTERN, parameterName);
    }
}