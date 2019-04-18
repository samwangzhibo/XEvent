package com.wzb.xevent.util;

/**
 * 判断环的校验器
 * Created by samwangzhibo on 2018/12/28.
 */

public class CycleExamine {
    long enterTime = 0L;
    boolean isEnterCycle = false;
    public final int DURATION_TIMEOUT = 2000;

    public void enterCycle(){
        if (isEnterCycle){
            if (System.currentTimeMillis() - enterTime > DURATION_TIMEOUT){
                throw new RuntimeException("递归超时了，配置文件出现了环");
            }
        }else {
            enterTime = System.currentTimeMillis();
        }
    }

    public void exitCycle(){
        isEnterCycle = false;
    }
}
