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
