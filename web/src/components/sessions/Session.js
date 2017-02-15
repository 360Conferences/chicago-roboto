import React, { Component } from 'react'
import { browserHistory } from 'react-router'
import { db } from '../../config/constants'
import './Session.css'

export default class Session extends Component {

  state = {
    session: null
  }

  constructor() {
    super()
    this.edit = this.edit.bind(this)
  }

  componentDidMount() {
    this.ref = db.child('sessions').child(this.props.params.sessionId)
    this.ref.on('value', snapshot => {
          this.setState({
            session: snapshot.val()
          })
        })
  }

  componentWillUnmount() {
    this.ref.off()
  }

  edit() {
    browserHistory.push('/sessions/' + this.state.session.id + '/edit')
  }

  render() {
    if (this.state.session) {
      return (
        <div className="Session mdl-cell mdl-cell--12-col">
          <h3>{this.state.session.name}</h3>

          <div className="speakers">
            {Object.values(this.state.session.speakers).map((speaker) =>
              <span className="mdl-chip mdl-chip--contact">
                <span className="mdl-chip__contact mdl-color--teal mdl-color-text--white">{speaker.substr(0, 1).toUpperCase()}</span>
                <span className="mdl-chip__text">{speaker}</span>
              </span>
            )}
          </div>

          <p className="description">{this.state.session.description}</p>

          <button className="edit-button mdl-button mdl-js-button mdl-button--fab" onClick={this.edit}>
            <i className="material-icons">edit</i>
          </button>
        </div>
      )
    } else {
      return <h1>Loading...</h1>
    }
  }

}
