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
