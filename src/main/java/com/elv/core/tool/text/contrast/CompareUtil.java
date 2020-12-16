package com.elv.core.tool.text.contrast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.elv.core.tool.text.model.LayoutRoomEntity;
import com.elv.core.util.BeanUtil;
import com.elv.core.util.JsonUtil;

/**
 * 对比工具
 *
 * @author lxh
 * @since 2020-08-27
 */
public class CompareUtil {

    /**
     * 属性对比
     *
     * @param oldObject 旧实例对象
     * @param newObject 新实例对象
     * @return com.elv.core.tool.text.contrast.CompareVO
     * @see #compare(Object, Object, Collection)
     */
    public static CompareVO compare(Object oldObject, Object newObject) {
        return compare(oldObject, newObject, null);
    }

    /**
     * 属性对比
     * <p>
     * 1.支持不同类实例对比
     * 2.不支持深度对比
     *
     * @param oldObject     旧实例对象
     * @param newObject     新实例对象
     * @param compareFields 需要对比的属性，可空，为空时对比全部属性
     * @return com.elv.core.tool.text.contrast.CompareVO
     */
    public static CompareVO compare(Object oldObject, Object newObject, Collection<String> compareFields) {
        List<CompareDetailVO> detailVOs = new ArrayList<>();
        if (oldObject == null || newObject == null) {
            return CompareVO.of().detailVOs(detailVOs);
        }

        boolean limitField = false;
        if (compareFields == null) {
            // do-nothing.
        } else if (compareFields.size() == 0) {
            return CompareVO.of().detailVOs(detailVOs);
        } else {
            limitField = true;
        }

        try {
            Map<String, Method> oldGetterMap = BeanUtil.getGetterMap(oldObject.getClass());
            Map<String, Method> newGetterMap = BeanUtil.getGetterMap(newObject.getClass());
            if (oldGetterMap.size() == 0 || newGetterMap.size() == 0) {
                return CompareVO.of().detailVOs(detailVOs);
            }

            for (Entry<String, Method> oldEntry : oldGetterMap.entrySet()) {
                String fieldName = oldEntry.getKey();
                if (limitField && !compareFields.contains(fieldName)) {
                    continue;
                }
                Method newMethod = newGetterMap.get(fieldName);
                if (newMethod == null) {
                    continue;
                }
                Method oldMethod = oldEntry.getValue();
                Object oldValue = oldMethod.invoke(oldObject);
                Object newValue = newMethod.invoke(newObject);

                detailVOs.add(CompareDetailVO.of().fieldName(fieldName).before(oldValue).after(newValue));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return CompareVO.of().detailVOs(detailVOs);

    }

    public static void main(String[] args) {
        LayoutRoomEntity oldEntity = new LayoutRoomEntity();
        LayoutRoomEntity newEntity = new LayoutRoomEntity();
        newEntity.setHotelId(441L);

        List<String> list = new ArrayList<>();
        list.add("hotelId");
        list.add("layoutId");

        CompareVO compareVO = compare(oldEntity, newEntity, list);

        Map<String, CompareDetailVO> compareMap = compareVO.getCompareMap();
        for (Entry<String, CompareDetailVO> entry : compareMap.entrySet()) {
            System.out.println(entry.getKey() + "-->" + JsonUtil.toJson(entry.getValue()));
        }

        compareVO.getChangedFields().stream().forEach(item -> System.out.println(item));

        compareVO.getChangedMap().forEach((key, value) -> System.out.println(key + "-->" + JsonUtil.toJson(value)));
        System.out.println();
        compareVO.getUnChangedMap().forEach((key, value) -> System.out.println(key + "-->" + JsonUtil.toJson(value)));

    }

}
