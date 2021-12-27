package com.elv.core.tool.application.log.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

/**
 * @author lxh
 * @since 2020-08-27
 */
public class BLogCompareVO {

    private List<BLogGroupVO> groupVOs;

    public List<BLogGroupVO> getGroupVOs() {
        return groupVOs;
    }

    public void setGroupVOs(List<BLogGroupVO> groupVOs) {
        this.groupVOs = groupVOs;
    }

    public BLogCompareVO detailVOs(List<BLogGroupVO> detailVOs) {
        this.groupVOs = detailVOs;
        return this;
    }

    public static BLogCompareVO of() {
        BLogCompareVO compareVO = new BLogCompareVO();
        compareVO.setGroupVOs(new ArrayList<>());
        return compareVO;
    }

    public List<BLogDetailVO> getDetailVOs() {
        return Optional.ofNullable(this.getGroupVOs()).orElse(Collections.emptyList()).stream()
                .flatMap(item -> item.getDetailVOs().stream()).collect(Collectors.toList());
    }

    /**
     * 是否有变化
     *
     * @return boolean
     */
    public boolean changed() {
        return this.getChangedMap().size() > 0;
    }

    /**
     * 是否包含给定的多个属性名
     * <p>
     * 注意：存在即包含
     *
     * @param fieldNames 属性列表
     * @return boolean
     */
    public boolean contains(Collection<String> fieldNames) {
        if (CollectionUtils.isEmpty(fieldNames)) {
            return false;
        }

        Set<String> changedFields = this.getChangedFields();
        if (changedFields.size() == 0) {
            return false;
        }

        for (String fieldName : fieldNames) {
            if (changedFields.contains(fieldName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 是否包含给定的多个属性名
     * <p>
     * 注意：存在即包含
     *
     * @param fieldNames 属性数组
     * @return boolean
     */
    public boolean contains(String... fieldNames) {
        if (fieldNames == null || fieldNames.length == 0) {
            return false;
        }

        Set<String> changedFields = this.getChangedFields();
        if (changedFields.size() == 0) {
            return false;
        }

        for (String fieldName : fieldNames) {
            if (changedFields.contains(fieldName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 发生变化的属性
     *
     * @return java.util.Set
     */
    public Set<String> getChangedFields() {
        return this.getChangedMap().keySet();
    }

    /**
     * 未发生变化的属性
     *
     * @return java.util.Set
     */
    public Set<String> getUnChangedFields() {
        return this.getUnChangedMap().keySet();
    }

    /**
     * 对比结果map
     *
     * @return java.util.Map
     */
    public Map<String, BLogGroupVO> getCompareMap() {
        if (CollectionUtils.isEmpty(this.getGroupVOs())) {
            return Collections.emptyMap();
        }

        return this.getGroupVOs().stream().collect(
                Collectors.toMap(key -> key.getGroupCode(), value -> value, (key1, key2) -> key2, LinkedHashMap::new));
    }

    /**
     * 属性值已发生变化map
     *
     * @return java.util.Map
     */
    public Map<String, BLogGroupVO> getChangedMap() {
        if (CollectionUtils.isEmpty(this.getGroupVOs())) {
            return Collections.emptyMap();
        }

        return this.getGroupVOs().stream().filter(item -> item.changed()).collect(
                Collectors.toMap(key -> key.getGroupCode(), value -> value, (key1, key2) -> key2, LinkedHashMap::new));
    }

    /**
     * 关心的属性值map
     *
     * @return java.util.Map
     */
    public Map<String, BLogGroupVO> getCareMap() {
        if (CollectionUtils.isEmpty(this.getGroupVOs())) {
            return Collections.emptyMap();
        }

        return this.getGroupVOs().stream().filter(item -> item.changed() || item.isUptKeep()).collect(
                Collectors.toMap(key -> key.getGroupCode(), value -> value, (key1, key2) -> key2, LinkedHashMap::new));
    }

    /**
     * 属性值未发生变化map
     *
     * @return java.util.Map
     */
    public Map<String, BLogGroupVO> getUnChangedMap() {
        if (CollectionUtils.isEmpty(this.getGroupVOs())) {
            return Collections.emptyMap();
        }

        return this.getGroupVOs().stream().filter(item -> !item.changed()).collect(
                Collectors.toMap(key -> key.getGroupCode(), value -> value, (key1, key2) -> key2, LinkedHashMap::new));
    }

}
