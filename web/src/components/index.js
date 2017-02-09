import React, { Component } from 'react'
import { Router, Route, IndexRoute, Miss, Redirect, hashHistory} from 'react-router'
import { auth } from '../config/constants'
import Header from './Header'
import Home from './Home'
import Dashboard from './dashboard/Dashboard'

function RouteWhenAuthed({component: Component, authed, ...rest}) {
  return (
    <Route
      {...rest}
      render={(props) => authed === true
        ? <Component {...props} />
        : <Redirect to={{pathname: '/login', state: {from: props.location}}} />}
    />
  )
}

function RouteWhenUnauthed({component: Component, authed, ...rest}) {
  return (
    <Route
      {...rest}
      render={(props) => authed === false
        ? <Component {...props} />
        : <Redirect to='/dashboard' />}
    />
  )
}

const Body = React.createClass({
  render() {
    return (
      <div class="demo-layout mdl-layout mdl-js-layout mdl-layout--fixed-drawer mdl-layout--fixed-header">
        <Header authed={this.props.authed} />
        <main className="mdl-layout__content mdl-color--gray-100">
          <div className="mdl-cell mdl-cell--12-col mdl-grid">
            {this.props.children}
          </div>
        </main>
      </div>
    )
  }
})

export default class App extends Component {
  state = {
    authed: false,
    loading: true,
  }

  componentDidMount() {
    this.removeListener = auth.onAuthStateChanged((user) => {
      if (user) {
        this.setState({
          authed: true,
          loading: false,
        })
      } else {
        this.setState({
          loading: false
        })
      }
    })
  }

  componentWillUnmount() {
    this.removeListener()
  }

  render() {
    return (
      <Router history={hashHistory}>
        <Route path="/" component={Body} authed={this.state.authed}>
          <IndexRoute component={Home} />
          <RouteWhenAuthed authed={this.state.authed} pattern='/dashboard' component={Dashboard}/>
        </Route>
      </Router>
    );
  }
}
