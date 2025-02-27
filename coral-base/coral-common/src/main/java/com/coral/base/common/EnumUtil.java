package com.coral.base.common;

import com.coral.base.common.enums.IEnum;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author huss
 */
public final class EnumUtil {

    /**
     * 获取enum描述
     *
     * @param clazz
     * @return
     */
    public static String description(Class<? extends IEnum> clazz) {
        List<String> remarks = Stream.of(clazz.getEnumConstants()).map(e -> {
            StringBuilder sb = new StringBuilder();
            if (org.apache.commons.lang3.StringUtils.isNotBlank(e.getName())) {
                sb.append(e.getName()).append(":");
            }
            if (Objects.nonNull(e.getCode())) {
                sb.append(e.getCode());
            }

            return sb.toString();
        }).collect(Collectors.toList());
        return String.join("    ", remarks);
    }

    public static <T extends IEnum> T classNameOf(Class<T> clazz, String className) {
        T[] enums = clazz.getEnumConstants();
        return Arrays.stream(enums).filter(e -> StringUtils.isNotBlank(className) && e.getClass().getSimpleName().equals(className)).findFirst()
                .orElseThrow(() -> new NoSuchElementException("name not exist"));
    }

    /**
     * 根据名称获取enum
     *
     * @param clazz
     * @param name
     * @return
     */
    public static <T extends IEnum> T nameOf(Class<T> clazz, String name) {
        T[] enums = clazz.getEnumConstants();
        return Arrays.stream(enums).filter(e -> StringUtils.isNotBlank(name) && e.getName().equals(name)).findFirst()
                .orElseThrow(() -> new NoSuchElementException("name not exist"));
    }

    /**
     * 根据code获取enum
     *
     * @param clazz
     * @param code
     * @return
     */
    public static <T extends IEnum> T codeOf(Class<T> clazz, Object code) {
        T[] enums = clazz.getEnumConstants();
        return Arrays.stream(enums).filter(e -> !Objects.isNull(code) && e.getCode().toString().equals(code.toString()))
                .findFirst().orElseThrow(() -> new NoSuchElementException("code not exist"));
    }

    public static <T extends IEnum> List<Map<String, Object>> toList(Class<T> clazz) {
        T[] array = clazz.getEnumConstants();
        List<Map<String, Object>> list = new ArrayList<>();
        for (T t : array) {
            list.add(t.getParams());
        }
        return list;
    }

}
