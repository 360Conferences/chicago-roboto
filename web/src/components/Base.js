import React, { Component } from 'react'
import { auth } from '../config/constants'
import { Link } from 'react-router'
import { Layout, Drawer, Header, Navigation } from 'react-mdl'
import { login, logout } from '../helpers/auth'
import './Base.css'

export default class Base extends Component {
    state = {
      user: null
    }

    componentDidMount() {
      this.removeListener = auth.onAuthStateChanged((user) => {
        if (user) {
          this.setState({
            user: user
          })
        } else {
          this.setState({
            user: null
          })
        }
      })
    }

    componentWillUnmount() {
      this.removeListener()
    }

    render() {
      return (
        <Layout className="demo-layout" fixedDrawer fixedHeader>
          <Header title="Chicago Roboto">
            {this.state.user
              ? <div id="user-container">
                  <div id="user-pic" style={{backgroundImage: 'url(' + this.state.user.photoURL + ')'}}></div>
                  <div id="user-name">{this.state.user.displayName}</div>
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
                </div>
            }
          </Header>
          <Drawer className="demo-drawer">
            <Navigation className="demo-navigation mdl-navigation mdl-color--blue-grey-800">
              <Link to='/dashboard' className="mdl-navigation__link"><i className="mdl-color-text--blue-grey-400 material-icons" role="presentation">home</i>Dashboard</Link>
              <Link to='/speakers' className="mdl-navigation__link"><i className="mdl-color-text--blue-grey-400 material-icons" role="presentation">people</i>Speakers</Link>
              <Link to='/sessions' className="mdl-navigation__link"><i className="mdl-color-text--blue-grey-400 material-icons" role="presentation">tv</i>Sessions</Link>
              <Link to='/schedule' className="mdl-navigation__link"><i className="mdl-color-text--blue-grey-400 material-icons" role="presentation">event</i>Schedule</Link>
            </Navigation>
          </Drawer>
            <main className="content-grid mdl-grid mdl-layout__content mdl-color--gray-100">
                {this.props.children}
            </main>
        </Layout>
      )
    }
}
