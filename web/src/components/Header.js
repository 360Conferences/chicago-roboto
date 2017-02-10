import React, { Component } from 'react'
import logo from '../logo.svg'
import './Header.css'
import { login, logout } from '../helpers/auth'
import { auth } from '../config/constants'

export default class Header extends Component {



  render() {
    return (
      <header className="mdl-layout__header mdl-color-text--white mdl-color--light-blue-700">
        <div className="mdl-layout__header-row">
          <img className="mdl-layout-logo" alt="logo" src={logo} />
          <span className="mdl-layout-title">Chicago Roboto</span>

          <div className="mdl-layout-spacer" />

          {this.props.authed
            ? <div id="user-container">
                <div id="user-pic" style={{backgroundImage: 'url(' + auth.currentUser.photoURL + ')'}}></div>
                <div id="user-name">{auth.currentUser.displayName}</div>
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
