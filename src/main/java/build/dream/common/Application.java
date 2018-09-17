package build.dream.common;

import build.dream.common.constants.Constants;
import build.dream.common.erp.catering.domains.Coupon;
import build.dream.common.saas.domains.*;
import build.dream.common.utils.NamingStrategyUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * Created by liuyandong on 2017/7/25.
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
//        SpringApplication.run(Application.class, args);
        Class<?> domainClass = AlipayMaterialImage.class;

        StringBuilder stringBuilder1 = new StringBuilder("public static final class ColumnName extends BasicDomain.ColumnName {");
        Field[] fieldsa = domainClass.getDeclaredFields();
        for (Field field : fieldsa) {
            String name = NamingStrategyUtils.camelCaseToUnderscore(field.getName());
            stringBuilder1.append("public static final String ").append(name.toUpperCase()).append(" = ").append("\"").append(name).append("\";");
        }
        stringBuilder1.append("}");
        System.out.println(stringBuilder1.toString());

        String simpleName = domainClass.getSimpleName();
        StringBuilder code = new StringBuilder("public static class Builder {private final " + simpleName + " instance = new " + simpleName + "();");
        while (domainClass != Object.class) {
            Field[] fields = domainClass.getDeclaredFields();
            for (Field field : fields) {
                String name = field.getName();
                code.append("public Builder " + name + "(" + field.getType().getSimpleName() + " " + name + ") { instance.set" + name.substring(0, 1).toUpperCase() + name.substring(1) + "(" + name + ");return this;}");
            }
            domainClass = domainClass.getSuperclass();
        }
        code.append("public " + simpleName + " build() {return instance;}");
        code.append("}");

        System.out.println(code.toString());
        System.out.println("public static Builder builder() {return new Builder();}");

        BigDecimal sunQuality = BigDecimal.valueOf(Double.valueOf(1.9891)).multiply(Constants.BIG_DECIMAL_TEN.pow(30));
        System.out.println("太阳质量为：" + sunQuality + "kg");

        BigDecimal earthQuality = BigDecimal.valueOf(Double.valueOf(5.965)).multiply(Constants.BIG_DECIMAL_TEN.pow(24));
        System.out.println("地球质量为：" + earthQuality + "kg");

        System.out.println("太阳的质量为地球质量的" + sunQuality.divide(earthQuality, 10, BigDecimal.ROUND_DOWN) + "倍");

        BigDecimal sunRadius = BigDecimal.valueOf(6.955).multiply(BigDecimal.TEN.pow(8));
        System.out.println("太阳的半径为：" + sunRadius + "m");

        BigDecimal sunVolume = Constants.BIG_DECIMAL_FOUR.multiply(Constants.BIG_DECIMAL_PI).multiply(sunRadius.pow(3)).divide(Constants.BIG_DECIMAL_THREE, 10, BigDecimal.ROUND_DOWN);
        System.out.println("太阳的体积为：" + sunVolume + "m³");

        BigDecimal earthRadius = BigDecimal.valueOf(6371000);
        System.out.println("地球的半径为：" + earthRadius + "m");
        BigDecimal earthVolume = Constants.BIG_DECIMAL_FOUR.multiply(Constants.BIG_DECIMAL_PI).multiply(earthRadius.pow(3)).divide(Constants.BIG_DECIMAL_THREE, 10, BigDecimal.ROUND_DOWN);
        System.out.println("地球的体积为：" + earthVolume + "m³");

        System.out.println("太阳的体积是地球体积的:" + sunVolume.divide(earthVolume, 10, BigDecimal.ROUND_DOWN) + "倍");

        BigDecimal moonQuality = BigDecimal.valueOf(Double.valueOf(7.349)).multiply(Constants.BIG_DECIMAL_TEN.pow(22));
        System.out.println("月球的质量为：" + moonQuality + "kg");

        System.out.println("地球的质量是月球质量的：" + earthQuality.divide(moonQuality, 10, BigDecimal.ROUND_DOWN) + "倍");

        int array[] = buildRandomArray(10, 100);
        System.out.print("排序前：");
        printArray(array);

//        bubbleSort(array);
//        selectionSort(array);
        insertionSort(array);

        System.out.print("排序后：");
        printArray(array);
    }

    public static void printArray(int array[]) {
        int length = array.length;
        for (int index = 0; index < length; index++) {
            System.out.print(array[index]);
            if (index != length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    public static int[] buildRandomArray(int length, int maxValue) {
        int[] array = new int[length];
        for (int index = 0; index < length; index++) {
            array[index] = Double.valueOf(Math.random() * maxValue).intValue();
        }
        return array;
    }

    /**
     * 冒泡排序
     *
     * @param array
     */
    public static void bubbleSort(int array[]) {
        int length = array.length;
        for (int index = 0; index < length - 1; index++) {
            for (int innerIndex = 0; innerIndex < length - 1 - index; innerIndex++) {
                if (array[innerIndex] < array[innerIndex + 1]) {
                    int temp = array[innerIndex];
                    array[innerIndex] = array[innerIndex + 1];
                    array[innerIndex + 1] = temp;
                }
            }
        }
    }

    /**
     * 选择排序
     * 假设数组的长度为: length
     * 第一次循环，选出索引从 array[0]~array[length - 1] 中选出最大值或最小值与array[0]交换
     * 第二次循环，选出索引从 array[1]~array[length - 1] 中选出最大值或最小值与array[1]交换
     * 第三次循环，选出索引从 array[2]~array[length - 1] 中选出最大值或最小值与array[2]交换
     * ...
     *
     * @param array
     */
    public static void selectionSort(int array[]) {
        int length = array.length;
        for (int index = 0; index < length; index++) {
            int position = index;
            for (int innerIndex = index; innerIndex < length; innerIndex++) {
                if (array[innerIndex] < array[position]) {
                    position = innerIndex;
                }
            }

            if (index != position) {
                int temp = array[index];
                array[index] = array[position];
                array[position] = temp;
            }
        }
    }

    /**
     * 插入排序
     *
     * @param array
     */
    public static void insertionSort(int array[]) {
        int length = array.length;

        for (int index = 1; index < length; index++) {
            for (int innerIndex = index; innerIndex > 0 && array[innerIndex] > array[innerIndex - 1]; innerIndex--) {
                int temp = array[innerIndex];
                array[innerIndex] = array[innerIndex - 1];
                array[innerIndex - 1] = temp;
            }
        }
    }
}
