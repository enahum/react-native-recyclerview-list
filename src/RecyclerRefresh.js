// Copyright (c) 2015-present Mattermost, Inc. All Rights Reserved.
// See LICENSE.txt for license information.

import React, { Component } from 'react';
import ReactNative, { View, requireNativeComponent, DeviceEventEmitter, StyleSheet, UIManager, ViewPropTypes } from 'react-native';
import PropTypes from 'prop-types';

const RecyclerRefreshLayout = UIManager.RecyclerRefreshLayout;
const RecyclerRefreshLayoutConsts = RecyclerRefreshLayout.Constants;
const NativeRefreshControl = requireNativeComponent('RecyclerRefreshLayout');

class RefreshControl extends React.Component {
    static SIZE = RecyclerRefreshLayoutConsts.SIZE;

    static defaultProps = {
        direction: 'bottom',
    };

    _nativeRef = null;
    _lastNativeRefreshing = false;

    componentDidMount() {
        this.mounted = true;
        this._lastNativeRefreshing = this.props.refreshing;
    }

    componentDidUpdate(prevProps) {
        // RefreshControl is a controlled component so if the native refreshing
        // value doesn't match the current js refreshing prop update it to
        // the js value.
        if (this.props.refreshing !== prevProps.refreshing) {
            this._lastNativeRefreshing = this.props.refreshing;
        } else if (this.props.refreshing !== this._lastNativeRefreshing && this._nativeRef && this.mounted) {
            this._nativeRef.setNativeProps({
                refreshing: this.props.refreshing,
            });
            this._lastNativeRefreshing = this.props.refreshing;
        }
    }

    componentWillUnmount() {
        this.mounted = false;
    }

    render() {
        return (
            <NativeRefreshControl
                {...this.props}
                ref={ref => {
                    this._nativeRef = ref;
                }}
                onRefresh={this.onRefresh}
            />
        );
    }

    onRefresh = (e) => {
        if (this.mounted) {
            this._lastNativeRefreshing = true;

            this.props.onRefresh && this.props.onRefresh();

            // The native component will start refreshing so force an update to
            // make sure it stays in sync with the js component.
            this.forceUpdate();
        }
    };
}

module.exports = RefreshControl;
