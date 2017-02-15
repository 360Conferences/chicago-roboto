import React, { Component, } from 'react'
import { browserHistory } from 'react-router'
import { db } from '../../config/constants'
import MDLComponent from '../../helpers/MDLComponent'

export default class EditSpeaker extends Component {

  state = {
    speaker: null
  }

  constructor() {
    super()
    this.save = this.save.bind(this)
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

  save() {
    this.ref.set(this.state.speaker)
        .then(() => browserHistory.goBack())
  }

  render() {
    if (this.state.speaker) {
      return (
        <div className="Speaker mdl-layout__content">
          <form action="#">
            <div>
              <button className="mdl-button mdl-js-button mdl-button--raised" onClick={this.save}>Save</button>
            </div>
            <ul className="mdl-list">
              <li className="mdl-list__item">
                <span className="mdl-list__item-primary-content">
                  <MDLComponent>
                    <div className="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                      <input
                        className="mdl-textfield__input"
                        type="text"
                        id="input-name"
                        ref={(c) => (this.inputRef = c)}
                        value={this.state.speaker.name} onChange={(e) => {
                            let speaker = this.state.speaker
                            speaker.name = e.target.value
                            this.setState({
                              speaker: speaker
                            })
                        }}/>
                      <label className="mdl-textfield__label" htmlFor="input-name">Name</label>
                    </div>
                  </MDLComponent>
                </span>
              </li>
              <li className="mdl-list__item">
                <span className="mdl-list__item-primary-content">
                  <MDLComponent>
                    <div className="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                      <input className="mdl-textfield__input" type="text" id="input-company" value={this.state.speaker.company} onChange={(e) => {
                        let speaker = this.state.speaker
                        speaker.company = e.target.value
                        this.setState({
                          speaker: speaker
                        })
                      }}/>
                      <label className="mdl-textfield__label" htmlFor="input-company">Company</label>
                    </div>
                  </MDLComponent>
                </span>
              </li>
              <li className="mdl-list__item">
                <span className="mdl-list__item-primary-content">
                  <MDLComponent>
                    <div className="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                      <input className="mdl-textfield__input" type="text" id="input-title" value={this.state.speaker.title} onChange={(e) => {
                        let speaker = this.state.speaker
                        speaker.title = e.target.value
                        this.setState({
                          speaker: speaker
                        })
                      }}/>
                      <label className="mdl-textfield__label" htmlFor="input-title">Title</label>
                    </div>
                  </MDLComponent>
                </span>
              </li>
              <li className="mdl-list__item">
                <span className="mdl-list__item-primary-content">
                  <MDLComponent>
                    <div className="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                      <input className="mdl-textfield__input" type="text" id="input-twitter" value={this.state.speaker.twitter} onChange={(e) => {
                        let speaker = this.state.speaker
                        speaker.twitter = e.target.value
                        this.setState({
                          speaker: speaker
                        })
                      }}/>
                      <label className="mdl-textfield__label" htmlFor="input-twitter">Twitter</label>
                    </div>
                  </MDLComponent>
                </span>
              </li>
              <li className="mdl-list__item">
                <span className="mdl-list__item-primary-content">
                  <MDLComponent>
                    <div className="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                      <input className="mdl-textfield__input" type="text" id="input-google" value={this.state.speaker.google} onChange={(e) => {
                        let speaker = this.state.speaker
                        speaker.google = e.target.value
                        this.setState({
                          speaker: speaker
                        })
                      }}/>
                      <label className="mdl-textfield__label" htmlFor="input-google">Google</label>
                    </div>
                  </MDLComponent>
                </span>
              </li>
              <li className="mdl-list__item">
                <span className="mdl-list__item-primary-content">
                  <MDLComponent>
                    <div className="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                      <input className="mdl-textfield__input" type="text" id="input-github" value={this.state.speaker.github} onChange={(e) => {
                        let speaker = this.state.speaker
                        speaker.github = e.target.value
                        this.setState({
                          speaker: speaker
                        })
                      }}/>
                      <label className="mdl-textfield__label" htmlFor="input-github">Github</label>
                    </div>
                  </MDLComponent>
                </span>
              </li>
              <li className="mdl-list__item">
                <span className="mdl-list__item-primary-content">
                  <MDLComponent>
                    <div className="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                      <input className="mdl-textfield__input" type="text" id="input-website" value={this.state.speaker.website} onChange={(e) => {
                        let speaker = this.state.speaker
                        speaker.website = e.target.value
                        this.setState({
                          speaker: speaker
                        })
                      }}/>
                      <label className="mdl-textfield__label" htmlFor="input-website">Website</label>
                    </div>
                  </MDLComponent>
                </span>
              </li>
              <li className="mdl-list__item">
                <span className="mdl-list__item-primary-content">
                  <MDLComponent>
                    <div className="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                      <textarea className="mdl-textfield__input" type="text" id="input-bio" value={this.state.speaker.bio} onChange={(e) => {
                        let speaker = this.state.speaker
                        speaker.bio = e.target.value
                        this.setState({
                          speaker: speaker
                        })
                      }}/>
                      <label className="mdl-textfield__label" htmlFor="input-bio">Bio</label>
                    </div>
                  </MDLComponent>
                </span>
              </li>
            </ul>
          </form>
        </div>
      )
    } else {
      return <h1>Loading...</h1>
    }
  }
}
