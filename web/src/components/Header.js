import React, { Component } from 'react'
import logo from '../logo.svg'
import './Header.css'
import { login, logout } from '../helpers/auth'

export default class Header extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <header className="Header mdl-layout__header mdl-color-text--white mdl-color--light-blue-700">
        <div className="mdl-cell mdl-cell--12-col mdl-cell--12-col-tablet mdl-grid">
          <div className="mdl-layout__header-row mdl-cell mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-desktop">
            <h3><img src={logo} className="Header-logo" alt="logo" /> Chicago Roboto</h3>
          </div>
            {this.props.authed
              ? <div id="user-container">
                  <div id="user-pic"></div>
                  <div id="user-name"></div>
                  <button id="sign-out" className="mdl-button mdl-js-button mdl-js-ripple-effect mdl-color-text--white"
                      onClick={() => {
                        logout()
                      }}>
                    Sign-out
                  </button>
                </div>
              : <div id="user-container">
                  <button id="sign-in" className="mdl-button mdl-js-button mdl-js-ripple-effect mdl-color-text--white"
                      onClick={() => {
                        login()
                      }}>
                    <i className="material-icons">account_circle</i>Sign-in with Google
                  </button>
                </div>}
        </div>
      </header>
    );
  }
}
