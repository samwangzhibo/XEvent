/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
/******/ 		}
/******/ 	};
/******/
/******/ 	// define __esModule on exports
/******/ 	__webpack_require__.r = function(exports) {
/******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 		}
/******/ 		Object.defineProperty(exports, '__esModule', { value: true });
/******/ 	};
/******/
/******/ 	// create a fake namespace object
/******/ 	// mode & 1: value is a module id, require it
/******/ 	// mode & 2: merge all properties of value into the ns
/******/ 	// mode & 4: return value when already ns object
/******/ 	// mode & 8|1: behave like require
/******/ 	__webpack_require__.t = function(value, mode) {
/******/ 		if(mode & 1) value = __webpack_require__(value);
/******/ 		if(mode & 8) return value;
/******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
/******/ 		var ns = Object.create(null);
/******/ 		__webpack_require__.r(ns);
/******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
/******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
/******/ 		return ns;
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = "./src/index.js");
/******/ })
/************************************************************************/
/******/ ({

/***/ "./node_modules/webpack/buildin/global.js":
/*!***********************************!*\
  !*** (webpack)/buildin/global.js ***!
  \***********************************/
/*! no static exports found */
/***/ (function(module, exports) {

var g;

// This works in non-strict mode
g = (function() {
	return this;
})();

try {
	// This works if eval is allowed (see CSP)
	g = g || new Function("return this")();
} catch (e) {
	// This works if the window reference is available
	if (typeof window === "object") g = window;
}

// g can still be undefined, but nothing to do about it...
// We return undefined, instead of nothing here, so it's
// easier to handle this case. if(!global) { ...}

module.exports = g;


/***/ }),

/***/ "./src/XPEvent.js":
/*!************************!*\
  !*** ./src/XPEvent.js ***!
  \************************/
/*! no static exports found */
/***/ (function(module, exports) {

class Event {
    constructor(dimension, xpEventId, time, attrsMap, eventAttrsMap) {
        this.dimension = dimension;
        this.xpEventId = xpEventId;
        this.time = time;
        this.attrsMap = attrsMap;
        this.eventAttrsMap = eventAttrsMap;
    }

    getId() {
    	return this.xpEventId;
    }

    setId(xpEventId){
        this.xpEventId = xpEventId;
    }

    getTime(){
        return this.time;
    }

    setTime(time) {
        this.time = time;
    }

    getEventAttr(key) {
    	return this.eventAttrsMap[key];
    }

    //放置不需要打点的K V  只记录在Event中
    putEventAttr(key, value){
        this.eventAttrsMap[key] = value;
    }

    getAttr(key) {
    	return this.attrsMap[key];
    }

    //放置需要打点的K V
    putAttr(key, value){
        this.attrsMap[key] = value;
    }

    getDimension() {
        return this.dimension;
    }

    setDimension(dimension){
        this.dimension = dimension;
    }

}

module.exports = Event;


/***/ }),

/***/ "./src/XPEventManager.js":
/*!*******************************!*\
  !*** ./src/XPEventManager.js ***!
  \*******************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

/* WEBPACK VAR INJECTION */(function(global) {var initStream = __webpack_require__(/*! ./XPStream */ "./src/XPStream.js");
var Event = __webpack_require__(/*! ./XPEvent.js */ "./src/XPEvent.js");
var initTrackers = __webpack_require__(/*! ./XPRegisterTrackers */ "./src/XPRegisterTrackers.js");

var stream = new initStream();
var ostype = 1;
initTrackers.registerSystemTracker(stream);
function sendEvent(event) {
    var objs;
    if(ostype == 0){
        objs= eval(event);
    }else{
        objs= JSON.parse(event);
    }
    var innerEvent = new Event(objs.dimension, objs.xpEventId, objs.time, objs.attrsMap, objs.eventAttrsMap);
    stream.receiveEvent(innerEvent)
}

//区分os
function init(osType){
    ostype = osType;
}

module.exports = {
    newStream: stream,
    sendEvent: sendEvent
};
global.sendEvent = sendEvent;
global.init = init;


/* WEBPACK VAR INJECTION */}.call(this, __webpack_require__(/*! ./../node_modules/webpack/buildin/global.js */ "./node_modules/webpack/buildin/global.js")))

/***/ }),

/***/ "./src/XPRegisterTrackers.js":
/*!***********************************!*\
  !*** ./src/XPRegisterTrackers.js ***!
  \***********************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

var EventTracker = __webpack_require__(/*! ./XPTracker.js */ "./src/XPTracker.js");
var stream = __webpack_require__(/*! ./XPStream */ "./src/XPStream.js");
var StashEventTracker = __webpack_require__(/*! ./XPStashTracker */ "./src/XPStashTracker.js");

var isTest = 1;

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
            fulfill(event);
            xpanel_sw_time_tracker.putStashEvent("A", event);
        }
    }).addDescriptor((event, fulfill, breaker)=>{
        if (event.getId() == "onpause") {
            let duration = event.getTime() - xpanel_sw_time_tracker.getStashEvent("A").getTime();
            event.putAttr("toast_str", "页面停留时长， 耗时:" + duration/1000 + "秒");
            fulfill(event);
        }
    }).addCompletion((event)=>{
        var jsonData = JSON.stringify(event);
        triggerReport("xpanel_sw_time", jsonData);
    }).addResendEventCb((event, timestamp)=>{
          event.setTime(timestamp);
          event.setId("toast");
          return event;
      }
    );
    trackers.push(xpanel_sw_time_tracker);

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

/***/ }),

/***/ "./src/XPStashTracker.js":
/*!*******************************!*\
  !*** ./src/XPStashTracker.js ***!
  \*******************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

var EventTracker = __webpack_require__(/*! ./XPTracker.js */ "./src/XPTracker.js");
class StashEventTracker extends EventTracker{
    constructor(name) {
        super(name);
        this.stashEventMap = [];
    }

    putStashEvent(alias, event){
        this.stashEventMap[alias] = event;
    }

    getStashEvent(alias){
        return this.stashEventMap[alias];
    }

    receiveEvent(event) {
        let descriptor = this.descriptions[this.index];
        if(!descriptor){
            return null;
        }
        var self = this;
        let fulfill = function(event) {
            self.index++;
        };
        let breaker = (event) => {
            self.index = 0;
        };

        descriptor(event, fulfill, breaker);
        
        if (this.index >= this.descriptions.length) {
            if (this.completion) {
                this.completion(event);
            }
            this.index = 0;

            //满足条件之后看有没有需要回传的事件
            let resendEvent = this.onResendEvent(event);
//                console.log(resendEvent.getId());
            return resendEvent;
        }
        return null;
    }
}

module.exports = StashEventTracker;


/***/ }),

/***/ "./src/XPStream.js":
/*!*************************!*\
  !*** ./src/XPStream.js ***!
  \*************************/
/*! no static exports found */
/***/ (function(module, exports) {

class EventStream {
    constructor() {
        // K 维度  V 容器(K     V   )
        this.streams = [];
        // 记录当前维度
        this.currentDimension = null;
    }

    receiveEvent(event) {
        if (event.getId().indexOf("XEventSystem") == 0) {
            let stream = this.streams["XEventSystem"];
            let systemTrackers = stream["trackers"];
            for (let tracker of systemTrackers) {
                tracker.receiveEvent(event);
            }
            return;
        }

        //tracker回传的事件
        var bufferEvents = new Array()
        let currentStream = this.streams[this.currentDimension];
        let currentTrackers = currentStream["trackers"];
        for (let tracker of currentTrackers) {
            let reSendEvent = tracker.receiveEvent(event);
            if(reSendEvent){
                console.log("buffer event : " + reSendEvent.getId());
                bufferEvents.push(reSendEvent);
            }
        }

        for (let bufferEvent of bufferEvents)
        {
            this.receiveEvent(bufferEvent);
        }
    }
    
    registerTrackers(trackers, dimension) { 
        this.currentDimension = dimension;
        // 存放当前维度所需要的信息及 trackers
        var trackerGroup = [];
        trackerGroup["trackers"] = trackers;
        this.streams[dimension] = trackerGroup;
    }
    
    unregisterTracker(dimension){
        this.currentDimension = null;
        if (this.streams[dimension]) {
            delete this.streams[dimension];
        }
    }

    changeDimensionAndResetTrackers(dimension) {
        this.currentDimension = dimension;
        let currentStream = this.streams[dimension];
        let currentTrackers = currentStream["trackers"];
        for (let tracker of currentTrackers) {
            tracker.reset();
        }
    }

}

module.exports = EventStream;


/***/ }),

/***/ "./src/XPTracker.js":
/*!**************************!*\
  !*** ./src/XPTracker.js ***!
  \**************************/
/*! no static exports found */
/***/ (function(module, exports) {

class EventTracker {
    constructor(name) {
        this.name = name;
        this.index = 0;
        this.descriptions = [];
        this.completion = null;
        this.breaker = null;
        this.resendEventCb = null;
    }
    addDescriptor(descriptor) {
        this.descriptions.push(descriptor);
        return this;
    }
    addBreaker(breaker) {
        this.breaker = breaker;
        return this;
    }
    addCompletion(completion) {
        this.completion = completion;
        return this;
    }
    addResendEventCb(resendEventCb){
        this.resendEventCb = resendEventCb;
        return this;
    }

    reset() {
        this.index = 0;
    }

    onResendEvent(event){
        //构建新事件的时间戳
        var timestamp = Date.parse(new Date());
        if(this.resendEventCb){
            let reSentEvent = this.resendEventCb(event, timestamp);
            return reSentEvent;
        }
        return null;
    }

    receiveEvent(event) {
        let descriptor = this.descriptions[this.index];
        if(!descriptor){
            return null;
        }
        var self = this;
        let fulfill = function(event) {
            self.index++;
        };
        let breaker = (event) => {
            self.index = 0;
        };

//        console.log(descriptor);
        descriptor(event, fulfill, breaker);
        
        if (this.index >= this.descriptions.length) {
            if (this.completion) {
                this.completion(event);
            }
            this.index = 0;
            //满足条件之后看有没有需要回传的事件
            let resendEvent = this.onResendEvent(event);
            return resendEvent;
        }
        return null;
    }
}

module.exports = EventTracker;


/***/ }),

/***/ "./src/index.js":
/*!**********************!*\
  !*** ./src/index.js ***!
  \**********************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

__webpack_require__(/*! ./XPEventManager.js */ "./src/XPEventManager.js");


/***/ })

/******/ });
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIndlYnBhY2s6Ly8vd2VicGFjay9ib290c3RyYXAiLCJ3ZWJwYWNrOi8vLyh3ZWJwYWNrKS9idWlsZGluL2dsb2JhbC5qcyIsIndlYnBhY2s6Ly8vLi9zcmMvWFBFdmVudC5qcyIsIndlYnBhY2s6Ly8vLi9zcmMvWFBFdmVudE1hbmFnZXIuanMiLCJ3ZWJwYWNrOi8vLy4vc3JjL1hQUmVnaXN0ZXJUcmFja2Vycy5qcyIsIndlYnBhY2s6Ly8vLi9zcmMvWFBTdGFzaFRyYWNrZXIuanMiLCJ3ZWJwYWNrOi8vLy4vc3JjL1hQU3RyZWFtLmpzIiwid2VicGFjazovLy8uL3NyYy9YUFRyYWNrZXIuanMiLCJ3ZWJwYWNrOi8vLy4vc3JjL2luZGV4LmpzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7QUFBQTtBQUNBOztBQUVBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTs7QUFFQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTs7O0FBR0E7QUFDQTs7QUFFQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTtBQUNBLGtEQUEwQyxnQ0FBZ0M7QUFDMUU7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7QUFDQSxnRUFBd0Qsa0JBQWtCO0FBQzFFO0FBQ0EseURBQWlELGNBQWM7QUFDL0Q7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLGlEQUF5QyxpQ0FBaUM7QUFDMUUsd0hBQWdILG1CQUFtQixFQUFFO0FBQ3JJO0FBQ0E7O0FBRUE7QUFDQTtBQUNBO0FBQ0EsbUNBQTJCLDBCQUEwQixFQUFFO0FBQ3ZELHlDQUFpQyxlQUFlO0FBQ2hEO0FBQ0E7QUFDQTs7QUFFQTtBQUNBLDhEQUFzRCwrREFBK0Q7O0FBRXJIO0FBQ0E7OztBQUdBO0FBQ0E7Ozs7Ozs7Ozs7OztBQ2xGQTs7QUFFQTtBQUNBO0FBQ0E7QUFDQSxDQUFDOztBQUVEO0FBQ0E7QUFDQTtBQUNBLENBQUM7QUFDRDtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBLDRDQUE0Qzs7QUFFNUM7Ozs7Ozs7Ozs7OztBQ25CQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7O0FBRUE7O0FBRUE7Ozs7Ozs7Ozs7OztBQ3JEQSwrREFBaUIsbUJBQU8sQ0FBQyxxQ0FBWTtBQUNyQyxZQUFZLG1CQUFPLENBQUMsc0NBQWM7QUFDbEMsbUJBQW1CLG1CQUFPLENBQUMseURBQXNCOztBQUVqRDtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLEtBQUs7QUFDTDtBQUNBO0FBQ0E7QUFDQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTs7Ozs7Ozs7Ozs7Ozs7QUM1QkEsbUJBQW1CLG1CQUFPLENBQUMsMENBQWdCO0FBQzNDLGFBQWEsbUJBQU8sQ0FBQyxxQ0FBWTtBQUNqQyx3QkFBd0IsbUJBQU8sQ0FBQyxpREFBa0I7O0FBRWxEOztBQUVBO0FBQ0E7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0EsS0FBSztBQUNMO0FBQ0E7QUFDQSxLQUFLO0FBQ0w7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLEtBQUs7QUFDTDtBQUNBO0FBQ0E7QUFDQSxLQUFLO0FBQ0w7QUFDQTtBQUNBLEtBQUs7QUFDTDtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0EsS0FBSztBQUNMO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSxLQUFLO0FBQ0w7QUFDQTtBQUNBLEtBQUs7QUFDTDtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLEtBQUs7QUFDTDtBQUNBO0FBQ0EsS0FBSztBQUNMOztBQUVBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSxLQUFLO0FBQ0w7QUFDQTtBQUNBLE1BQU07QUFDTjs7QUFFQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0EsS0FBSztBQUNMO0FBQ0E7QUFDQSxLQUFLO0FBQ0w7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLEtBQUs7QUFDTDtBQUNBO0FBQ0EsS0FBSztBQUNMOztBQUVBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSxLQUFLO0FBQ0w7QUFDQTtBQUNBLEtBQUs7QUFDTDs7QUFFQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0EsS0FBSztBQUNMO0FBQ0E7QUFDQTtBQUNBO0FBQ0EsV0FBVztBQUNYO0FBQ0E7QUFDQTtBQUNBO0FBQ0EsY0FBYztBQUNkO0FBQ0E7QUFDQTtBQUNBLEtBQUs7QUFDTDs7QUFFQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0EsS0FBSztBQUNMO0FBQ0E7QUFDQSxLQUFLO0FBQ0w7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0EsS0FBSztBQUNMO0FBQ0E7QUFDQSxLQUFLO0FBQ0w7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0EsS0FBSztBQUNMO0FBQ0EsS0FBSztBQUNMOztBQUVBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLEtBQUs7QUFDTDtBQUNBLEtBQUs7QUFDTDs7QUFFQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBO0FBQ0EsS0FBSztBQUNMO0FBQ0E7QUFDQSxDOzs7Ozs7Ozs7OztBQ2pOQSxtQkFBbUIsbUJBQU8sQ0FBQywwQ0FBZ0I7QUFDM0M7QUFDQTtBQUNBO0FBQ0E7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTs7QUFFQTs7QUFFQTtBQUNBO0FBQ0E7QUFDQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7Ozs7Ozs7Ozs7OztBQzdDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7QUFDQTtBQUNBOztBQUVBLDJDO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBOztBQUVBOztBQUVBOzs7Ozs7Ozs7Ozs7QUM5REE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7Ozs7Ozs7Ozs7OztBQ3JFQSxtQkFBTyxDQUFDLG9EQUFxQiIsImZpbGUiOiJsaWJ4ZXZlbnQuanMiLCJzb3VyY2VzQ29udGVudCI6WyIgXHQvLyBUaGUgbW9kdWxlIGNhY2hlXG4gXHR2YXIgaW5zdGFsbGVkTW9kdWxlcyA9IHt9O1xuXG4gXHQvLyBUaGUgcmVxdWlyZSBmdW5jdGlvblxuIFx0ZnVuY3Rpb24gX193ZWJwYWNrX3JlcXVpcmVfXyhtb2R1bGVJZCkge1xuXG4gXHRcdC8vIENoZWNrIGlmIG1vZHVsZSBpcyBpbiBjYWNoZVxuIFx0XHRpZihpbnN0YWxsZWRNb2R1bGVzW21vZHVsZUlkXSkge1xuIFx0XHRcdHJldHVybiBpbnN0YWxsZWRNb2R1bGVzW21vZHVsZUlkXS5leHBvcnRzO1xuIFx0XHR9XG4gXHRcdC8vIENyZWF0ZSBhIG5ldyBtb2R1bGUgKGFuZCBwdXQgaXQgaW50byB0aGUgY2FjaGUpXG4gXHRcdHZhciBtb2R1bGUgPSBpbnN0YWxsZWRNb2R1bGVzW21vZHVsZUlkXSA9IHtcbiBcdFx0XHRpOiBtb2R1bGVJZCxcbiBcdFx0XHRsOiBmYWxzZSxcbiBcdFx0XHRleHBvcnRzOiB7fVxuIFx0XHR9O1xuXG4gXHRcdC8vIEV4ZWN1dGUgdGhlIG1vZHVsZSBmdW5jdGlvblxuIFx0XHRtb2R1bGVzW21vZHVsZUlkXS5jYWxsKG1vZHVsZS5leHBvcnRzLCBtb2R1bGUsIG1vZHVsZS5leHBvcnRzLCBfX3dlYnBhY2tfcmVxdWlyZV9fKTtcblxuIFx0XHQvLyBGbGFnIHRoZSBtb2R1bGUgYXMgbG9hZGVkXG4gXHRcdG1vZHVsZS5sID0gdHJ1ZTtcblxuIFx0XHQvLyBSZXR1cm4gdGhlIGV4cG9ydHMgb2YgdGhlIG1vZHVsZVxuIFx0XHRyZXR1cm4gbW9kdWxlLmV4cG9ydHM7XG4gXHR9XG5cblxuIFx0Ly8gZXhwb3NlIHRoZSBtb2R1bGVzIG9iamVjdCAoX193ZWJwYWNrX21vZHVsZXNfXylcbiBcdF9fd2VicGFja19yZXF1aXJlX18ubSA9IG1vZHVsZXM7XG5cbiBcdC8vIGV4cG9zZSB0aGUgbW9kdWxlIGNhY2hlXG4gXHRfX3dlYnBhY2tfcmVxdWlyZV9fLmMgPSBpbnN0YWxsZWRNb2R1bGVzO1xuXG4gXHQvLyBkZWZpbmUgZ2V0dGVyIGZ1bmN0aW9uIGZvciBoYXJtb255IGV4cG9ydHNcbiBcdF9fd2VicGFja19yZXF1aXJlX18uZCA9IGZ1bmN0aW9uKGV4cG9ydHMsIG5hbWUsIGdldHRlcikge1xuIFx0XHRpZighX193ZWJwYWNrX3JlcXVpcmVfXy5vKGV4cG9ydHMsIG5hbWUpKSB7XG4gXHRcdFx0T2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIG5hbWUsIHsgZW51bWVyYWJsZTogdHJ1ZSwgZ2V0OiBnZXR0ZXIgfSk7XG4gXHRcdH1cbiBcdH07XG5cbiBcdC8vIGRlZmluZSBfX2VzTW9kdWxlIG9uIGV4cG9ydHNcbiBcdF9fd2VicGFja19yZXF1aXJlX18uciA9IGZ1bmN0aW9uKGV4cG9ydHMpIHtcbiBcdFx0aWYodHlwZW9mIFN5bWJvbCAhPT0gJ3VuZGVmaW5lZCcgJiYgU3ltYm9sLnRvU3RyaW5nVGFnKSB7XG4gXHRcdFx0T2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFN5bWJvbC50b1N0cmluZ1RhZywgeyB2YWx1ZTogJ01vZHVsZScgfSk7XG4gXHRcdH1cbiBcdFx0T2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsICdfX2VzTW9kdWxlJywgeyB2YWx1ZTogdHJ1ZSB9KTtcbiBcdH07XG5cbiBcdC8vIGNyZWF0ZSBhIGZha2UgbmFtZXNwYWNlIG9iamVjdFxuIFx0Ly8gbW9kZSAmIDE6IHZhbHVlIGlzIGEgbW9kdWxlIGlkLCByZXF1aXJlIGl0XG4gXHQvLyBtb2RlICYgMjogbWVyZ2UgYWxsIHByb3BlcnRpZXMgb2YgdmFsdWUgaW50byB0aGUgbnNcbiBcdC8vIG1vZGUgJiA0OiByZXR1cm4gdmFsdWUgd2hlbiBhbHJlYWR5IG5zIG9iamVjdFxuIFx0Ly8gbW9kZSAmIDh8MTogYmVoYXZlIGxpa2UgcmVxdWlyZVxuIFx0X193ZWJwYWNrX3JlcXVpcmVfXy50ID0gZnVuY3Rpb24odmFsdWUsIG1vZGUpIHtcbiBcdFx0aWYobW9kZSAmIDEpIHZhbHVlID0gX193ZWJwYWNrX3JlcXVpcmVfXyh2YWx1ZSk7XG4gXHRcdGlmKG1vZGUgJiA4KSByZXR1cm4gdmFsdWU7XG4gXHRcdGlmKChtb2RlICYgNCkgJiYgdHlwZW9mIHZhbHVlID09PSAnb2JqZWN0JyAmJiB2YWx1ZSAmJiB2YWx1ZS5fX2VzTW9kdWxlKSByZXR1cm4gdmFsdWU7XG4gXHRcdHZhciBucyA9IE9iamVjdC5jcmVhdGUobnVsbCk7XG4gXHRcdF9fd2VicGFja19yZXF1aXJlX18ucihucyk7XG4gXHRcdE9iamVjdC5kZWZpbmVQcm9wZXJ0eShucywgJ2RlZmF1bHQnLCB7IGVudW1lcmFibGU6IHRydWUsIHZhbHVlOiB2YWx1ZSB9KTtcbiBcdFx0aWYobW9kZSAmIDIgJiYgdHlwZW9mIHZhbHVlICE9ICdzdHJpbmcnKSBmb3IodmFyIGtleSBpbiB2YWx1ZSkgX193ZWJwYWNrX3JlcXVpcmVfXy5kKG5zLCBrZXksIGZ1bmN0aW9uKGtleSkgeyByZXR1cm4gdmFsdWVba2V5XTsgfS5iaW5kKG51bGwsIGtleSkpO1xuIFx0XHRyZXR1cm4gbnM7XG4gXHR9O1xuXG4gXHQvLyBnZXREZWZhdWx0RXhwb3J0IGZ1bmN0aW9uIGZvciBjb21wYXRpYmlsaXR5IHdpdGggbm9uLWhhcm1vbnkgbW9kdWxlc1xuIFx0X193ZWJwYWNrX3JlcXVpcmVfXy5uID0gZnVuY3Rpb24obW9kdWxlKSB7XG4gXHRcdHZhciBnZXR0ZXIgPSBtb2R1bGUgJiYgbW9kdWxlLl9fZXNNb2R1bGUgP1xuIFx0XHRcdGZ1bmN0aW9uIGdldERlZmF1bHQoKSB7IHJldHVybiBtb2R1bGVbJ2RlZmF1bHQnXTsgfSA6XG4gXHRcdFx0ZnVuY3Rpb24gZ2V0TW9kdWxlRXhwb3J0cygpIHsgcmV0dXJuIG1vZHVsZTsgfTtcbiBcdFx0X193ZWJwYWNrX3JlcXVpcmVfXy5kKGdldHRlciwgJ2EnLCBnZXR0ZXIpO1xuIFx0XHRyZXR1cm4gZ2V0dGVyO1xuIFx0fTtcblxuIFx0Ly8gT2JqZWN0LnByb3RvdHlwZS5oYXNPd25Qcm9wZXJ0eS5jYWxsXG4gXHRfX3dlYnBhY2tfcmVxdWlyZV9fLm8gPSBmdW5jdGlvbihvYmplY3QsIHByb3BlcnR5KSB7IHJldHVybiBPYmplY3QucHJvdG90eXBlLmhhc093blByb3BlcnR5LmNhbGwob2JqZWN0LCBwcm9wZXJ0eSk7IH07XG5cbiBcdC8vIF9fd2VicGFja19wdWJsaWNfcGF0aF9fXG4gXHRfX3dlYnBhY2tfcmVxdWlyZV9fLnAgPSBcIlwiO1xuXG5cbiBcdC8vIExvYWQgZW50cnkgbW9kdWxlIGFuZCByZXR1cm4gZXhwb3J0c1xuIFx0cmV0dXJuIF9fd2VicGFja19yZXF1aXJlX18oX193ZWJwYWNrX3JlcXVpcmVfXy5zID0gXCIuL3NyYy9pbmRleC5qc1wiKTtcbiIsInZhciBnO1xuXG4vLyBUaGlzIHdvcmtzIGluIG5vbi1zdHJpY3QgbW9kZVxuZyA9IChmdW5jdGlvbigpIHtcblx0cmV0dXJuIHRoaXM7XG59KSgpO1xuXG50cnkge1xuXHQvLyBUaGlzIHdvcmtzIGlmIGV2YWwgaXMgYWxsb3dlZCAoc2VlIENTUClcblx0ZyA9IGcgfHwgbmV3IEZ1bmN0aW9uKFwicmV0dXJuIHRoaXNcIikoKTtcbn0gY2F0Y2ggKGUpIHtcblx0Ly8gVGhpcyB3b3JrcyBpZiB0aGUgd2luZG93IHJlZmVyZW5jZSBpcyBhdmFpbGFibGVcblx0aWYgKHR5cGVvZiB3aW5kb3cgPT09IFwib2JqZWN0XCIpIGcgPSB3aW5kb3c7XG59XG5cbi8vIGcgY2FuIHN0aWxsIGJlIHVuZGVmaW5lZCwgYnV0IG5vdGhpbmcgdG8gZG8gYWJvdXQgaXQuLi5cbi8vIFdlIHJldHVybiB1bmRlZmluZWQsIGluc3RlYWQgb2Ygbm90aGluZyBoZXJlLCBzbyBpdCdzXG4vLyBlYXNpZXIgdG8gaGFuZGxlIHRoaXMgY2FzZS4gaWYoIWdsb2JhbCkgeyAuLi59XG5cbm1vZHVsZS5leHBvcnRzID0gZztcbiIsImNsYXNzIEV2ZW50IHtcbiAgICBjb25zdHJ1Y3RvcihkaW1lbnNpb24sIHhwRXZlbnRJZCwgdGltZSwgYXR0cnNNYXAsIGV2ZW50QXR0cnNNYXApIHtcbiAgICAgICAgdGhpcy5kaW1lbnNpb24gPSBkaW1lbnNpb247XG4gICAgICAgIHRoaXMueHBFdmVudElkID0geHBFdmVudElkO1xuICAgICAgICB0aGlzLnRpbWUgPSB0aW1lO1xuICAgICAgICB0aGlzLmF0dHJzTWFwID0gYXR0cnNNYXA7XG4gICAgICAgIHRoaXMuZXZlbnRBdHRyc01hcCA9IGV2ZW50QXR0cnNNYXA7XG4gICAgfVxuXG4gICAgZ2V0SWQoKSB7XG4gICAgXHRyZXR1cm4gdGhpcy54cEV2ZW50SWQ7XG4gICAgfVxuXG4gICAgc2V0SWQoeHBFdmVudElkKXtcbiAgICAgICAgdGhpcy54cEV2ZW50SWQgPSB4cEV2ZW50SWQ7XG4gICAgfVxuXG4gICAgZ2V0VGltZSgpe1xuICAgICAgICByZXR1cm4gdGhpcy50aW1lO1xuICAgIH1cblxuICAgIHNldFRpbWUodGltZSkge1xuICAgICAgICB0aGlzLnRpbWUgPSB0aW1lO1xuICAgIH1cblxuICAgIGdldEV2ZW50QXR0cihrZXkpIHtcbiAgICBcdHJldHVybiB0aGlzLmV2ZW50QXR0cnNNYXBba2V5XTtcbiAgICB9XG5cbiAgICAvL+aUvue9ruS4jemcgOimgeaJk+eCueeahEsgViAg5Y+q6K6w5b2V5ZyoRXZlbnTkuK1cbiAgICBwdXRFdmVudEF0dHIoa2V5LCB2YWx1ZSl7XG4gICAgICAgIHRoaXMuZXZlbnRBdHRyc01hcFtrZXldID0gdmFsdWU7XG4gICAgfVxuXG4gICAgZ2V0QXR0cihrZXkpIHtcbiAgICBcdHJldHVybiB0aGlzLmF0dHJzTWFwW2tleV07XG4gICAgfVxuXG4gICAgLy/mlL7nva7pnIDopoHmiZPngrnnmoRLIFZcbiAgICBwdXRBdHRyKGtleSwgdmFsdWUpe1xuICAgICAgICB0aGlzLmF0dHJzTWFwW2tleV0gPSB2YWx1ZTtcbiAgICB9XG5cbiAgICBnZXREaW1lbnNpb24oKSB7XG4gICAgICAgIHJldHVybiB0aGlzLmRpbWVuc2lvbjtcbiAgICB9XG5cbiAgICBzZXREaW1lbnNpb24oZGltZW5zaW9uKXtcbiAgICAgICAgdGhpcy5kaW1lbnNpb24gPSBkaW1lbnNpb247XG4gICAgfVxuXG59XG5cbm1vZHVsZS5leHBvcnRzID0gRXZlbnQ7XG4iLCJ2YXIgaW5pdFN0cmVhbSA9IHJlcXVpcmUoJy4vWFBTdHJlYW0nKTtcbnZhciBFdmVudCA9IHJlcXVpcmUoJy4vWFBFdmVudC5qcycpO1xudmFyIGluaXRUcmFja2VycyA9IHJlcXVpcmUoJy4vWFBSZWdpc3RlclRyYWNrZXJzJyk7XG5cbnZhciBzdHJlYW0gPSBuZXcgaW5pdFN0cmVhbSgpO1xudmFyIG9zdHlwZSA9IDE7XG5pbml0VHJhY2tlcnMucmVnaXN0ZXJTeXN0ZW1UcmFja2VyKHN0cmVhbSk7XG5mdW5jdGlvbiBzZW5kRXZlbnQoZXZlbnQpIHtcbiAgICB2YXIgb2JqcztcbiAgICBpZihvc3R5cGUgPT0gMCl7XG4gICAgICAgIG9ianM9IGV2YWwoZXZlbnQpO1xuICAgIH1lbHNle1xuICAgICAgICBvYmpzPSBKU09OLnBhcnNlKGV2ZW50KTtcbiAgICB9XG4gICAgdmFyIGlubmVyRXZlbnQgPSBuZXcgRXZlbnQob2Jqcy5kaW1lbnNpb24sIG9ianMueHBFdmVudElkLCBvYmpzLnRpbWUsIG9ianMuYXR0cnNNYXAsIG9ianMuZXZlbnRBdHRyc01hcCk7XG4gICAgc3RyZWFtLnJlY2VpdmVFdmVudChpbm5lckV2ZW50KVxufVxuXG4vL+WMuuWIhm9zXG5mdW5jdGlvbiBpbml0KG9zVHlwZSl7XG4gICAgb3N0eXBlID0gb3NUeXBlO1xufVxuXG5tb2R1bGUuZXhwb3J0cyA9IHtcbiAgICBuZXdTdHJlYW06IHN0cmVhbSxcbiAgICBzZW5kRXZlbnQ6IHNlbmRFdmVudFxufTtcbmdsb2JhbC5zZW5kRXZlbnQgPSBzZW5kRXZlbnQ7XG5nbG9iYWwuaW5pdCA9IGluaXQ7XG5cbiIsInZhciBFdmVudFRyYWNrZXIgPSByZXF1aXJlKCcuL1hQVHJhY2tlci5qcycpO1xudmFyIHN0cmVhbSA9IHJlcXVpcmUoJy4vWFBTdHJlYW0nKTtcbnZhciBTdGFzaEV2ZW50VHJhY2tlciA9IHJlcXVpcmUoJy4vWFBTdGFzaFRyYWNrZXInKTtcblxudmFyIGlzVGVzdCA9IDE7XG5cbi8v5qC55o2uIGRpbWVuc2lvbiDms6jlhozkuIDnu4QgdHJhY2tlclxuZnVuY3Rpb24gcmVnaXN0ZXJUcmFja2VycyhzdHJlYW0sIGRpbWVuc2lvbil7XG4gICAgdmFyIHRyYWNrZXJzID0gW107XG5cbiAgICB2YXIgeHBhbmVsX3N3X3RyYWNrZXIgPSBuZXcgRXZlbnRUcmFja2VyKFwieHBhbmVsX3N3XCIpO1xuICAgIHhwYW5lbF9zd190cmFja2VyLmFkZERlc2NyaXB0b3IoKGV2ZW50LCBmdWxmaWxsLCBicmVha2VyKT0+e1xuICAgICAgICBpZiAoZXZlbnQuZ2V0SWQoKSA9PSBcInhwYW5lbF9zd1wiKSB7XG4gICAgICAgICAgICBmdWxmaWxsKGV2ZW50KTtcbiAgICAgICAgfVxuICAgIH0pLmFkZENvbXBsZXRpb24oKGV2ZW50KT0+e1xuICAgICAgICB2YXIganNvbkRhdGEgPSBKU09OLnN0cmluZ2lmeShldmVudCk7XG4gICAgICAgIHRyaWdnZXJSZXBvcnQoXCJ4cGFuZWxfc3dcIiwganNvbkRhdGEpO1xuICAgIH0pO1xuICAgIHRyYWNrZXJzLnB1c2goeHBhbmVsX3N3X3RyYWNrZXIpO1xuXG4gICAgdmFyIHB1bGx1cF90cmFja2VyID0gbmV3IEV2ZW50VHJhY2tlcihcInhwYW5lbF9wdWxsX3VwXCIpO1xuICAgIHB1bGx1cF90cmFja2VyLmFkZERlc2NyaXB0b3IoKGV2ZW50LCBmdWxmaWxsLCBicmVha2VyKT0+e1xuICAgICAgICBpZiAoKGV2ZW50LmdldElkKCkgPT0gXCJ4cGFuZWxfaW5pdFwiIHx8IGV2ZW50LmdldElkKCkgPT0gXCJ4cGFuZWxfc2Nyb2xsXCIpICYmIGV2ZW50LmdldEV2ZW50QXR0cihcInNjcm9sbF9kaXN0YW5jZVwiKSA9PSAwKSB7XG4gICAgICAgICAgICBmdWxmaWxsKGV2ZW50KTtcbiAgICAgICAgfVxuICAgIH0pLmFkZERlc2NyaXB0b3IoKGV2ZW50LCBmdWxmaWxsLCBicmVha2VyKT0+e1xuICAgICAgICBpZiAoZXZlbnQuZ2V0SWQoKSA9PSBcInhwYW5lbF9zY3JvbGxcIiAmJiBldmVudC5nZXRFdmVudEF0dHIoXCJzY3JvbGxfZGlzdGFuY2VcIikgPiAwKSB7XG4gICAgICAgICAgICBmdWxmaWxsKGV2ZW50KTtcbiAgICAgICAgfVxuICAgIH0pLmFkZENvbXBsZXRpb24oKGV2ZW50KT0+e1xuICAgICAgICB2YXIganNvbkRhdGEgPSBKU09OLnN0cmluZ2lmeShldmVudCk7XG4gICAgICAgIHRyaWdnZXJSZXBvcnQoXCJ4cGFuZWxfcHVsbF91cFwiLCBqc29uRGF0YSk7XG4gICAgfSkuYWRkUmVzZW5kRXZlbnRDYigoZXZlbnQsIHRpbWVzdGFtcCk9PntcbiAgICAgICAgICAgIGV2ZW50LnNldFRpbWUodGltZXN0YW1wKTtcbiAgICAgICAgICAgIGV2ZW50LnNldElkKFwieHBhbmVsX3B1bGxfdXBcIik7XG4gICAgICAgICAgICByZXR1cm4gZXZlbnQ7XG4gICAgICAgIH1cbiAgICApO1xuICAgIHRyYWNrZXJzLnB1c2gocHVsbHVwX3RyYWNrZXIpO1xuXG4gICAgdmFyIHhwYW5lbF9zd190aW1lX3RyYWNrZXIgPSBuZXcgU3Rhc2hFdmVudFRyYWNrZXIoXCJrZWVwX3RpbWVcIik7XG4gICAgeHBhbmVsX3N3X3RpbWVfdHJhY2tlci5hZGREZXNjcmlwdG9yKChldmVudCwgZnVsZmlsbCwgYnJlYWtlcik9PntcbiAgICAgICAgaWYgKGV2ZW50LmdldElkKCkgPT0gXCJvbnJlc3VtZVwiKSB7XG4gICAgICAgICAgICBmdWxmaWxsKGV2ZW50KTtcbiAgICAgICAgICAgIHhwYW5lbF9zd190aW1lX3RyYWNrZXIucHV0U3Rhc2hFdmVudChcIkFcIiwgZXZlbnQpO1xuICAgICAgICB9XG4gICAgfSkuYWRkRGVzY3JpcHRvcigoZXZlbnQsIGZ1bGZpbGwsIGJyZWFrZXIpPT57XG4gICAgICAgIGlmIChldmVudC5nZXRJZCgpID09IFwib25wYXVzZVwiKSB7XG4gICAgICAgICAgICBsZXQgZHVyYXRpb24gPSBldmVudC5nZXRUaW1lKCkgLSB4cGFuZWxfc3dfdGltZV90cmFja2VyLmdldFN0YXNoRXZlbnQoXCJBXCIpLmdldFRpbWUoKTtcbiAgICAgICAgICAgIGV2ZW50LnB1dEF0dHIoXCJ0b2FzdF9zdHJcIiwgXCLpobXpnaLlgZznlZnml7bplb/vvIwg6ICX5pe2OlwiICsgZHVyYXRpb24vMTAwMCArIFwi56eSXCIpO1xuICAgICAgICAgICAgZnVsZmlsbChldmVudCk7XG4gICAgICAgIH1cbiAgICB9KS5hZGRDb21wbGV0aW9uKChldmVudCk9PntcbiAgICAgICAgdmFyIGpzb25EYXRhID0gSlNPTi5zdHJpbmdpZnkoZXZlbnQpO1xuICAgICAgICB0cmlnZ2VyUmVwb3J0KFwieHBhbmVsX3N3X3RpbWVcIiwganNvbkRhdGEpO1xuICAgIH0pLmFkZFJlc2VuZEV2ZW50Q2IoKGV2ZW50LCB0aW1lc3RhbXApPT57XG4gICAgICAgICAgZXZlbnQuc2V0VGltZSh0aW1lc3RhbXApO1xuICAgICAgICAgIGV2ZW50LnNldElkKFwidG9hc3RcIik7XG4gICAgICAgICAgcmV0dXJuIGV2ZW50O1xuICAgICAgfVxuICAgICk7XG4gICAgdHJhY2tlcnMucHVzaCh4cGFuZWxfc3dfdGltZV90cmFja2VyKTtcblxuICAgIHZhciB4cGFuZWxfaGFsZl9yZXZlYWxfc3dfdHJhY2tlciA9IG5ldyBFdmVudFRyYWNrZXIoXCJ4cGFuZWxfaGFsZl9yZXZlYWxfc3dcIik7XG4gICAgeHBhbmVsX2hhbGZfcmV2ZWFsX3N3X3RyYWNrZXIuYWRkRGVzY3JpcHRvcigoZXZlbnQsIGZ1bGZpbGwsIGJyZWFrZXIpPT57XG4gICAgICAgIGlmIChldmVudC5nZXRJZCgpID09IFwieHBhbmVsX2hhbGZfc3dcIikge1xuICAgICAgICAgICAgZnVsZmlsbChldmVudCk7XG4gICAgICAgIH1cbiAgICB9KS5hZGRDb21wbGV0aW9uKChldmVudCk9PntcbiAgICAgICAgdmFyIGpzb25EYXRhID0gSlNPTi5zdHJpbmdpZnkoZXZlbnQpO1xuICAgICAgICB0cmlnZ2VyUmVwb3J0KFwieHBhbmVsX3N3XCIsIGpzb25EYXRhKTtcbiAgICB9KTtcbiAgICB0cmFja2Vycy5wdXNoKHhwYW5lbF9oYWxmX3JldmVhbF9zd190cmFja2VyKTtcblxuICAgIHZhciB4cGFuZWxfY2FyZF9ja190cmFja2VyID0gbmV3IEV2ZW50VHJhY2tlcihcInhwYW5lbF9jYXJkX2NrXCIpO1xuICAgIHhwYW5lbF9jYXJkX2NrX3RyYWNrZXIuYWRkRGVzY3JpcHRvcigoZXZlbnQsIGZ1bGZpbGwsIGJyZWFrZXIpPT57XG4gICAgICAgIGlmIChldmVudC5nZXRJZCgpID09IFwiY2FyZF9ja1wiKSB7XG4gICAgICAgICAgICBmdWxmaWxsKGV2ZW50KTtcbiAgICAgICAgfVxuICAgIH0pLmFkZENvbXBsZXRpb24oKGV2ZW50KT0+e1xuICAgICAgICAgdmFyIGpzb25EYXRhID0gSlNPTi5zdHJpbmdpZnkoZXZlbnQpO1xuICAgICAgICAgdHJpZ2dlclJlcG9ydChcInhwYW5lbF9jYXJkX2NrXCIsIGpzb25EYXRhKTtcbiAgICAgfSk7XG4gICAgdHJhY2tlcnMucHVzaCh4cGFuZWxfY2FyZF9ja190cmFja2VyKTtcbiAgICBcbiAgICB2YXIgeHBhbmVsX2J1dHRvbl9ja190cmFja2VyID0gbmV3IEV2ZW50VHJhY2tlcihcInhwYW5lbF9idXR0b25fY2tcIik7XG4gICAgeHBhbmVsX2J1dHRvbl9ja190cmFja2VyLmFkZERlc2NyaXB0b3IoKGV2ZW50LCBmdWxmaWxsLCBicmVha2VyKT0+e1xuICAgICAgICBpZiAoZXZlbnQuZ2V0SWQoKSA9PSBcImJ0bl9ja1wiKSB7XG4gICAgICAgICAgICBmdWxmaWxsKGV2ZW50KTtcbiAgICAgICAgfVxuICAgIH0pLmFkZENvbXBsZXRpb24oKGV2ZW50KT0+e1xuICAgICAgICB2YXIganNvbkRhdGEgPSBKU09OLnN0cmluZ2lmeShldmVudCk7XG4gICAgICAgIHRyaWdnZXJSZXBvcnQoXCJ4cGFuZWxfYnV0dG9uX2NrXCIsIGpzb25EYXRhKTtcbiAgICB9KTtcbiAgICB0cmFja2Vycy5wdXNoKHhwYW5lbF9idXR0b25fY2tfdHJhY2tlcik7XG5cbiAgICB2YXIgeHBhbmVsX3N1YmNhcmRfY2tfdHJhY2tlciA9IG5ldyBFdmVudFRyYWNrZXIoXCJ4cGFuZWxfc3ViY2FyZF9ja1wiKTtcbiAgICB4cGFuZWxfc3ViY2FyZF9ja190cmFja2VyLmFkZERlc2NyaXB0b3IoKGV2ZW50LCBmdWxmaWxsLCBicmVha2VyKT0+e1xuICAgICAgICBpZiAoZXZlbnQuZ2V0SWQoKSA9PSBcInN1YmNhcmRfY2tcIikge1xuICAgICAgICAgICAgZnVsZmlsbChldmVudCk7XG4gICAgICAgIH1cbiAgICB9KS5hZGRDb21wbGV0aW9uKChldmVudCk9PntcbiAgICAgICAgdmFyIGpzb25EYXRhID0gSlNPTi5zdHJpbmdpZnkoZXZlbnQpO1xuICAgICAgICB0cmlnZ2VyUmVwb3J0KFwieHBhbmVsX3N1YmNhcmRfY2tcIiwganNvbkRhdGEpO1xuICAgIH0pO1xuICAgIHRyYWNrZXJzLnB1c2goeHBhbmVsX3N1YmNhcmRfY2tfdHJhY2tlcik7XG5cbiAgICB2YXIgeHBhbmVsX3RyeV9wdWxsX2Rvd25fdHJhY2tlciA9IG5ldyBFdmVudFRyYWNrZXIoXCJ0cnlfcHVsbF9kb3duXCIpO1xuICAgIHhwYW5lbF90cnlfcHVsbF9kb3duX3RyYWNrZXIuYWRkRGVzY3JpcHRvcigoZXZlbnQsIGZ1bGZpbGwsIGJyZWFrZXIpPT57XG4gICAgICAgIGlmIChldmVudC5nZXRJZCgpID09IFwidHJ5X3B1bGxfZG93blwiKSB7XG4gICAgICAgICAgICBmdWxmaWxsKGV2ZW50KTtcbiAgICAgICAgfVxuICAgIH0pLmFkZENvbXBsZXRpb24oKGV2ZW50KT0+e1xuICAgICAgICB2YXIganNvbkRhdGEgPSBKU09OLnN0cmluZ2lmeShldmVudCk7XG4gICAgICAgIHRyaWdnZXJSZXBvcnQoXCJ4cGFuZWxfdHJ5X3B1bGxfZG93blwiLCBqc29uRGF0YSk7XG4gICAgfSk7XG4gICAgdHJhY2tlcnMucHVzaCh4cGFuZWxfdHJ5X3B1bGxfZG93bl90cmFja2VyKTtcblxuICAgIHZhciB4cGFuZWxfbmV0X3N1Y2Nlc19zaG93X2hlaWdodF90cmFja2VyID0gbmV3IEV2ZW50VHJhY2tlcihcInRyeV9wdWxsX2Rvd25cIik7XG4gICAgeHBhbmVsX25ldF9zdWNjZXNfc2hvd19oZWlnaHRfdHJhY2tlci5hZGREZXNjcmlwdG9yKChldmVudCwgZnVsZmlsbCwgYnJlYWtlcik9PntcbiAgICAgICAgaWYgKGV2ZW50LmdldElkKCkgPT0gXCJuZXR3b3JrX2JhY2tcIiAmJiBldmVudC5nZXQpIHtcbiAgICAgICAgICAgIGZ1bGZpbGwoZXZlbnQpO1xuICAgICAgICB9XG4gICAgfSkuXG4gICAgYWRkRGVzY3JpcHRvcigoZXZlbnQsIGZ1bGZpbGwsIGJyZWFrZXIpPT57XG4gICAgICAgICAgICAgIGlmIChldmVudC5nZXRJZCgpID09IFwidHJ5X3B1bGxfZG93blwiKSB7XG4gICAgICAgICAgICAgICAgICBmdWxmaWxsKGV2ZW50KTtcbiAgICAgICAgICAgICAgfVxuICAgICAgICAgIH0pXG4gICAgIC5hZGREZXNjcmlwdG9yKChldmVudCwgZnVsZmlsbCwgYnJlYWtlcik9PntcbiAgICAgICAgICAgICAgICAgaWYgKGV2ZW50LmdldElkKCkgPT0gXCJ0cnlfcHVsbF9kb3duXCIpIHtcbiAgICAgICAgICAgICAgICAgICAgIGZ1bGZpbGwoZXZlbnQpO1xuICAgICAgICAgICAgICAgICB9XG4gICAgICAgICAgICAgfSlcbiAgICAuYWRkQ29tcGxldGlvbigoZXZlbnQpPT57XG4gICAgICAgIHZhciBqc29uRGF0YSA9IEpTT04uc3RyaW5naWZ5KGV2ZW50KTtcbiAgICAgICAgdHJpZ2dlclJlcG9ydChcInhwYW5lbF9uZXRfc3VjY2VzX3Nob3dfaGVpZ2h0XCIsIGpzb25EYXRhKTtcbiAgICB9KTtcbiAgICB0cmFja2Vycy5wdXNoKHhwYW5lbF9uZXRfc3VjY2VzX3Nob3dfaGVpZ2h0X3RyYWNrZXIpO1xuXG4gICAgLy8g5rOo5YaM5pW057uEIHRyYWNrZXJzXG4gICAgc3RyZWFtLnJlZ2lzdGVyVHJhY2tlcnModHJhY2tlcnMsIGRpbWVuc2lvbik7XG59XG5cbmZ1bmN0aW9uIHJlZ2lzdGVyU3lzdGVtVHJhY2tlcihzdHJlYW0pe1xuICAgIHZhciB0cmFja2VycyA9IFtdO1xuICAgIC8vIFZDIGluaXRcbiAgICAvL3ZhciByZWdpc3RlclRyYWNrZXIgID0gbmV3IEV2ZW50VHJhY2tlcihcInhldmVudF90cmFja2Vyc19yZWdpc3RlclwiKTtcbiAgICB2YXIgcmVnaXN0ZXJUcmFja2VyID0gbmV3IEV2ZW50VHJhY2tlcihcInhldmVudF90cmFja2Vyc19yZWdpc3RlclwiKTtcbiAgICByZWdpc3RlclRyYWNrZXIuYWRkRGVzY3JpcHRvcigoZXZlbnQsIGZ1bGZpbGwsIGJyZWFrZXIpPT57XG4gICAgICAgIGlmIChldmVudC5nZXRJZCgpID09IFwiWEV2ZW50U3lzdGVtX1ZDX2luaXRcIikge1xuICAgICAgICAgICAgZnVsZmlsbChldmVudCk7XG4gICAgICAgIH1cbiAgICB9KS5hZGRDb21wbGV0aW9uKChldmVudCk9PntcbiAgICAgICAvLyDms6jlhowgdHJhY2tlcnNcbiAgICAgICByZWdpc3RlclRyYWNrZXJzKHN0cmVhbSwgZXZlbnQuZ2V0RGltZW5zaW9uKCkpXG4gICAgfSk7XG4gICAgdHJhY2tlcnMucHVzaChyZWdpc3RlclRyYWNrZXIpO1xuXG4gICAgLy8gVkMgZGVhbGxvY1xuICAgIHZhciB1bnJlZ2lzdGVyVHJhY2tlciAgPSBuZXcgRXZlbnRUcmFja2VyKFwieGV2ZW50X3RyYWNrZXJzX2RlYWxsb2NcIik7XG4gICAgdW5yZWdpc3RlclRyYWNrZXIuYWRkRGVzY3JpcHRvcigoZXZlbnQsIGZ1bGZpbGwsIGJyZWFrZXIpPT57XG4gICAgICAgIGlmIChldmVudC5nZXRJZCgpID09IFwiWEV2ZW50U3lzdGVtX1ZDX2RlYWxsb2NcIikge1xuICAgICAgICAgICAgZnVsZmlsbChldmVudCk7XG4gICAgICAgIH1cbiAgICB9KS5hZGRDb21wbGV0aW9uKChldmVudCk9PntcbiAgICAgICAvLyDplIDmr4EgdHJhY2tlcnNcbiAgICAgICBzdHJlYW0udW5yZWdpc3RlclRyYWNrZXIoZXZlbnQuZ2V0RGltZW5zaW9uKCkpXG4gICAgfSk7XG4gICAgdHJhY2tlcnMucHVzaCh1bnJlZ2lzdGVyVHJhY2tlcik7XG5cbiAgICAvLyDlkIzkuIDkuKogVkMg77yM5Zy65pmvKGRpbWVuc2lvbinliIfmjaJcbiAgICB2YXIgZGltZW5zaW9uQ2hhbmdlVHJhY2tlciAgPSBuZXcgRXZlbnRUcmFja2VyKFwieGV2ZW50X2RpbWVuc2lvbl9jaGFuZ2VcIik7XG4gICAgZGltZW5zaW9uQ2hhbmdlVHJhY2tlci5hZGREZXNjcmlwdG9yKChldmVudCwgZnVsZmlsbCwgYnJlYWtlcik9PntcbiAgICAgICAgaWYgKGV2ZW50LmdldElkKCkgPT0gXCJYRXZlbnRTeXN0ZW1fZGltZW5zaW9uX2NoYW5nZVwiKSB7XG4gICAgICAgICAgICBmdWxmaWxsKGV2ZW50KTtcbiAgICAgICAgfVxuICAgIH0pLmFkZENvbXBsZXRpb24oKGV2ZW50KT0+e1xuICAgICAgIHJlZ2lzdGVyVHJhY2tlcnMoc3RyZWFtLCBldmVudC5nZXREaW1lbnNpb24oKSlcbiAgICB9KTtcbiAgICB0cmFja2Vycy5wdXNoKGRpbWVuc2lvbkNoYW5nZVRyYWNrZXIpO1xuXG4gICAgLy8g5YiH5o2i5Lia5Yqh57q/6Iez5pyq6KKr6ZSA5q+B55qEIFZDXG4gICAgdmFyIHB1c2hWQ1RyYWNrZXIgID0gbmV3IEV2ZW50VHJhY2tlcihcInhldmVudF9wdXNoX1ZDXCIpO1xuICAgIHB1c2hWQ1RyYWNrZXIuYWRkRGVzY3JpcHRvcigoZXZlbnQsIGZ1bGZpbGwsIGJyZWFrZXIpPT57XG4gICAgICAgIGlmIChldmVudC5nZXRJZCgpID09IFwiWEV2ZW50U3lzdGVtX3B1c2hfVkNcIikge1xuICAgICAgICAgICAgZnVsZmlsbChldmVudCk7XG4gICAgICAgIH1cbiAgICB9KS5hZGRDb21wbGV0aW9uKChldmVudCk9PntcbiAgICAgICBzdHJlYW0uY2hhbmdlRGltZW5zaW9uQW5kUmVzZXRUcmFja2VycyhldmVudC5nZXREaW1lbnNpb24oKSk7XG4gICAgfSk7XG4gICAgdHJhY2tlcnMucHVzaChwdXNoVkNUcmFja2VyKTtcblxuICAgIC8vIOazqOWGjCBzeXN0ZW0gdHJhY2tlcnNcbiAgICBzdHJlYW0ucmVnaXN0ZXJUcmFja2Vycyh0cmFja2VycywgXCJYRXZlbnRTeXN0ZW1cIik7XG59XG5cbm1vZHVsZS5leHBvcnRzID0ge1xuICAgIHJlZ2lzdGVyVHJhY2tlcnM6cmVnaXN0ZXJUcmFja2VycyxcbiAgICByZWdpc3RlclN5c3RlbVRyYWNrZXI6cmVnaXN0ZXJTeXN0ZW1UcmFja2VyLFxufTtcblxuZnVuY3Rpb24gdHJpZ2dlclJlcG9ydChsb2csIGpzb25EYXRhKSB7XG4gICAgaWYgKGlzVGVzdCA9PSAxKSB7XG4gICAgICAgIGNvbnNvbGUubG9nKGxvZyk7XG4gICAgfSBlbHNlIHtcbiAgICAgICAgeHBFdmVudE1hbmFnZXIudHJpZ2dlclJlcG9ydChsb2csIGpzb25EYXRhKTtcbiAgICB9XG59IiwidmFyIEV2ZW50VHJhY2tlciA9IHJlcXVpcmUoJy4vWFBUcmFja2VyLmpzJyk7XG5jbGFzcyBTdGFzaEV2ZW50VHJhY2tlciBleHRlbmRzIEV2ZW50VHJhY2tlcntcbiAgICBjb25zdHJ1Y3RvcihuYW1lKSB7XG4gICAgICAgIHN1cGVyKG5hbWUpO1xuICAgICAgICB0aGlzLnN0YXNoRXZlbnRNYXAgPSBbXTtcbiAgICB9XG5cbiAgICBwdXRTdGFzaEV2ZW50KGFsaWFzLCBldmVudCl7XG4gICAgICAgIHRoaXMuc3Rhc2hFdmVudE1hcFthbGlhc10gPSBldmVudDtcbiAgICB9XG5cbiAgICBnZXRTdGFzaEV2ZW50KGFsaWFzKXtcbiAgICAgICAgcmV0dXJuIHRoaXMuc3Rhc2hFdmVudE1hcFthbGlhc107XG4gICAgfVxuXG4gICAgcmVjZWl2ZUV2ZW50KGV2ZW50KSB7XG4gICAgICAgIGxldCBkZXNjcmlwdG9yID0gdGhpcy5kZXNjcmlwdGlvbnNbdGhpcy5pbmRleF07XG4gICAgICAgIGlmKCFkZXNjcmlwdG9yKXtcbiAgICAgICAgICAgIHJldHVybiBudWxsO1xuICAgICAgICB9XG4gICAgICAgIHZhciBzZWxmID0gdGhpcztcbiAgICAgICAgbGV0IGZ1bGZpbGwgPSBmdW5jdGlvbihldmVudCkge1xuICAgICAgICAgICAgc2VsZi5pbmRleCsrO1xuICAgICAgICB9O1xuICAgICAgICBsZXQgYnJlYWtlciA9IChldmVudCkgPT4ge1xuICAgICAgICAgICAgc2VsZi5pbmRleCA9IDA7XG4gICAgICAgIH07XG5cbiAgICAgICAgZGVzY3JpcHRvcihldmVudCwgZnVsZmlsbCwgYnJlYWtlcik7XG4gICAgICAgIFxuICAgICAgICBpZiAodGhpcy5pbmRleCA+PSB0aGlzLmRlc2NyaXB0aW9ucy5sZW5ndGgpIHtcbiAgICAgICAgICAgIGlmICh0aGlzLmNvbXBsZXRpb24pIHtcbiAgICAgICAgICAgICAgICB0aGlzLmNvbXBsZXRpb24oZXZlbnQpO1xuICAgICAgICAgICAgfVxuICAgICAgICAgICAgdGhpcy5pbmRleCA9IDA7XG5cbiAgICAgICAgICAgIC8v5ruh6Laz5p2h5Lu25LmL5ZCO55yL5pyJ5rKh5pyJ6ZyA6KaB5Zue5Lyg55qE5LqL5Lu2XG4gICAgICAgICAgICBsZXQgcmVzZW5kRXZlbnQgPSB0aGlzLm9uUmVzZW5kRXZlbnQoZXZlbnQpO1xuLy8gICAgICAgICAgICAgICAgY29uc29sZS5sb2cocmVzZW5kRXZlbnQuZ2V0SWQoKSk7XG4gICAgICAgICAgICByZXR1cm4gcmVzZW5kRXZlbnQ7XG4gICAgICAgIH1cbiAgICAgICAgcmV0dXJuIG51bGw7XG4gICAgfVxufVxuXG5tb2R1bGUuZXhwb3J0cyA9IFN0YXNoRXZlbnRUcmFja2VyO1xuIiwiY2xhc3MgRXZlbnRTdHJlYW0ge1xuICAgIGNvbnN0cnVjdG9yKCkge1xuICAgICAgICAvLyBLIOe7tOW6piAgViDlrrnlmagoSyAgICAgViAgIClcbiAgICAgICAgdGhpcy5zdHJlYW1zID0gW107XG4gICAgICAgIC8vIOiusOW9leW9k+WJjee7tOW6plxuICAgICAgICB0aGlzLmN1cnJlbnREaW1lbnNpb24gPSBudWxsO1xuICAgIH1cblxuICAgIHJlY2VpdmVFdmVudChldmVudCkge1xuICAgICAgICBpZiAoZXZlbnQuZ2V0SWQoKS5pbmRleE9mKFwiWEV2ZW50U3lzdGVtXCIpID09IDApIHtcbiAgICAgICAgICAgIGxldCBzdHJlYW0gPSB0aGlzLnN0cmVhbXNbXCJYRXZlbnRTeXN0ZW1cIl07XG4gICAgICAgICAgICBsZXQgc3lzdGVtVHJhY2tlcnMgPSBzdHJlYW1bXCJ0cmFja2Vyc1wiXTtcbiAgICAgICAgICAgIGZvciAobGV0IHRyYWNrZXIgb2Ygc3lzdGVtVHJhY2tlcnMpIHtcbiAgICAgICAgICAgICAgICB0cmFja2VyLnJlY2VpdmVFdmVudChldmVudCk7XG4gICAgICAgICAgICB9XG4gICAgICAgICAgICByZXR1cm47XG4gICAgICAgIH1cblxuICAgICAgICAvL3RyYWNrZXLlm57kvKDnmoTkuovku7ZcbiAgICAgICAgdmFyIGJ1ZmZlckV2ZW50cyA9IG5ldyBBcnJheSgpXG4gICAgICAgIGxldCBjdXJyZW50U3RyZWFtID0gdGhpcy5zdHJlYW1zW3RoaXMuY3VycmVudERpbWVuc2lvbl07XG4gICAgICAgIGxldCBjdXJyZW50VHJhY2tlcnMgPSBjdXJyZW50U3RyZWFtW1widHJhY2tlcnNcIl07XG4gICAgICAgIGZvciAobGV0IHRyYWNrZXIgb2YgY3VycmVudFRyYWNrZXJzKSB7XG4gICAgICAgICAgICBsZXQgcmVTZW5kRXZlbnQgPSB0cmFja2VyLnJlY2VpdmVFdmVudChldmVudCk7XG4gICAgICAgICAgICBpZihyZVNlbmRFdmVudCl7XG4gICAgICAgICAgICAgICAgY29uc29sZS5sb2coXCJidWZmZXIgZXZlbnQgOiBcIiArIHJlU2VuZEV2ZW50LmdldElkKCkpO1xuICAgICAgICAgICAgICAgIGJ1ZmZlckV2ZW50cy5wdXNoKHJlU2VuZEV2ZW50KTtcbiAgICAgICAgICAgIH1cbiAgICAgICAgfVxuXG4gICAgICAgIGZvciAobGV0IGJ1ZmZlckV2ZW50IG9mIGJ1ZmZlckV2ZW50cylcbiAgICAgICAge1xuICAgICAgICAgICAgdGhpcy5yZWNlaXZlRXZlbnQoYnVmZmVyRXZlbnQpO1xuICAgICAgICB9XG4gICAgfVxuICAgIFxuICAgIHJlZ2lzdGVyVHJhY2tlcnModHJhY2tlcnMsIGRpbWVuc2lvbikgeyBcbiAgICAgICAgdGhpcy5jdXJyZW50RGltZW5zaW9uID0gZGltZW5zaW9uO1xuICAgICAgICAvLyDlrZjmlL7lvZPliY3nu7TluqbmiYDpnIDopoHnmoTkv6Hmga/lj4ogdHJhY2tlcnNcbiAgICAgICAgdmFyIHRyYWNrZXJHcm91cCA9IFtdO1xuICAgICAgICB0cmFja2VyR3JvdXBbXCJ0cmFja2Vyc1wiXSA9IHRyYWNrZXJzO1xuICAgICAgICB0aGlzLnN0cmVhbXNbZGltZW5zaW9uXSA9IHRyYWNrZXJHcm91cDtcbiAgICB9XG4gICAgXG4gICAgdW5yZWdpc3RlclRyYWNrZXIoZGltZW5zaW9uKXtcbiAgICAgICAgdGhpcy5jdXJyZW50RGltZW5zaW9uID0gbnVsbDtcbiAgICAgICAgaWYgKHRoaXMuc3RyZWFtc1tkaW1lbnNpb25dKSB7XG4gICAgICAgICAgICBkZWxldGUgdGhpcy5zdHJlYW1zW2RpbWVuc2lvbl07XG4gICAgICAgIH1cbiAgICB9XG5cbiAgICBjaGFuZ2VEaW1lbnNpb25BbmRSZXNldFRyYWNrZXJzKGRpbWVuc2lvbikge1xuICAgICAgICB0aGlzLmN1cnJlbnREaW1lbnNpb24gPSBkaW1lbnNpb247XG4gICAgICAgIGxldCBjdXJyZW50U3RyZWFtID0gdGhpcy5zdHJlYW1zW2RpbWVuc2lvbl07XG4gICAgICAgIGxldCBjdXJyZW50VHJhY2tlcnMgPSBjdXJyZW50U3RyZWFtW1widHJhY2tlcnNcIl07XG4gICAgICAgIGZvciAobGV0IHRyYWNrZXIgb2YgY3VycmVudFRyYWNrZXJzKSB7XG4gICAgICAgICAgICB0cmFja2VyLnJlc2V0KCk7XG4gICAgICAgIH1cbiAgICB9XG5cbn1cblxubW9kdWxlLmV4cG9ydHMgPSBFdmVudFN0cmVhbTtcbiIsImNsYXNzIEV2ZW50VHJhY2tlciB7XG4gICAgY29uc3RydWN0b3IobmFtZSkge1xuICAgICAgICB0aGlzLm5hbWUgPSBuYW1lO1xuICAgICAgICB0aGlzLmluZGV4ID0gMDtcbiAgICAgICAgdGhpcy5kZXNjcmlwdGlvbnMgPSBbXTtcbiAgICAgICAgdGhpcy5jb21wbGV0aW9uID0gbnVsbDtcbiAgICAgICAgdGhpcy5icmVha2VyID0gbnVsbDtcbiAgICAgICAgdGhpcy5yZXNlbmRFdmVudENiID0gbnVsbDtcbiAgICB9XG4gICAgYWRkRGVzY3JpcHRvcihkZXNjcmlwdG9yKSB7XG4gICAgICAgIHRoaXMuZGVzY3JpcHRpb25zLnB1c2goZGVzY3JpcHRvcik7XG4gICAgICAgIHJldHVybiB0aGlzO1xuICAgIH1cbiAgICBhZGRCcmVha2VyKGJyZWFrZXIpIHtcbiAgICAgICAgdGhpcy5icmVha2VyID0gYnJlYWtlcjtcbiAgICAgICAgcmV0dXJuIHRoaXM7XG4gICAgfVxuICAgIGFkZENvbXBsZXRpb24oY29tcGxldGlvbikge1xuICAgICAgICB0aGlzLmNvbXBsZXRpb24gPSBjb21wbGV0aW9uO1xuICAgICAgICByZXR1cm4gdGhpcztcbiAgICB9XG4gICAgYWRkUmVzZW5kRXZlbnRDYihyZXNlbmRFdmVudENiKXtcbiAgICAgICAgdGhpcy5yZXNlbmRFdmVudENiID0gcmVzZW5kRXZlbnRDYjtcbiAgICAgICAgcmV0dXJuIHRoaXM7XG4gICAgfVxuXG4gICAgcmVzZXQoKSB7XG4gICAgICAgIHRoaXMuaW5kZXggPSAwO1xuICAgIH1cblxuICAgIG9uUmVzZW5kRXZlbnQoZXZlbnQpe1xuICAgICAgICAvL+aehOW7uuaWsOS6i+S7tueahOaXtumXtOaIs1xuICAgICAgICB2YXIgdGltZXN0YW1wID0gRGF0ZS5wYXJzZShuZXcgRGF0ZSgpKTtcbiAgICAgICAgaWYodGhpcy5yZXNlbmRFdmVudENiKXtcbiAgICAgICAgICAgIGxldCByZVNlbnRFdmVudCA9IHRoaXMucmVzZW5kRXZlbnRDYihldmVudCwgdGltZXN0YW1wKTtcbiAgICAgICAgICAgIHJldHVybiByZVNlbnRFdmVudDtcbiAgICAgICAgfVxuICAgICAgICByZXR1cm4gbnVsbDtcbiAgICB9XG5cbiAgICByZWNlaXZlRXZlbnQoZXZlbnQpIHtcbiAgICAgICAgbGV0IGRlc2NyaXB0b3IgPSB0aGlzLmRlc2NyaXB0aW9uc1t0aGlzLmluZGV4XTtcbiAgICAgICAgaWYoIWRlc2NyaXB0b3Ipe1xuICAgICAgICAgICAgcmV0dXJuIG51bGw7XG4gICAgICAgIH1cbiAgICAgICAgdmFyIHNlbGYgPSB0aGlzO1xuICAgICAgICBsZXQgZnVsZmlsbCA9IGZ1bmN0aW9uKGV2ZW50KSB7XG4gICAgICAgICAgICBzZWxmLmluZGV4Kys7XG4gICAgICAgIH07XG4gICAgICAgIGxldCBicmVha2VyID0gKGV2ZW50KSA9PiB7XG4gICAgICAgICAgICBzZWxmLmluZGV4ID0gMDtcbiAgICAgICAgfTtcblxuLy8gICAgICAgIGNvbnNvbGUubG9nKGRlc2NyaXB0b3IpO1xuICAgICAgICBkZXNjcmlwdG9yKGV2ZW50LCBmdWxmaWxsLCBicmVha2VyKTtcbiAgICAgICAgXG4gICAgICAgIGlmICh0aGlzLmluZGV4ID49IHRoaXMuZGVzY3JpcHRpb25zLmxlbmd0aCkge1xuICAgICAgICAgICAgaWYgKHRoaXMuY29tcGxldGlvbikge1xuICAgICAgICAgICAgICAgIHRoaXMuY29tcGxldGlvbihldmVudCk7XG4gICAgICAgICAgICB9XG4gICAgICAgICAgICB0aGlzLmluZGV4ID0gMDtcbiAgICAgICAgICAgIC8v5ruh6Laz5p2h5Lu25LmL5ZCO55yL5pyJ5rKh5pyJ6ZyA6KaB5Zue5Lyg55qE5LqL5Lu2XG4gICAgICAgICAgICBsZXQgcmVzZW5kRXZlbnQgPSB0aGlzLm9uUmVzZW5kRXZlbnQoZXZlbnQpO1xuICAgICAgICAgICAgcmV0dXJuIHJlc2VuZEV2ZW50O1xuICAgICAgICB9XG4gICAgICAgIHJldHVybiBudWxsO1xuICAgIH1cbn1cblxubW9kdWxlLmV4cG9ydHMgPSBFdmVudFRyYWNrZXI7XG4iLCJyZXF1aXJlKCcuL1hQRXZlbnRNYW5hZ2VyLmpzJyk7XG4iXSwic291cmNlUm9vdCI6IiJ9