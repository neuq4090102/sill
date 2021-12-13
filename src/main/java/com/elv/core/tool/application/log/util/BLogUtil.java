package com.elv.core.tool.application.log.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.elv.core.tool.application.log.anno.BLog;
import com.elv.core.tool.application.log.vo.BLogCompareVO;
import com.elv.core.tool.application.log.vo.BLogDetailVO;
import com.elv.core.tool.application.log.vo.BLogGroupVO;
import com.elv.core.tool.application.log.vo.BLogObjectVO;
import com.elv.core.util.BeanUtil;
import com.elv.core.util.JsonUtil;
import com.elv.core.util.StrUtil;
import com.google.common.collect.Lists;

/**
 * @author lxh
 * @since 2021-10-21
 */
public class BLogUtil {

    private static final String ENUM_DELIMITER = ",|;|，|；|、";

    /**
     * 属性对比
     * <p>
     * 1.支持不同类实例对比
     * 2.不支持深度对比
     *
     * @param oldObject  旧实例对象
     * @param newObject  新实例对象
     * @param careFields 需要对比的属性，可空，为空时对比全部属性，也影响日志输出的展示顺序
     * @param refLangMap 多语言信息
     * @return com.elv.core.tool.application.log.vo.CompareVO
     */
    public static BLogCompareVO compare(Object oldObject, Object newObject, List<String> careFields,
            Map<String, String> refLangMap) {
        try {
            if (oldObject == null && newObject == null) {
                return BLogCompareVO.of();
            } else if (newObject == null) { // 删除
                newObject = oldObject.getClass().newInstance();
            } else if (oldObject == null) { // 新增
                oldObject = newObject.getClass().newInstance();
            }
        } catch (Exception e) {
        }

        BLogObjectVO objectVO = initObjectVO(oldObject, newObject);

        // 生产所有比对明细
        List<BLogDetailVO> detailVOs = generateDetailVOs(objectVO);
        if (CollectionUtils.isEmpty(detailVOs)) {
            return BLogCompareVO.of();
        }

        List<String> targetFields = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(careFields)) {
            targetFields.addAll(careFields);
        } else {
            targetFields = objectVO.getAllFields().stream().map(item -> item.getName()).collect(Collectors.toList());
        }

        Map<String, Field> fieldMap = objectVO.getFieldMap();
        Map<String, BLogDetailVO> detailVOMap = detailVOs.stream()
                .collect(Collectors.toMap(key -> key.getFieldName(), val -> val));
        Map<String, String> langMap = Optional.ofNullable(refLangMap).orElse(new HashMap<>());

        Map<String, BLogGroupVO> groupMap = new HashMap<>();
        for (int i = 0; i < targetFields.size(); i++) { // i决定了排序
            String fieldName = targetFields.get(i);
            Field field = fieldMap.get(fieldName);
            if (field == null) {
                continue;
            }
            BLogDetailVO detailVO = detailVOMap.get(fieldName);
            if (detailVO == null) {
                continue;
            }

            // 业务日志-解析
            BLog bLog = field.getDeclaredAnnotation(BLog.class);
            if (bLog == null) {
                continue;
            }

            // 业务日志-明细
            resetDetailVO(field, bLog, objectVO, detailVO, langMap);

            // 业务日志-分组
            BLogGroupVO groupVO;
            String groupCode = bLog.groupCode();
            if (StringUtils.isNotBlank(groupCode)) {
                String groupDesc = Optional.ofNullable(langMap.get(bLog.groupDesc())).orElse(bLog.groupDesc());
                String groupDelimiter = Optional.ofNullable(langMap.get(bLog.groupDelimiter()))
                        .orElse(bLog.groupDelimiter());
                groupVO = groupMap.get(groupCode);
                if (groupVO != null) {
                    groupVO.groupDesc(groupDesc).groupDelimiter(groupDelimiter).addDetail(detailVO);
                } else {
                    groupVO = BLogGroupVO.of().customizeGroup(true).groupCode(groupCode).groupDesc(groupDesc)
                            .groupDelimiter(groupDelimiter).addDetail(detailVO).sort(i);
                    groupMap.put(groupCode, groupVO);
                }
            } else {
                groupVO = BLogGroupVO.of().groupCode(fieldName).groupDesc(detailVO.getFieldDesc())
                        .before(detailVO.getBefore()).beforeDesc(detailVO.getBeforeDesc()).after(detailVO.getAfter())
                        .afterDesc(detailVO.getAfterDesc()).detailVOs(Lists.newArrayList(detailVO)).sort(i);
                groupMap.put(fieldName, groupVO);
            }
        }

        return BLogCompareVO.of().detailVOs(fetchGroupVOs(groupMap));

    }

    private static BLogObjectVO initObjectVO(Object oldObject, Object newObject) {
        BLogObjectVO objectVO = new BLogObjectVO();
        objectVO.setOldObject(oldObject);
        objectVO.setNewObject(newObject);
        objectVO.setAllFields(BeanUtil.getAllFields(oldObject.getClass()));
        objectVO.setOldGetterMap(BeanUtil.getGetterMap(oldObject.getClass()));
        objectVO.setNewGetterMap(BeanUtil.getGetterMap(newObject.getClass()));
        return objectVO;
    }

    private static List<BLogDetailVO> generateDetailVOs(BLogObjectVO objectVO) {
        Map<String, Method> oldGetterMap = objectVO.getOldGetterMap();
        Map<String, Method> newGetterMap = objectVO.getNewGetterMap();
        if (oldGetterMap.size() == 0 || newGetterMap.size() == 0) {
            return Collections.emptyList();
        }
        Map<String, Field> fieldMap = objectVO.getFieldMap();

        List<BLogDetailVO> detailVOs = new ArrayList<>();
        try {
            for (Entry<String, Method> oldEntry : oldGetterMap.entrySet()) {
                String fieldName = oldEntry.getKey();
                Field field = fieldMap.get(fieldName);
                if (field == null) {
                    continue;
                }

                Method oldMethod = oldEntry.getValue();
                Method newMethod = newGetterMap.get(fieldName);
                if (newMethod == null) {
                    continue;
                }

                // 默认解析
                Object oldValue = oldMethod.invoke(objectVO.getOldObject());
                Object newValue = newMethod.invoke(objectVO.getNewObject());
                detailVOs.add(BLogDetailVO.of().fieldName(fieldName).fieldDesc(fieldName).oldValue(oldValue)
                        .oldValueDesc(oldValue).newValue(newValue).newValueDesc(newValue));
            }

        } catch (Exception e) {

        }

        return detailVOs;
    }

    private static void resetDetailVO(Field field, BLog bLog, BLogObjectVO objectVO, BLogDetailVO detailVO,
            Map<String, String> langMap) {
        // 业务日志-其他属性
        String desc = Optional.ofNullable(langMap.get(bLog.desc())).orElse(bLog.desc());
        String prefix = Optional.ofNullable(langMap.get(bLog.prefix())).orElse(bLog.prefix());
        String suffix = Optional.ofNullable(langMap.get(bLog.suffix())).orElse(bLog.suffix());

        detailVO.fieldDesc(desc).prefix(prefix).suffix(suffix).uptKeep(bLog.uptKeep()).sort(bLog.groupSort());

        // 业务日志-枚举属性
        resetEnumInfo(field, bLog, detailVO);

        // 业务日志-属性映射
        resetMappingInfo(bLog, objectVO, detailVO);

        // 业务日志-集合属性
        if (detailVO.getOldValueDesc() != null && !(detailVO.getOldValueDesc() instanceof String)) {
            detailVO.oldValueDesc(JsonUtil.toJson(detailVO.getOldValueDesc()));
        }
        if (detailVO.getNewValueDesc() != null && !(detailVO.getNewValueDesc() instanceof String)) {
            detailVO.newValueDesc(JsonUtil.toJson(detailVO.getNewValueDesc()));
        }
    }

    /**
     * 如果是枚举类，替换为对应的属性值
     *
     * @param field
     * @param bLog
     * @param detailVO
     */
    private static void resetEnumInfo(Field field, BLog bLog, BLogDetailVO detailVO) {
        if (bLog == null || detailVO == null) {
            return;
        }
        Class<Enum> enumClass = (Class<Enum>) bLog.enumClass();
        if (enumClass == null) {
            return;
        }

        boolean complexEnum = bLog.complexEnum();
        Class<?> fieldType = field.getType();
        if (complexEnum) {
            Object referenceVal = detailVO.getNewValueDesc();
            if (detailVO.getOldValueDesc() != null) {
                referenceVal = detailVO.getOldValueDesc();
            }
            if (referenceVal != null && referenceVal instanceof String) {
                if (!Arrays.stream(referenceVal.toString().split(ENUM_DELIMITER)).filter(item -> !StrUtil.isDigit(item))
                        .findFirst().isPresent()) {
                    fieldType = int.class;
                }
            }
        }

        // 确定枚举的【key】和【描述】属性
        String enumKey;
        String enumDesc;
        if (StringUtils.isNotBlank(bLog.enumKey())) {
            enumKey = bLog.enumKey();
        } else {
            enumKey = confirmField(enumClass, fieldType);
        }
        if (StringUtils.isNotBlank(bLog.enumDesc())) {
            enumDesc = bLog.enumDesc();
        } else {
            enumDesc = confirmField(enumClass, String.class);
        }
        if (enumKey == null || enumDesc == null) {
            return;
        }

        try {
            Map<String, Method> oldEnumGetterMap = BeanUtil.getGetterMap(enumClass);
            Method enumKeyMethod = oldEnumGetterMap.get(enumKey);
            Method descMethod = oldEnumGetterMap.get(enumDesc);
            if (enumKeyMethod == null || descMethod == null) {
                return;
            }

            Map<Object, Object> enumMap = new HashMap<>();
            for (Enum enumItem : enumClass.getEnumConstants()) {
                enumMap.put(enumKeyMethod.invoke(enumItem).toString(), descMethod.invoke(enumItem));
            }

            if (detailVO.getOldValue() != null) {
                detailVO.oldValueDesc(Arrays.stream(detailVO.getOldValue().toString().split(ENUM_DELIMITER))
                        .map(item -> Optional.ofNullable(enumMap.get(item)).orElse("").toString())
                        .collect(Collectors.joining(bLog.enumDelimiter())));
            }

            if (detailVO.getNewValue() != null) {
                detailVO.newValueDesc(Arrays.stream(detailVO.getNewValue().toString().split(ENUM_DELIMITER))
                        .map(item -> Optional.ofNullable(enumMap.get(item)).orElse("").toString())
                        .collect(Collectors.joining(bLog.enumDelimiter())));
            }
        } catch (Exception e) {

        }
    }

    private static void resetMappingInfo(BLog bLog, BLogObjectVO objectVO, BLogDetailVO detailVO) {
        String fieldName = bLog.mappingField();
        if (StringUtils.isBlank(fieldName)) {
            return;
        }
        Field field = objectVO.getFieldMap().get(fieldName);
        if (field == null) {
            return;
        }

        Map<String, Method> oldGetterMap = objectVO.getOldGetterMap();
        Map<String, Method> newGetterMap = objectVO.getNewGetterMap();
        if (oldGetterMap.size() == 0 || newGetterMap.size() == 0) {
            return;
        }
        try {
            Method oldMethod = oldGetterMap.get(fieldName);
            Method newMethod = newGetterMap.get(fieldName);
            if (oldMethod != null) {
                detailVO.oldValueDesc(oldMethod.invoke(objectVO.getOldObject()));
            }
            if (newMethod != null) {
                detailVO.newValueDesc(newMethod.invoke(objectVO.getNewObject()));
            }
        } catch (Exception e) {

        }
    }

    /**
     * 获取分组对象
     *
     * @param groupMap
     * @return java.util.List
     */
    private static List<BLogGroupVO> fetchGroupVOs(Map<String, BLogGroupVO> groupMap) {
        if (groupMap == null || groupMap.size() == 0) {
            return Collections.emptyList();
        }

        List<BLogGroupVO> customizedGroupVOs = groupMap.values().stream().filter(item -> item.isCustomizeGroup())
                .collect(Collectors.toList());
        // 自定义分组
        for (BLogGroupVO groupVO : customizedGroupVOs) {
            List<BLogDetailVO> detailVOs = groupVO.getDetailVOs();
            if (CollectionUtils.isEmpty(detailVOs)) {
                continue;
            }
            if (detailVOs.size() == 1) {
                groupVO.setGroupDesc(detailVOs.get(0).getFieldDesc());
            } else {
                Collections.sort(detailVOs, Comparator.comparing(item -> item.getSort()));
            }
            groupVO.setBefore(detailVOs.stream().map(item -> item.getBefore().toString())
                    .collect(Collectors.joining(groupVO.getGroupDelimiter())));
            groupVO.setBeforeDesc(detailVOs.stream().map(item -> item.getBeforeDesc().toString())
                    .collect(Collectors.joining(groupVO.getGroupDelimiter())));
            groupVO.setAfter(detailVOs.stream().map(item -> item.getAfterDesc().toString())
                    .collect(Collectors.joining(groupVO.getGroupDelimiter())));
            groupVO.setAfterDesc(detailVOs.stream().map(item -> item.getAfterDesc().toString())
                    .collect(Collectors.joining(groupVO.getGroupDelimiter())));
        }

        List<BLogGroupVO> groupVOs = new ArrayList<>();
        groupVOs.addAll(
                groupMap.values().stream().filter(item -> !item.isCustomizeGroup()).collect(Collectors.toList()));
        groupVOs.addAll(customizedGroupVOs);

        Collections.sort(groupVOs, Comparator.comparing(item -> item.getSort()));

        return groupVOs;
    }

    /**
     * 获取第一个符合属性类型的枚举属性
     *
     * @param enumClass
     * @param fieldType
     * @return java.lang.String
     */
    private static String confirmField(Class<Enum> enumClass, Class<?> fieldType) {
        return BeanUtil.getAllFields(enumClass).stream().filter(item -> item.getType().isAssignableFrom(fieldType))
                .findFirst().map(item -> item.getName()).orElse(null);
    }

}
