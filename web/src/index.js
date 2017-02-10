import React from 'react';
import { render } from 'react-dom';
import { Router, Route, IndexRoute, browserHistory } from 'react-router'
import { auth } from './config/constants'
import Body from './components/Body'
import Home from './components/Home'
import Dashboard from './components/dashboard/Dashboard'

function requireAuth(nextState, replace) {
  if (!auth.currentUser) {
    replace({
      pathname: '/'
    })
  }
}

render((
  <Router history={browserHistory}>
    <Route path='/' component={Body}>
      <IndexRoute component={Home} />
      <Route path='dashboard' component={Dashboard} onEnter={requireAuth}/>
    </Route>
  </Router>
), document.getElementById('root'))
