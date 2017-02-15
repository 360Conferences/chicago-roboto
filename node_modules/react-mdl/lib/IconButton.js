'use strict';

Object.defineProperty(exports, "__esModule", {
    value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _react = require('react');

var _react2 = _interopRequireDefault(_react);

var _classnames = require('classnames');

var _classnames2 = _interopRequireDefault(_classnames);

var _Button = require('./Button');

var _Button2 = _interopRequireDefault(_Button);

var _Icon = require('./Icon');

var _Icon2 = _interopRequireDefault(_Icon);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _objectWithoutProperties(obj, keys) { var target = {}; for (var i in obj) { if (keys.indexOf(i) >= 0) continue; if (!Object.prototype.hasOwnProperty.call(obj, i)) continue; target[i] = obj[i]; } return target; }

var IconButton = function IconButton(props) {
    var className = props.className,
        name = props.name,
        otherProps = _objectWithoutProperties(props, ['className', 'name']);

    var classes = (0, _classnames2.default)('mdl-button--icon', className);

    return _react2.default.createElement(
        _Button2.default,
        _extends({ className: classes }, otherProps),
        _react2.default.createElement(_Icon2.default, { name: name })
    );
};

IconButton.propTypes = {
    className: _react.PropTypes.string,
    name: _react.PropTypes.string.isRequired
};

exports.default = IconButton;