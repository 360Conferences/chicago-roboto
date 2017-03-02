import React, { Component } from 'react'
import { auth } from '../config/constants'
import { Link } from 'react-router'
import AppBar from 'material-ui/AppBar'
import IconMenu from 'material-ui/IconMenu'
import MenuItem from 'material-ui/MenuItem'
import IconButton from 'material-ui/IconButton'
import MoreVertIcon from 'material-ui/svg-icons/navigation/more-vert'
import FlatButton from 'material-ui/FlatButton'
import Drawer from 'material-ui/Drawer'
import Home from 'material-ui/svg-icons/action/home'
import People from 'material-ui/svg-icons/social/people'
import TV from 'material-ui/svg-icons/hardware/tv'
import Event from 'material-ui/svg-icons/action/event'
import { login, logout } from '../helpers/auth'
import './Base.css'

class LoggedIn extends Component {
  render() {
    return (
      <IconMenu
        {...this.props}
        iconButtonElement={<IconButton><MoreVertIcon /></IconButton>}
        targetOrigin={{horizontal: 'right', vertical: 'top'}}
        anchorOrigin={{horizontal: 'right', vertical: 'top'}}>
        <MenuItem primaryText="Sign out" onClick={logout} />
      </IconMenu>
    )
  }
}

class Login extends Component {
  render() {
    return (
      <FlatButton {...this.props} label="Login" onClick={login} />
    )
  }
}

export default class Base extends Component {
    state = {
      user: null,
      drawerOpen: true
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

    handleMenuButtonTouch = (event) => {
      this.updateState({drawerOpen: !this.state.drawerOpen})
    }

    updateState = (newCmpts) => {
      let state = this.state
      Object.assign(state, newCmpts)
      this.setState(state)
    }

    render() {
      return (
        <div className="demo-layout">
          <AppBar
            title="Chicago Roboto"
            onLeftIconButtonTouchTap={this.handleMenuButtonTouch}
            iconElementRight={this.state.user ? <LoggedIn user={this.state.user}/> : <Login />}
          />
          <Drawer docked={false} open={this.state.drawerOpen} onRequestChange={this.handleMenuButtonTouch} className="demo-drawer">
            <MenuItem primaryText="Dashboard" leftIcon={<Home />} containerElement={<Link to="/" />} />
            <MenuItem primaryText="Speakers" leftIcon={<People />} containerElement={<Link to="/speakers" />} />
            <MenuItem primaryText="Sessions" leftIcon={<TV />} containerElement={<Link to="/sessions" />} />
            <MenuItem primaryText="Schedule" leftIcon={<Event />} containerElement={<Link to="/schedule" />} />
          </Drawer>
          <main className="content-grid mdl-grid mdl-layout__content mdl-color--gray-100">
              {this.props.children}
          </main>
        </div>
      )
    }
}
