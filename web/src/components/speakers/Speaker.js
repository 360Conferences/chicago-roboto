import React, { Component } from 'react'
import { browserHistory } from 'react-router'
import { db } from '../../config/constants'
import './Speaker.css'

export default class Speaker extends Component {

  state = {
    speaker: null
  }

  constructor() {
    super()
    this.edit = this.edit.bind(this)
  }

  componentDidMount() {
    this.ref = db.child('speakers').child(this.props.params.speakerId)
    this.ref.on('value', snapshot => {
          this.setState({
            speaker: snapshot.val()
          })
        })
  }

  componentWillUnmount() {
    this.ref.off()
  }

  edit() {
    browserHistory.push('/speakers/' + this.state.speaker.id + '/edit')
  }

  render() {
    if (this.state.speaker) {
      return (
        <div className="Speaker mdl-cell mdl-cell--12-col">
          <h3>{this.state.speaker.name}</h3>
          <span className="company">{this.state.speaker.company}</span> <span className="title">{this.state.speaker.title}</span>

          <h5>Social</h5>
          <ul className="social mdl-list">
            <li className="mdl-list__item">
              <span className="mdl-list__item-primary-content">
                <img src="/images/social-logo-twitter.svg" role="presentation"/>
                {this.state.speaker.twitter || "None"}
              </span>
            </li>
            <li className="mdl-list__item">
              <span className="mdl-list__item-primary-content">
                <img src="/images/social-logo-google.svg" role="presentation"/>
                {this.state.speaker.google || "None"}
              </span>
            </li>
            <li className="mdl-list__item">
              <span className="mdl-list__item-primary-content">
                <img src="/images/social-logo-github.svg" role="presentation"/>
                {this.state.speaker.github || "None"}
              </span>
            </li>
            <li className="mdl-list__item">
              <span className="mdl-list__item-primary-content">
                <img src="/images/social-logo-website.svg" role="presentation"/>
                {this.state.speaker.website || "None"}
              </span>
            </li>
          </ul>

          <p>{this.state.speaker.bio}</p>

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
