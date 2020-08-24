package com.elv.web.servlet;

import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * 自定义Json视图解析
 * <p>
 * 针对使用new ModelAndView(viewName, modelName, modelObject)返回的json结果如：
 * {"apiResult":{"code":30001,"msg":"寄件日期参数不合法"}}，
 * 将其转化为：
 * {"code":30001,"msg":"寄件日期参数不合法"}
 *
 * @author lxh
 * @since 2020-08-21
 */
public class MappingToJsonView extends MappingJackson2JsonView {

    private List<String> filterKeys;

    @Override
    protected Object filterModel(Map<String, Object> model) {
        if (filterKeys != null && filterKeys.size() > 0) {
            for (String filterKey : filterKeys) {
                Object object = model.get(filterKey);
                if (object != null) {
                    return object;
                }
            }
        }
        return super.filterModel(model);
    }

    public List<String> getFilterKeys() {
        return filterKeys;
    }

    public void setFilterKeys(List<String> filterKeys) {
        this.filterKeys = filterKeys;
    }
}
