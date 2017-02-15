import React, { Component } from 'react'
import { browserHistory } from 'react-router'
import { db } from '../../config/constants'
import './Session.css'

class SpeakerChip extends Component {

  state = {
    speaker: null
  }

  componentDidMount() {
    this.ref = db.child('speakers').child(this.props.speakerId)
    this.ref.on('value', snapshot => {
      this.setState({
        speaker: snapshot.val()
      })
    })
  }

  componentWillUnmount() {
    this.ref.off()
  }

  render() {
    if (this.state.speaker) {
      return (
        <span className="mdl-chip mdl-chip--contact">
          <span className="mdl-chip__contact mdl-color--teal mdl-color-text--white">{this.state.speaker.name.substr(0, 1).toUpperCase()}</span>
          <span className="mdl-chip__text">{this.state.speaker.name}</span>
        </span>
      )
    } else {
      return (
        <span className="mdl-chip mdl-chip--contact">
          <span className="mdl-chip__contact mdl-color--teal mdl-color-text--white">L</span>
          <span className="mdl-chip__text">Loading</span>
        </span>
      )
    }
  }
}

export default class Session extends Component {

  state = {
    session: null
  }

  constructor() {
    super()
    this.edit = this.edit.bind(this)
  }

  componentDidMount() {
    this.sessionRef = db.child('sessions').child(this.props.params.sessionId)
    this.sessionRef.on('value', snapshot => {
      this.setState({
        session: snapshot.val()
      })
    })
  }

  componentWillUnmount() {
    this.sessionRef.off()
  }

  edit() {
    browserHistory.push('/sessions/' + this.state.session.id + '/edit')
  }

  render() {
    if (this.state.session) {
      let speakers = this.state.session.speakers || []

      return (
        <div className="Session mdl-cell mdl-cell--12-col">
          <h3>{this.state.session.name}</h3>

          <div className="speakers">
            {Object.values(speakers).map((speaker) =>
              <SpeakerChip speakerId={speaker}/>
            )}
          </div>

          <div className="description">
            {this.state.session.description.split('\n').map(i => <p>{i}</p>)}
          </div>

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
