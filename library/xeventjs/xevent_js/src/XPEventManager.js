var initStream = require('./XPStream');
var Event = require('./XPEvent.js');
var initTrackers = require('./XPRegisterTrackers');

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
//    console.log(objs);
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

