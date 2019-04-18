var EventTracker = require('./XPTracker.js');
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
