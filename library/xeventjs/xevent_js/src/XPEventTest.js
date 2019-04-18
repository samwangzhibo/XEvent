var Event = require('./XPEvent');
var eventManager = require('./XPEventManager');

var newEvent = new Event("", "XEventSystem_VC_init", "", "", "");
eventManager.newStream.receiveEvent(newEvent);

function testStr(eventJson) {
    eventManager.sendEvent(eventJson);
}

function test(eventID, eventAttrsMap) {
    var newEvent = new Event("", eventID,"" ,"" , eventAttrsMap);
    eventManager.newStream.receiveEvent(newEvent);
}


module.exports = {
    testStr: testStr,
    test: test
};