import React, { Component } from 'react'
import { Link } from 'react-router'
import './Drawer.css'

export default class Drawer extends Component {
  render() {
    return (
      <div className="demo-drawer mdl-layout__drawer mdl-color--blue-grey-900 mdl-color-text--blue-grey-50">
        <nav className="demo-navigation mdl-navigation mdl-color--blue-grey-800">
          <Link to='/dashboard' className="mdl-navigation__link"><i className="mdl-color-text--blue-grey-400 material-icons" role="presentation">home</i>Dashboard</Link>
          <Link to='/speakers' className="mdl-navigation__link"><i className="mdl-color-text--blue-grey-400 material-icons" role="presentation">people</i>Speakers</Link>
          <Link to='/sessions' className="mdl-navigation__link"><i className="mdl-color-text--blue-grey-400 material-icons" role="presentation">tv</i>Sessions</Link>
        </nav>
      </div>
    )
  }
}
