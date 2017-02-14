import React, { Component } from 'react'
import { Link } from 'react-router'
import { db } from '../../config/constants'
import './SpeakersList.css'

export default class SpeakersList extends Component {
  state = {
    speakers: []
  }

  componentDidMount() {
    this.ref = db.child('speakers')
    this.ref.on('value', snapshot => {
      this.setState({speakers:Object.values(snapshot.val())})
    })
  }

  componentWillUnmount() {
    this.ref.off()
  }

  viewSpeaker(id) {
    this.props.router.push('/speakers/' + id)
  }

  render() {
    return (
      <div className="SpeakersList">
        <h1>Speakers</h1>
        <table className="mdl-data-table mdl-js-data-table mdl-data-table--selectable mdl-shadow--2dp">
          <thead>
            <tr>
              <th className="mdl-data-table__cell--non-numeric">Name</th>
              <th className="mdl-data-table__cell--non-numeric">Bio</th>
              <th className="mdl-data-table__cell--non-numeric">Twitter</th>
              <th className="mdl-data-table__cell--non-numeric">Edit</th>
            </tr>
          </thead>
          <tbody>
            {this.state.speakers.map((speaker) =>
              <tr key={speaker.id} onClick={() => this.viewSpeaker(speaker.id)}>
                <td className="mdl-data-table__cell--non-numeric">{speaker.name}</td>
                <td className="mdl-data-table__cell--non-numeric">{speaker.bio.substr(0, 50) + '...'}</td>
                <td className="mdl-data-table__cell--non-numeric">{speaker.twitter}</td>
                <td className="mdl-data-table__cell--non-numeric"><Link to={'/speaker/' + speaker.id}><i className="mdl-color-text--blue-grey-400 material-icons" role="presentation">edit</i></Link></td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    )
  }
}
