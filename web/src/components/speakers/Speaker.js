import React, { Component } from 'react'
import { Link } from 'react-router'
import { db } from '../../config/constants'
import './Speaker.css'

class ValueInput extends Component {
  render() {
    return (
      <div className="mdl-textfield mdl-js-textfield">
        <input className="mdl-textfield__input" type="text" id={"input-" + this.props.id} value={this.props.value} onChange={(e) => {
          this.props.onChange(e)
        }}/>
      </div>
    )
  }
}

export default class Speaker extends Component {

  state = {
    speaker: null,
    editing: false
  }

  componentDidMount() {
    this.ref = db.child('speakers').child(this.props.params.speakerId)
    this.ref.on('value', snapshot => {
          this.setState({
            speaker: snapshot.val(),
            editing: this.state.editing
          })
        })
  }

  componentWillUnmount() {
    this.ref.off()
  }

  edit() {
    this.setState({
      speaker: this.state.speaker,
      editing: true
    })
  }

  render() {
    if (this.state.speaker) {
      return (
        <div className="Speaker mdl-cell">
          <div className="header mdl-grid mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--4-col-phone">
            <h3 className="mdl-cell mdl-cell--8-col mdl-cell--6-col-tablet mdl-cell--4-col-phone">{this.state.speaker.name}</h3>
            {!this.state.editing &&
              <div className="buttons mdl-cell mdl-cell--2-col mdl-cell--4-col-phone">
                <button className="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--colored" onClick={() => this.edit()}>Edit</button>
              </div>
            }
            {this.state.editing &&
              <div className="buttons mdl-cell mdl-cell--2-col mdl-cell--4-col-phone">
                <button className="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect" onClick={() => this.cancel()}>Cancel</button>
                <button className="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--colored" onClick={() => this.save()}>Save</button>
              </div>
            }
          </div>
          <form action="#">
            <table className="table mdl-cell--12-col mdl-data-table mdl-js-data-table">
              <tbody>
                <tr>
                  <td className="label">Name</td>
                  <td className="mdl-data-table__cell--non-numeric">
                    {!this.state.editing && this.state.speaker.name}
                    {this.state.editing &&
                      <ValueInput id="name" placeholder="Name..." value={this.state.speaker.name} onChange={(e) => {
                        let speaker = this.state.speaker
                        speaker.name = e.target.value
                        this.setState({speaker: speaker})
                      }}/>
                    }
                  </td>
                </tr>
                <tr>
                  <td className="label">Company</td>
                  <td className="mdl-data-table__cell--non-numeric">
                  {!this.state.editing &&
                    <p>{this.state.speaker.company}</p>
                  }
                  {this.state.editing &&
                    <ValueInput id="company" placeholder="Company..." value={this.state.speaker.company} onChange={(e) => {
                        let speaker = this.state.speaker
                        speaker.company = e.target.value
                        this.setState({speaker: speaker})
                    }}/>
                  }
                  </td>
                </tr>
                <tr>
                  <td className="label">Title</td>
                  <td className="mdl-data-table__cell--non-numeric">
                  {!this.state.editing &&
                    <p>{this.state.speaker.title}</p>
                  }
                  {this.state.editing &&
                    <ValueInput id="title" placeholder="Title..." value={this.state.speaker.title} onChange={(e) => {
                        let speaker = this.state.speaker
                        speaker.title = e.target.value
                        this.setState({speaker: speaker})
                    }}/>
                  }
                  </td>
                </tr>
                <tr>
                  <td className="label">Bio</td>
                  <td className="mdl-data-table__cell--non-numeric">
                  {!this.state.editing && this.state.speaker.bio}
                  {this.state.editing &&
                    <ValueInput id="bio" placeholder="Bio..." value={this.state.speaker.bio} onChange={(e) => {
                        let speaker = this.state.speaker
                        speaker.bio = e.target.value
                        this.setState({speaker: speaker})
                    }}/>
                  }
                  </td>
                </tr>
                <tr>
                  <td className="label">Twitter</td>
                  <td className="mdl-data-table__cell--non-numeric">
                  {!this.state.editing &&
                    <p>{this.state.speaker.twitter}</p>
                  }
                  {this.state.editing &&
                    <ValueInput id="twitter" placeholder="Twitter..." value={this.state.speaker.twitter} onChange={(e) => {
                        let speaker = this.state.speaker
                        speaker.twitter = e.target.value
                        this.setState({speaker: speaker})
                    }}/>
                  }
                  </td>
                </tr>
                <tr>
                  <td className="label">Github</td>
                  <td className="mdl-data-table__cell--non-numeric">
                  {!this.state.editing &&
                    <p>{this.state.speaker.github}</p>
                  }
                  {this.state.editing &&
                    <ValueInput id="github" placeholder="Github..." value={this.state.speaker.github} onChange={(e) => {
                        let speaker = this.state.speaker
                        speaker.github = e.target.value
                        this.setState({speaker: speaker})
                    }}/>
                  }
                  </td>
                </tr>
                <tr>
                  <td className="label">Google</td>
                  <td className="mdl-data-table__cell--non-numeric">
                  {!this.state.editing &&
                    <p>{this.state.speaker.google}</p>
                  }
                  {this.state.editing &&
                    <ValueInput id="google" placeholder="Google..." value={this.state.speaker.google} onChange={(e) => {
                        let speaker = this.state.speaker
                        speaker.google = e.target.value
                        this.setState({speaker: speaker})
                    }}/>
                  }
                  </td>
                </tr>
                <tr>
                  <td className="label">Website</td>
                  <td className="mdl-data-table__cell--non-numeric">
                  {!this.state.editing &&
                    <p>{this.state.speaker.website}</p>
                  }
                  {this.state.editing &&
                    <ValueInput id="website" placeholder="Website..." value={this.state.speaker.website} onChange={(e) => {
                        let speaker = this.state.speaker
                        speaker.website = e.target.value
                        this.setState({speaker: speaker})
                    }}/>
                  }
                  </td>
                </tr>
              </tbody>
            </table>
          </form>
        </div>
      )
    } else {
      return <h1>Loading...</h1>
    }
  }

}
