import React, { Component } from 'react'
import { Link } from 'react-router'
import { db } from '../../config/constants'
import './SessionList.css'

export default class SessionList extends Component {
  state = {
    sessions: []
  }

  constructor() {
    super()
    this.viewSession = this.viewSession.bind(this)
  }

  componentDidMount() {
    this.ref = db.child('sessions')
    this.ref.on('value', snapshot => {
      this.setState({sessions:Object.values(snapshot.val())})
    })
  }

  componentWillUnmount() {
    this.ref.off()
  }

  viewSession(id) {
    this.props.router.push('/sessions/' + id)
  }

  render() {
    return (
      <div className="SessionList">
        <h1>Sessions</h1>
        <table className="mdl-data-table mdl-js-data-table mdl-data-table--selectable mdl-shadow--2dp">
          <thead>
            <tr>
              <th className="mdl-data-table__cell--non-numeric">Name</th>
              <th className="mdl-data-table__cell--non-numeric">Room</th>
            </tr>
          </thead>
          <tbody>
            {this.state.sessions.map((session) =>
              <tr key={session.id} onClick={() => this.viewSession(session.id)}>
                <td className="mdl-data-table__cell--non-numeric">{session.name}</td>
                <td className="mdl-data-table__cell--non-numeric">{session.room}</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    )
  }
}
