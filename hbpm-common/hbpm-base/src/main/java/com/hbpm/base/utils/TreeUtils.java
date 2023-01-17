package com.hbpm.base.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author huangxiuqi
 */
public class TreeUtils {

    /**
     * 列表转树形
     * @param list 原始列表
     * @param mapper 类型转换
     * @param idFunc 获取元素id
     * @param pidFunc 获取元素parentId
     * @param setChildren 设置元素children
     * @return 树形结构
     * @param <T> 原始类型
     * @param <R> 结果类型
     * @param <K> id和parentId类型
     */
    public static <T, R, K> List<R> listToTree(List<T> list,
                                               Function<? super T, ? extends R> mapper,
                                               Function<? super R, ? extends K> idFunc,
                                               Function<? super R, ? extends K> pidFunc,
                                               TreeChildConsumer<R> setChildren) {
        List<R> result = new ArrayList<>();
        List<R> mapList = list.stream()
                .map(mapper)
                .collect(Collectors.toList());
        Map<K, List<R>> childrenMap = mapList.stream()
                .collect(Collectors.toMap(idFunc, i -> new ArrayList<>()));
        for (R node : mapList) {
            K pid = pidFunc.apply(node);
            childrenMap.getOrDefault(pid, result).add(node);
        }
        for (R node : mapList) {
            K id = idFunc.apply(node);
            setChildren.accept(node, childrenMap.getOrDefault(id, new ArrayList<>()));
        }
        return result;
    }

    @FunctionalInterface
    public interface TreeChildConsumer<T> {
        /**
         * 设置children
         * @param t 类型
         * @param tList 子元素列表
         */
        void accept(T t, List<T> tList);
    }
}
