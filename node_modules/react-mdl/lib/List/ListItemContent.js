'use strict';

Object.defineProperty(exports, "__esModule", {
    value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _react = require('react');

var _react2 = _interopRequireDefault(_react);

var _classnames = require('classnames');

var _classnames2 = _interopRequireDefault(_classnames);

var _Icon = require('../Icon');

var _Icon2 = _interopRequireDefault(_Icon);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _objectWithoutProperties(obj, keys) { var target = {}; for (var i in obj) { if (keys.indexOf(i) >= 0) continue; if (!Object.prototype.hasOwnProperty.call(obj, i)) continue; target[i] = obj[i]; } return target; }

var propTypes = {
    avatar: _react.PropTypes.oneOfType([_react.PropTypes.string, _react.PropTypes.element]),
    children: _react.PropTypes.node,
    className: _react.PropTypes.string,
    icon: _react.PropTypes.oneOfType([_react.PropTypes.string, _react.PropTypes.element]),
    subtitle: _react.PropTypes.node,
    useBodyClass: _react.PropTypes.bool
};

function createIcon(type, icon) {
    if (typeof icon === 'string') {
        return _react2.default.createElement(_Icon2.default, { className: 'mdl-list__item-' + type, name: icon });
    }
    return _react2.default.cloneElement(icon, { className: 'mdl-list__item-' + type });
}

var ListItemContent = function ListItemContent(props) {
    var avatar = props.avatar,
        children = props.children,
        className = props.className,
        icon = props.icon,
        subtitle = props.subtitle,
        useBodyClass = props.useBodyClass,
        otherProps = _objectWithoutProperties(props, ['avatar', 'children', 'className', 'icon', 'subtitle', 'useBodyClass']);

    var classes = (0, _classnames2.default)('mdl-list__item-primary-content', className);
    var subtitleClassName = useBodyClass ? 'mdl-list__item-text-body' : 'mdl-list__item-sub-title';

    var iconElement = null;
    if (icon) {
        iconElement = createIcon('icon', icon);
    } else if (avatar) {
        iconElement = createIcon('avatar', avatar);
    }

    return _react2.default.createElement(
        'span',
        _extends({ className: classes }, otherProps),
        iconElement,
        _react2.default.createElement(
            'span',
            null,
            children
        ),
        subtitle && _react2.default.createElement(
            'span',
            { className: subtitleClassName },
            subtitle
        )
    );
};

ListItemContent.propTypes = propTypes;

exports.default = ListItemContent;