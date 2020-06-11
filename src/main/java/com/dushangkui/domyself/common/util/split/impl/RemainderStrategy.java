package com.dushangkui.domyself.common.util.split.impl;

import com.dushangkui.domyself.common.util.JsonUtil;
import com.dushangkui.domyself.common.util.split.Strategy;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.Map;

/**
 * 只支持Map和Object对象
 */
public class RemainderStrategy implements Strategy {
    private Log log = LogFactory.getLog(RemainderStrategy.class);

    private static final int DIVIDER = 16;

    @Override
    public String convert(Map<String, Object> params) throws Exception {
        StringBuilder sb = new StringBuilder(params.get(Strategy.TABLE_NAME).toString());
        sb.append("_");
        String field = JsonUtil.getString(params, Strategy.SPLIT_FIELD);
        List<Map<String, Object>> paramDeclare = JsonUtil.toList(params.get(Strategy.EXECUTE_PARAM_DECLARE));
        Object paramValue = params.get(Strategy.EXECUTE_PARAM_VALUES);
        String value = "";
        if (paramValue instanceof Map) {
            if (((Map) paramValue).size() == 1) {
                if (((Map) paramValue).containsKey("list")) {
                    paramValue = ((List) ((Map) paramValue).get("list")).get(0);
                } else if (((Map) paramValue).containsKey("array")) {
                    paramValue = ((Object[]) ((Map) paramValue).get("list"))[0];
                }
                value = BeanUtils.getProperty(paramValue, field);
            } else {
                value = JsonUtil.getString((Map<String, Object>) paramValue, field);
            }

        } else {
            value = BeanUtils.getProperty(paramValue, field);
        }
        Integer reminder = Integer.valueOf(value) % DIVIDER;
        sb.append(Integer.toHexString(reminder));
        return sb.toString();
    }

}
