import React from 'react';
import { render } from 'react-dom';
import { Router, Route, IndexRoute, browserHistory } from 'react-router'
import { auth } from './config/constants'
import App from './components/App'
import Home from './components/Home'
import Dashboard from './components/dashboard/Dashboard'
import SpeakersList from './components/speakers/SpeakersList'
import Speaker from './components/speakers/Speaker'

function requireAuth(nextState, replace) {
  if (!auth.currentUser) {
    replace({
      pathname: '/'
    })
  }
}

render((
  <Router history={browserHistory}>
    <Route path="/" component={App}>
      <IndexRoute component={Home} />
      <Route path="dashboard" component={Dashboard} onEnter={requireAuth}/>
      <Route path="/speaker/:speakerId" component={Speaker} onEnter={requireAuth}/>
      <Route path="speakers" component={SpeakersList} onEnter={requireAuth}/>
    </Route>
  </Router>
), document.getElementById('root'))
