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
