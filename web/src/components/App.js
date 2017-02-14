import React, { Component } from 'react'
import { auth } from '../config/constants'
import Header from './Header'
import Drawer from './Drawer'

export default class App extends Component {
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
          <div className="mdl-layout__content mdl-layout--fixed-drawer">
            <Drawer />
            <main className="content-grid mdl-grid mdl-layout__content mdl-color--gray-100">
                {this.props.children}
            </main>
          </div>
        </div>
      )
    }
}
