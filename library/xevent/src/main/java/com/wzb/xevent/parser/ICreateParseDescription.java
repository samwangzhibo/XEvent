package com.wzb.xevent.parser;

import com.wzb.xevent.description.SimpleJEXLConditionDescription;

/**
 * 给外部使用，让他传入自定义识别DSL的{@link com.wzb.xevent.description.XPDescription}
 * Created by samwangzhibo on 2018/9/14.
 */

public interface ICreateParseDescription {
    /**
     * 创建识别DSL的描述
     * @return
     */
    SimpleJEXLConditionDescription createJEXLConditionDescription();
}
