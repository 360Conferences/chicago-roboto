import React from 'react';
import { render } from 'react-dom'
import App from './components'
import 'react-mdl/extra/material.css';
import 'react-mdl/extra/material.js';
import injectTapEventPlugin from 'react-tap-event-plugin';

injectTapEventPlugin();

render((
  <App />
), document.getElementById('root'))
