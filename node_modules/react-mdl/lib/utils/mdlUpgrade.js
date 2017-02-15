'use strict';

Object.defineProperty(exports, "__esModule", {
    value: true
});

var _react = require('react');

var React = _interopRequireWildcard(_react);

var _MDLComponent = require('./MDLComponent');

var _MDLComponent2 = _interopRequireDefault(_MDLComponent);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

function patchComponentClass(Component, recursive) {
    var oldRender = Component.prototype.render;

    Component.prototype.render = function render() {
        // eslint-disable-line no-param-reassign
        return React.createElement(
            _MDLComponent2.default,
            { recursive: recursive },
            oldRender.call(this)
        );
    };

    return Component;
}

function patchSFC(component, recursive) {
    var patchedComponent = function patchedComponent(props) {
        return React.createElement(
            _MDLComponent2.default,
            { recursive: recursive },
            component(props)
        );
    };

    // Attempt to change the function name for easier debugging, but don't die
    // if the browser doesn't support it
    try {
        Object.defineProperty(patchedComponent, 'name', {
            value: component.name
        });
    } catch (e) {} // eslint-disable-line no-empty

    return patchedComponent;
}

exports.default = function (Component) {
    var recursive = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;
    return Component.prototype && Component.prototype.isReactComponent ? patchComponentClass(Component, recursive) : patchSFC(Component, recursive);
};