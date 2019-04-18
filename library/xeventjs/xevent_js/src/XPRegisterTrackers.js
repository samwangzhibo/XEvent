var EventTracker = require('./XPTracker.js');
var stream = require('./XPStream');
var StashEventTracker = require('./XPStashTracker');

//var isTest = 1; ///测试时候打开，运行test.js文件
var isTest = 0;

//根据 dimension 注册一组 tracker
function registerTrackers(stream, dimension){
    var trackers = [];

    var xpanel_sw_tracker = new EventTracker("xpanel_sw");
    xpanel_sw_tracker.addDescriptor((event, fulfill, breaker)=>{
        if (event.getId() == "xpanel_sw") {
            fulfill(event);
        }
    }).addCompletion((event)=>{
        var jsonData = JSON.stringify(event);
        triggerReport("xpanel_sw", jsonData);
    });
    trackers.push(xpanel_sw_tracker);

    var pullup_tracker = new EventTracker("xpanel_pull_up");
    pullup_tracker.addDescriptor((event, fulfill, breaker)=>{
        if ((event.getId() == "xpanel_init" || event.getId() == "xpanel_scroll") && event.getEventAttr("scroll_distance") == 0) {
            fulfill(event);
        }
    }).addDescriptor((event, fulfill, breaker)=>{
        if (event.getId() == "xpanel_scroll" && event.getEventAttr("scroll_distance") > 0) {
            fulfill(event);
        }
    }).addCompletion((event)=>{
        var jsonData = JSON.stringify(event);
        triggerReport("xpanel_pull_up", jsonData);
    }).addResendEventCb((event, timestamp)=>{
            event.setTime(timestamp);
            event.setId("xpanel_pull_up");
            return event;
        }
    );
    trackers.push(pullup_tracker);

    var xpanel_sw_time_tracker = new StashEventTracker("keep_time");
    xpanel_sw_time_tracker.addDescriptor((event, fulfill, breaker)=>{
        if (event.getId() == "onresume") {
//          console.log("addDescriptor onresume");
            fulfill(event);
            xpanel_sw_time_tracker.putStashEvent("A", event);
        }
    }).addDescriptor((event, fulfill, breaker)=>{
        if (event.getId() == "onpause") {
            let duration = event.getTime() - xpanel_sw_time_tracker.getStashEvent("A").getTime();
            let toastStr = "页面停留时长， 耗时:" + duration/1000 + "秒";
//            console.log("addDescriptor onpause toastStr : " + toastStr + " event.attrsMap : " + event.attrsMap);
            event.putAttr("toast_str", toastStr);
            fulfill(event);
        }
    }).addCompletion((event)=>{
        var jsonData = JSON.stringify(event);
//        console.log("triggerReport jsonData: " + jsonData);
        triggerReport("sw_time", jsonData);
    }).addResendEventCb((event, timestamp)=>{
          event.setTime(timestamp);
          event.setId("toast");
//          console.log("addDescriptor event: " + event.getId());
          return event;
      }
    );
    trackers.push(xpanel_sw_time_tracker);
      
    //toast tracker
    var toast_tracker = new StashEventTracker("toast");
        toast_tracker.addDescriptor((event, fulfill, breaker)=>{
            if (event.getId() == "toast") {
                fulfill(event);
            }
        }).addCompletion((event)=>{
//            console.log("addCompletion : " + event.getAttr("toast_str"));
            toast(event.getAttr("toast_str"));
        });
    trackers.push(toast_tracker);
        

    var xpanel_half_reveal_sw_tracker = new EventTracker("xpanel_half_reveal_sw");
    xpanel_half_reveal_sw_tracker.addDescriptor((event, fulfill, breaker)=>{
        if (event.getId() == "xpanel_half_sw") {
            fulfill(event);
        }
    }).addCompletion((event)=>{
        var jsonData = JSON.stringify(event);
        triggerReport("xpanel_sw", jsonData);
    });
    trackers.push(xpanel_half_reveal_sw_tracker);

    var xpanel_card_ck_tracker = new EventTracker("xpanel_card_ck");
    xpanel_card_ck_tracker.addDescriptor((event, fulfill, breaker)=>{
        if (event.getId() == "card_ck") {
            fulfill(event);
        }
    }).addCompletion((event)=>{
         var jsonData = JSON.stringify(event);
         triggerReport("xpanel_card_ck", jsonData);
     });
    trackers.push(xpanel_card_ck_tracker);
    
    var xpanel_button_ck_tracker = new EventTracker("xpanel_button_ck");
    xpanel_button_ck_tracker.addDescriptor((event, fulfill, breaker)=>{
        if (event.getId() == "btn_ck") {
            fulfill(event);
        }
    }).addCompletion((event)=>{
        var jsonData = JSON.stringify(event);
        triggerReport("xpanel_button_ck", jsonData);
    });
    trackers.push(xpanel_button_ck_tracker);

    var xpanel_subcard_ck_tracker = new EventTracker("xpanel_subcard_ck");
    xpanel_subcard_ck_tracker.addDescriptor((event, fulfill, breaker)=>{
        if (event.getId() == "subcard_ck") {
            fulfill(event);
        }
    }).addCompletion((event)=>{
        var jsonData = JSON.stringify(event);
        triggerReport("xpanel_subcard_ck", jsonData);
    });
    trackers.push(xpanel_subcard_ck_tracker);

    var xpanel_try_pull_down_tracker = new EventTracker("try_pull_down");
    xpanel_try_pull_down_tracker.addDescriptor((event, fulfill, breaker)=>{
        if (event.getId() == "try_pull_down") {
            fulfill(event);
        }
    }).addCompletion((event)=>{
        var jsonData = JSON.stringify(event);
        triggerReport("xpanel_try_pull_down", jsonData);
    });
    trackers.push(xpanel_try_pull_down_tracker);

    var xpanel_net_succes_show_height_tracker = new EventTracker("try_pull_down");
    xpanel_net_succes_show_height_tracker.addDescriptor((event, fulfill, breaker)=>{
        if (event.getId() == "network_back" && event.get) {
            fulfill(event);
        }
    }).
    addDescriptor((event, fulfill, breaker)=>{
              if (event.getId() == "try_pull_down") {
                  fulfill(event);
              }
          })
     .addDescriptor((event, fulfill, breaker)=>{
                 if (event.getId() == "try_pull_down") {
                     fulfill(event);
                 }
             })
    .addCompletion((event)=>{
        var jsonData = JSON.stringify(event);
        triggerReport("xpanel_net_succes_show_height", jsonData);
    });
    trackers.push(xpanel_net_succes_show_height_tracker);

    // 注册整组 trackers
    stream.registerTrackers(trackers, dimension);
}

function registerSystemTracker(stream){
    var trackers = [];
    // VC init
    //var registerTracker  = new EventTracker("xevent_trackers_register");
    var registerTracker = new EventTracker("xevent_trackers_register");
    registerTracker.addDescriptor((event, fulfill, breaker)=>{
        if (event.getId() == "XEventSystem_VC_init") {
            fulfill(event);
        }
    }).addCompletion((event)=>{
       // 注册 trackers
       registerTrackers(stream, event.getDimension())
    });
    trackers.push(registerTracker);

    // VC dealloc
    var unregisterTracker  = new EventTracker("xevent_trackers_dealloc");
    unregisterTracker.addDescriptor((event, fulfill, breaker)=>{
        if (event.getId() == "XEventSystem_VC_dealloc") {
            fulfill(event);
        }
    }).addCompletion((event)=>{
       // 销毁 trackers
       stream.unregisterTracker(event.getDimension())
    });
    trackers.push(unregisterTracker);

    // 同一个 VC ，场景(dimension)切换
    var dimensionChangeTracker  = new EventTracker("xevent_dimension_change");
    dimensionChangeTracker.addDescriptor((event, fulfill, breaker)=>{
        if (event.getId() == "XEventSystem_dimension_change") {
            fulfill(event);
        }
    }).addCompletion((event)=>{
       registerTrackers(stream, event.getDimension())
    });
    trackers.push(dimensionChangeTracker);

    // 切换业务线至未被销毁的 VC
    var pushVCTracker  = new EventTracker("xevent_push_VC");
    pushVCTracker.addDescriptor((event, fulfill, breaker)=>{
        if (event.getId() == "XEventSystem_push_VC") {
            fulfill(event);
        }
    }).addCompletion((event)=>{
       stream.changeDimensionAndResetTrackers(event.getDimension());
    });
    trackers.push(pushVCTracker);

    // 注册 system trackers
    stream.registerTrackers(trackers, "XEventSystem");
}

module.exports = {
    registerTrackers:registerTrackers,
    registerSystemTracker:registerSystemTracker,
};

function triggerReport(log, jsonData) {
    if (isTest == 1) {
        console.log(log);
    } else {
        xpEventManager.triggerReport(log, jsonData);
    }
}

function toast(str) {
  if (isTest == 1) {
    console.log(str);
  } else {
    xpEventManager.toast(str);
  }
}
