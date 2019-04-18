var xpTest = require('./XPEventTest.js');

xpTest.test("xpanel_sw");
xpTest.test("card_ck");
xpTest.test("xpanel_sw");
xpTest.test("xpanel_init", {"scroll_distance": 0});
xpTest.test("card_ck");
xpTest.test("xpanel_scroll", {"scroll_distance": 10});
xpTest.test("btn_ck");
xpTest.testStr('{"attrsMap":{},"eventAttrsMap":{"event_id":"onresume","event_time":1555603196691},"time":1555603196691,"xpEventId":"onresume"}')
xpTest.testStr('{"attrsMap":{},"eventAttrsMap":{"event_id":"onpause","event_time":1555603228001},"time":1555603228001,"xpEventId":"onpause"}')
xpTest.testStr('{"attrsMap":{"template":"operation","country":"CN","card_position":0,"pull_type":2,"length":6,"default_status":2,"position":2,"bottom_index":2,"is_init_status":0},"eventAttrsMap":{"scroll_distance":248,"xp_id":"xpanel_scroll","xp_time":1555487593516},"time":1555487593516,"xpEventId":"xpanel_scroll"}')