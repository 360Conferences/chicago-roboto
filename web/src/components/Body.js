import React, { Component } from 'react'
import { auth } from '../config/constants'
import Header from './Header'
import Drawer from './Drawer'

export default class Body extends Component {
    state = {
      authed: false
    }

    componentDidMount() {
      this.removeListener = auth.onAuthStateChanged((user) => {
        if (user) {
          this.setState({
            authed: true
          })
        } else {
          this.setState({
            authed: false
          })
        }
      })
    }

    componentWillUnmount() {
      this.removeListener()
    }

    render() {
      return (
        <div className="demo-layout mdl-layout mdl-js-layout mdl-layout--fixed-header">
          <Header authed={this.state.authed} />
          <main className="mdl-layout__content mdl-layout--fixed-drawer mdl-color--gray-100">
            <Drawer />
            <div className="mdl-cell mdl-cell--12-col mdl-grid">
              {this.props.children}
            </div>
          </main>
        </div>
      )
    }
}
