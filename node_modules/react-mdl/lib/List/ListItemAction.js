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

var propTypes = {
    children: _react.PropTypes.node,
    className: _react.PropTypes.string,
    info: _react.PropTypes.string
};

var ListItemAction = function ListItemAction(props) {
    var children = props.children,
        className = props.className,
        info = props.info,
        otherProps = _objectWithoutProperties(props, ['children', 'className', 'info']);

    var classes = (0, _classnames2.default)('mdl-list__item-secondary-content', className);

    return _react2.default.createElement(
        'span',
        _extends({ className: classes }, otherProps),
        info && _react2.default.createElement(
            'span',
            { className: 'mdl-list__item-secondary-info' },
            info
        ),
        _react2.default.createElement(
            'span',
            { className: 'mdl-list__item-secondary-action' },
            children
        )
    );
};

ListItemAction.propTypes = propTypes;

exports.default = ListItemAction;