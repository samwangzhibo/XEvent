package com.wzb.xevent.parser;

import android.text.TextUtils;
import android.util.Log;

import com.wzb.xevent.description.SimpleJEXLConditionDescription;
import com.wzb.xevent.description.XPDescription;
import com.wzb.xevent.tracker.SimpleXPTracker;
import com.wzb.xevent.util.FormatUtil;
import com.wzb.xevent.util.LogcatUtil;

import org.apache.commons.jexl3.JexlExpression;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * 配置解析器 把xml配置解析成Tracker列表
 * Created by samwangzhibo on 2018/8/14.
 */

public class ConfigParser {
    private static final String TAG = "ConfigParser";
    //节点名
    public static final String ELEMENT_ROOT = "trackers";
    public static final String ELEMENT_TRACKER = "tracker";
    public static final String ELEMENT_DESCRIPTION = "description";
    //属性名
    public static final String ATTR_TYPE = "type"; //tracker的类型 break终止条件类型、打点类型
    public static final String ATTR_GROUP = "group"; //tracker分组 用于动态创建tracker实例
    public static final String ATTR_DEFAULT_INDEX = "default_index"; // tracker状态机的初始条件索引 1的话 为第二个description
    public static final String ATTR_RESEND_EVENT = "resend_event"; //tracker满足description条件后发送的事件名称
    public static final String ATTR_EVENT_NAME = "log_name"; //tracker满足所有description后的打点名称
    public static final String ATTR_EVENT_END_INDEX = "event_end_index"; //tracker满足所有description后的打点结束后需要重置到的位置
    public static final String ATTR_DEBUG_MODE = "debug";

    public static final String ATTR_ID = "id"; //描述命中的事件id
    public static final String ATTR_CONDITION = "condition"; //描述满足的条件
    public static final String ATTR_TIME = "time"; //时间表达式 用于时间筛选条件
    public static final String ATTR_DESCRIPTION_ALIAS = "alias"; //description的别名，用于其他description通过别名取值
    public static final String ATTR_PUT_VALUE = "put_value"; //满足条件后需要打点的K V json字符串
    public static final String ATTR_BREAK_TIME = "break_time"; //终止时间条件 满足就终止tracker打点
    public static final String ATTR_BREAK_CONDITION = "break_condition"; //终止条件 满足就终止tracker打点

    private ICreateParseDescription iCreateParseDescription;
    private DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
//    XPCodnditionParser conditionParser = new XPConditionParser();

    enum Type {
        Break("break"),;

        String type;

        Type(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public void setICreateParseDescription(ICreateParseDescription iCreateParseDescription) {
        this.iCreateParseDescription = iCreateParseDescription;
    }


    /**
     * 通过配置构建Trackers
     *
     * @param config 配置的字符串
     * @return
     */
    public List<SimpleXPTracker> parseTrakersByConfig(String config) {
        ArrayList<SimpleXPTracker> trackers = new ArrayList<>();
        if (TextUtils.isEmpty(config)) {
            return trackers;
        }

        //数据预处理 首先转义化配置文件
        config = transferConfig(config);
        Document document = parse(config);
        if (document == null) {
            return trackers;
        }
//        get root element
        Element rootElement = document.getDocumentElement();
        if (!TextUtils.equals(rootElement.getTagName(), ELEMENT_ROOT)) {
            LogcatUtil.e(TAG, "element is not " + ELEMENT_ROOT);
            return trackers;
        }

        //traverse child elements
        NodeList nodes = rootElement.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element trackerElement = (Element) node;
                if (!TextUtils.equals(trackerElement.getTagName(), ELEMENT_TRACKER)) {
                    Log.e(TAG, "element is not " + ELEMENT_TRACKER);
                }
                //process child element
                SimpleXPTracker tracker = processTrackerElement(trackerElement);
                if (tracker != null) {
                    trackers.add(tracker);
                }
            }
        }
        return trackers;
    }

    /**
     * 数据预处理 转义配置文件中的特殊符
     */
    private String transferConfig(String config) {
        String TEMP_STR = "&ltr;";
        String XML_HEADER = "<?xml";
        String XML_HEADER_REPLACE = "&ltr;?xml";
        String TRACKERS_HEADER = "<trackers";
        String TRACKERS_HEADER_REPLACER = "&ltr;trackers";
        String TRACKER_HEADER = "<tracker";
        String TRACKER_HEADER_REPLACER = "&ltr;tracker";
        String DESCRIPTION_HEADER = "<description";
        String DESCRIPTION_REPLACER = "&ltr;description";
        String XML_ANNOTATION = "<!--";
        String XML_ANNOTATION_REPLACER = "&ltr;!--";
        String result = config;
//        config = config.replace("\n", "");
        // 创建一个正则表达式模式，用以匹配一个单词（\b=单词边界）
        // & lt;   <    小于号
        // & gt;    >    大于号
        // & amp;    &   和
        // & apos;   '   单引号
        // & quot;  "   双引号
        String patt = "</.+>";

        //从正则表达式实例中运行方法并查看其如何运行
        Pattern r = Pattern.compile(patt);
        Matcher m = r.matcher(config);
        while (m.find()) {
            String str = m.group();
            String lessSymbolFix = TEMP_STR + str.substring(1);
            result = result.replace(str, lessSymbolFix);
        }

        result = result
                .replace(XML_HEADER, XML_HEADER_REPLACE)
                .replace(TRACKERS_HEADER, TRACKERS_HEADER_REPLACER)
                .replace(TRACKER_HEADER, TRACKER_HEADER_REPLACER)
                .replace(DESCRIPTION_HEADER, DESCRIPTION_REPLACER)
                .replace(XML_ANNOTATION, XML_ANNOTATION_REPLACER)
                .replace("<", "&lt;")
                .replace(TEMP_STR, "<");
        return result;
    }

    /**
     * 解析Tracker节点 生成一个Tracker对象
     *
     * @param trackerElement dom树里面的Tracker节点
     * @return
     */
    private SimpleXPTracker processTrackerElement(Element trackerElement) {
        SimpleXPTracker xpEventTracker = new SimpleXPTracker();
        ArrayList<XPDescription> descriptions = new ArrayList<>(); //条件满足的描述
        ArrayList<XPDescription> breakDescriptions = new ArrayList<>(); //break条件描述
        List<Element> breakElements = new ArrayList<>();

        parseResendEvent(trackerElement, xpEventTracker); //重新抛掷事件
        parseEventName(trackerElement, xpEventTracker); //打点名
        parseEventEndIndex(trackerElement, xpEventTracker);
        xpEventTracker.setDebugSwitch(parseDebug(trackerElement));
        parseDefaultStatus(trackerElement, xpEventTracker); //初始态description索引
//        parseType(trackerElement, xpEventTracker);
        parseGroup(trackerElement, xpEventTracker); //解析group

        NodeList nodes = trackerElement.getChildNodes();
        //处理tracker下面的descriptions和break
        /**
         *  <traker resend_event="xp_toast">
         <description alias="AA" id="xpanel_keep" />
         <traker type="break">
         <description id="xpanel_pause" />
         </traker>
         </traker>
         *
         */
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) node;
                if (TextUtils.equals(childElement.getTagName(), ELEMENT_DESCRIPTION)) { //description
                    String condition = childElement.getAttribute(ATTR_CONDITION);
                    String id = childElement.getAttribute(ATTR_ID);
                    //描述没有意义
                    if (TextUtils.isEmpty(condition) && TextUtils.isEmpty(id)) {
                        continue;
                    }
                    SimpleJEXLConditionDescription description = iCreateParseDescription != null ?
                            iCreateParseDescription.createJEXLConditionDescription() :
                            new SimpleJEXLConditionDescription();
                    String timeExpression = childElement.getAttribute(ATTR_TIME);
                    if (!TextUtils.isEmpty(timeExpression)) {
                        description.setTimeExpression(timeExpression);
                    }
                    if (!parseEventId(description, id)) return null;
                    parseAlias(childElement, description);
                    if (!parseCondition(description, condition)) return null;
                    if (!parsePutValue(description, childElement)) return null;
                    parseBreakCondition(childElement, description);
                    parseBreakTime(childElement, description);
                    description.setDebugSwitch(parseDebug(childElement));
                    String type = childElement.getAttribute(ATTR_TYPE);
                    //如果是type类型的description
                    if ("break".equals(type)) {
//                    if (Type.Break.getType().equals(type)) {
                        breakDescriptions.add(description);
                    } else {
                        descriptions.add(description);
                    }
                } else if (TextUtils.equals(childElement.getTagName(), ELEMENT_TRACKER)) {
                    breakElements.add(childElement);
                }
            }
        }
        if (descriptions.size() == 0) {
            return null;
        }
        xpEventTracker.setDescriptions(descriptions);
        //校验break条件的配置是否合理
        if (!checkBreakConfigValidated(breakElements)) {
            return null;
        }
        if (breakElements.size() >= 1) {
            SimpleXPTracker breakTracker = processTrackerElement(breakElements.get(0));
            if (breakTracker != null) {
                xpEventTracker.setBreakEventTracker(breakTracker);
            }
        } else if (breakDescriptions.size() != 0) { //break条件 Tracker优先级高于description
            //构建breakTracker
            xpEventTracker.setBreakEventTracker(new SimpleXPTracker().setDescriptions(breakDescriptions));
        }
        return xpEventTracker;
    }

    private boolean parseDebug(Element childElement) {
        String debugStr = childElement.getAttribute(ATTR_DEBUG_MODE);
        return TextUtils.equals("true", debugStr);
    }

    private void parseEventEndIndex(Element childElement, SimpleXPTracker tracker) {
        String eventEndIndex = childElement.getAttribute(ATTR_EVENT_END_INDEX);
        int defaultIndex = FormatUtil.getIntFromString(eventEndIndex);
        if (defaultIndex > -1) {
            tracker.setEventEndIndex(defaultIndex);
        }
    }

    /**
     * 解析条件 如果解析失败 就抛弃这个tracker
     *
     * @param description
     * @param condition   条件字符串
     * @return 是否解析成功
     */
    boolean parseCondition(SimpleJEXLConditionDescription description, String condition) {
        if (TextUtils.isEmpty(condition)) { //condition可以不传
            return true;
        }
//        XPLogicalExpression xpLogicalExpression = buildRootLogicalExpression(condition);
//        if (xpLogicalExpression == null) return false;
        //如果传了condition就必须要检查condition是否正确
        JexlExpression jexlExpression = JexlExpressionParser.getInstance().parseExpression(condition);
        if (jexlExpression == null){
            return false;
        }
        description.setExpression(jexlExpression);
        return true;
    }

    private void parseGroup(Element trackerElement, SimpleXPTracker xpEventTracker) {
        String group = trackerElement.getAttribute(ATTR_GROUP);
        if (!TextUtils.isEmpty(group)) {
            xpEventTracker.setGroup(group);
        }
    }

//    private void parseType(Element trackerElement, SimpleXPTracker xpEventTracker) {
//        String type = trackerElement.getAttribute(ATTR_TYPE);
//        if (!TextUtils.isEmpty(type)){
//            xpEventTracker.setType(type);
//        }
//    }

    private void parseDefaultStatus(Element childElement, SimpleXPTracker tracker) {
        String defaultIndexStr = childElement.getAttribute(ATTR_DEFAULT_INDEX);
        int defaultIndex = FormatUtil.getIntFromString(defaultIndexStr);
        if (defaultIndex > -1) {
            tracker.setDefaultIndex(defaultIndex);
        }
    }

    private void parseBreakTime(Element childElement, SimpleJEXLConditionDescription description) {
        String breakTime = childElement.getAttribute(ATTR_BREAK_TIME);
        if (TextUtils.isEmpty(breakTime)) {
            return;
        }
        description.setBreakTimeExpression(breakTime);
//        description.setBreakDescription(new SimpleTimeDescription().setTimeExpression(breakTime).setXPEventId(description.xPEventId));
    }

    private void parseBreakCondition(Element childElement, SimpleJEXLConditionDescription description) {
        String breakCondition = childElement.getAttribute(ATTR_BREAK_CONDITION);
        if (TextUtils.isEmpty(breakCondition)) {
            return;
        }
//        description.setBreakDescription(new SimpleJEXLConditionDescription()
//                .setExpression(JexlExpressionParser.getInstance().parseExpression(breakCondition))
//                .setXPEventId(description.xPEventId));
        description.setBreakExpression(JexlExpressionParser.getInstance().parseExpression(breakCondition));
    }

    private void parseEventName(Element trackerElement, SimpleXPTracker xpEventTracker) {
        String eventName = trackerElement.getAttribute(ATTR_EVENT_NAME);
        if (!TextUtils.isEmpty(eventName)) {
            xpEventTracker.setEventName(eventName);
        }
    }

    private void parseResendEvent(Element trackerElement, SimpleXPTracker xpEventTracker) {
        String resendEvent = trackerElement.getAttribute(ATTR_RESEND_EVENT);
        if (!TextUtils.isEmpty(resendEvent)) {
            xpEventTracker.setReSendXPEventId(resendEvent);
        }
    }

    /**
     * 检查break是否合理，每个Tracker最多一个Break Tracker
     *
     * @param breakElements
     * @return
     */
    private boolean checkBreakConfigValidated(List<Element> breakElements) {
        return breakElements.size() <= 1; //最多一个break
    }

    private void parseAlias(Element childElement, SimpleJEXLConditionDescription description) {
        String alias = childElement.getAttribute(ATTR_DESCRIPTION_ALIAS);
        if (TextUtils.isEmpty(alias)) {
            return;
        }
        description.setAlias(alias);
    }

    /**
     * 解析{@link #ATTR_PUT_VALUE}
     *
     * @param description
     * @param childElement
     * @return
     */
    private boolean parsePutValue(SimpleJEXLConditionDescription description, Element childElement) {
        String putVaule = childElement.getAttribute(ATTR_PUT_VALUE);
        /**
         * <description alias="C" id="subcard_ck" condition="subcard_id == 'more'"
         put_value="{'toast_str':'操作是点击会员，横划套餐，点击更多套餐， 耗时:' + (xp_time - A.xp_time)/1000 + '秒'}"/>

         {'toast_str':'操作是点击会员，横划套餐，点击更多套餐， 耗时:' + (xp_time - A.xp_time)/1000 + '秒', 'a': attrA}
         */
        if (!TextUtils.isEmpty(putVaule)) {
            //把' 替换成 "  成为json字符串
//            String putVauleJsonStr = putVaule.replace("'", "\"");
            String patt = "' *:";
            String putVauleJsonStr = putVaule.replaceAll(patt, "'\" : \"");
            /**
             * {'toast_str'":"'操作是点击会员，横划套餐，点击更多套餐， 耗时:' + (xp_time - A.xp_time)/1000 + '秒', 'a'": "attrA}
             */
            putVauleJsonStr = putVauleJsonStr.replaceAll(", *'", "\", \"'");
            /**
             * {'toast_str'":"'操作是点击会员，横划套餐，点击更多套餐， 耗时:' + (xp_time - A.xp_time)/1000 + '秒'", "'a'": "attrA}
             */
            putVauleJsonStr = "{\"" + putVauleJsonStr.substring(1, putVauleJsonStr.length() - 1) + "\"}";
            /**
             * {"'toast_str'":"'操作是点击会员，横划套餐，点击更多套餐， 耗时:' + (xp_time - A.xp_time)/1000 + '秒'", "'a'": "attrA"}
             */
            try {
                JSONObject jsonObject = new JSONObject(putVauleJsonStr);
                HashMap<String, JexlExpression> attrs = new HashMap<>();
                Iterator<String> it = jsonObject.keys();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = jsonObject.getString(key);
                    String realKey = key.replace("'", "");
                    LogcatUtil.e(TAG, "解析description put key = " + realKey + ",value = " + value);
                    if (!TextUtils.isEmpty(value)) {
                        attrs.put(realKey, JexlExpressionParser.getInstance().parseExpression(value));
                    }
                }
                if (attrs.size() == 0) return true;
                description.setTempAttrsExpression(attrs); //展示放到description的缓存区
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * @return 解析是否成功
     */
    private boolean parseEventId(SimpleJEXLConditionDescription description, String ids) {
//        try {
        if (TextUtils.isEmpty(ids)) {
            return true;
        }
        ArrayList<String> idArrays = new ArrayList<>();
        for (String id : ids.split("\\|")){
            idArrays.add(id.trim());
        }
        description.setXPEventIds(idArrays);
//        } catch (NumberFormatException e) {
//            return false;
//        }
        return true;
    }

    /**
     * 通过配置生成document树
     *
     * @param config trackers配置
     * @return
     */
    public Document parse(String config) {
        Document document = null;
        //java.lang.AssertionError
        try {
            //DOM parser instance
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            //parse an XML file into a DOM tree
            document = builder.parse(new InputSource(new StringReader(config)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }

    public static ConfigParser getInstance() {
        return InnerClass.instance;
    }

    public static class InnerClass {
        public static ConfigParser instance = new ConfigParser();

        private InnerClass() {

        }
    }
}
