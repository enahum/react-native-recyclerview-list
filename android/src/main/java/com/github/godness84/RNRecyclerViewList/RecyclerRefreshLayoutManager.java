package com.github.godness84.RNRecyclerViewList;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.EventDispatcher;
import java.util.Map;
import javax.annotation.Nullable;
import android.util.Log;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

public class RecyclerRefreshLayoutManager extends ViewGroupManager<RecyclerRefreshLayout> {

    protected static final String REACT_CLASS = "RecyclerRefreshLayout";

    @Override
    protected RecyclerRefreshLayout createViewInstance(ThemedReactContext reactContext) {
        return new RecyclerRefreshLayout(reactContext);
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactProp(name = ViewProps.ENABLED, defaultBoolean = true)
    public void setEnabled(RecyclerRefreshLayout view, boolean enabled) {
        view.setEnabled(enabled);
    }

    @ReactProp(name = "colors", customType = "ColorArray")
    public void setColors(RecyclerRefreshLayout view, @Nullable ReadableArray colors) {
        if (colors != null) {
            int[] colorValues = new int[colors.size()];
            for (int i = 0; i < colors.size(); i++) {
                colorValues[i] = colors.getInt(i);
            }
            view.setColorSchemeColors(colorValues);
        } else {
            view.setColorSchemeColors();
        }
    }

    @ReactProp(name = "progressBackgroundColor", defaultInt = Color.TRANSPARENT, customType = "Color")
    public void setProgressBackgroundColor(RecyclerRefreshLayout view, int color) {
        try {
            Log.i("ReactNative", "the color to be used is " + color);
            view.setProgressBackgroundColor(color);
        } catch(Exception e) {
            Log.i("ReactNative", e.getMessage());
        }
    }

    @ReactProp(name = "size", defaultInt = SwipeRefreshLayout.DEFAULT)
    public void setSize(RecyclerRefreshLayout view, int size) {
        view.setSize(size);
    }

    @ReactProp(name = "direction", customType = "bottom")
    public void setDirection(RecyclerRefreshLayout view, String direction) {
        Log.i("ReactNative", "Set RecyclerRefreshLayout direction to " + direction);
        switch (direction) {
            case "top":
                view.setDirection(SwipyRefreshLayoutDirection.TOP);
                break;
            case "both":
                view.setDirection(SwipyRefreshLayoutDirection.BOTH);
                break;
            default:
                view.setDirection(SwipyRefreshLayoutDirection.BOTTOM);
                break;
        }
    }

    @ReactProp(name = "refreshing")
    public void setRefreshing(RecyclerRefreshLayout view, boolean refreshing) {
        view.setRefreshing(refreshing);
    }

    @Override
    protected void addEventEmitters(
            final ThemedReactContext reactContext,
            final RecyclerRefreshLayout view) {
        view.setOnRefreshListener(
                new SwipyRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh(SwipyRefreshLayoutDirection direction) {
                        reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher()
                                .dispatchEvent(new RefreshEvent(view.getId(), direction));
                    }
                });
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedViewConstants() {
        return MapBuilder.<String, Object>of(
                "SIZE",
                MapBuilder.of("DEFAULT", SwipeRefreshLayout.DEFAULT, "LARGE", SwipeRefreshLayout.LARGE));
    }

    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put("refreshRecyclerView", MapBuilder.of("registrationName", "onRefresh"))
                .build();
    }
}
