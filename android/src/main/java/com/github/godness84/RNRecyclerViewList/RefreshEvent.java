package com.github.godness84.RNRecyclerViewList;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

public class RefreshEvent extends Event<RefreshEvent> {
    private String direction;
    protected RefreshEvent(int viewTag, SwipyRefreshLayoutDirection direction) {
        super(viewTag);

        this.direction = (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom");

    }

    @Override
    public String getEventName() {
        return "refreshRecyclerView";
    }

    @Override
    public void dispatch(RCTEventEmitter rctEventEmitter) {
        WritableMap event = Arguments.createMap();
        event.putString("direction", this.direction);
        rctEventEmitter.receiveEvent(getViewTag(), getEventName(), event);
    }
}
