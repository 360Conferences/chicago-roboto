'use strict';

Object.defineProperty(exports, "__esModule", {
    value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _react = require('react');

var _react2 = _interopRequireDefault(_react);

var _classnames = require('classnames');

var _classnames2 = _interopRequireDefault(_classnames);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _objectWithoutProperties(obj, keys) { var target = {}; for (var i in obj) { if (keys.indexOf(i) >= 0) continue; if (!Object.prototype.hasOwnProperty.call(obj, i)) continue; target[i] = obj[i]; } return target; }

var DialogTitle = function DialogTitle(props) {
    var className = props.className,
        component = props.component,
        children = props.children,
        otherProps = _objectWithoutProperties(props, ['className', 'component', 'children']);

    return _react2.default.createElement(component || 'h4', _extends({
        className: (0, _classnames2.default)('mdl-dialog__title', className)
    }, otherProps), children);
};

DialogTitle.propTypes = {
    className: _react.PropTypes.string,
    component: _react.PropTypes.oneOfType([_react.PropTypes.string, _react.PropTypes.element, _react.PropTypes.func])
};

exports.default = DialogTitle;