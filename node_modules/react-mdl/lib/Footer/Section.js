'use strict';

Object.defineProperty(exports, "__esModule", {
    value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _react = require('react');

var _react2 = _interopRequireDefault(_react);

var _classnames = require('classnames');

var _classnames2 = _interopRequireDefault(_classnames);

var _cloneChildren = require('../utils/cloneChildren');

var _cloneChildren2 = _interopRequireDefault(_cloneChildren);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

function _objectWithoutProperties(obj, keys) { var target = {}; for (var i in obj) { if (keys.indexOf(i) >= 0) continue; if (!Object.prototype.hasOwnProperty.call(obj, i)) continue; target[i] = obj[i]; } return target; }

var Section = function Section(props) {
    var className = props.className,
        logo = props.logo,
        size = props.size,
        type = props.type,
        children = props.children,
        otherProps = _objectWithoutProperties(props, ['className', 'logo', 'size', 'type', 'children']);

    var classes = (0, _classnames2.default)(_defineProperty({}, 'mdl-' + size + '-footer__' + type + '-section', true), className);

    return _react2.default.createElement(
        'div',
        _extends({ className: classes }, otherProps),
        logo ? _react2.default.createElement(
            'div',
            { className: 'mdl-logo' },
            logo
        ) : null,
        (0, _cloneChildren2.default)(children, { size: size })
    );
};

Section.propTypes = {
    className: _react.PropTypes.string,
    logo: _react.PropTypes.node,
    size: _react.PropTypes.oneOf(['mini', 'mega']),
    type: _react.PropTypes.oneOf(['top', 'middle', 'bottom', 'left', 'right'])
};
Section.defaultProps = {
    size: 'mega',
    type: 'left'
};

exports.default = Section;