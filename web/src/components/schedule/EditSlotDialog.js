import React, { Component } from 'react'
import injectTapEventPlugin from 'react-tap-event-plugin';
import Dialog from 'material-ui/Dialog'
import TextField from 'material-ui/TextField'
import FlatButton from 'material-ui/FlatButton'
import SelectField from 'material-ui/SelectField'
import MenuItem from 'material-ui/MenuItem'
import { db } from '../../config/constants'

export default class EditSlotDialog extends Component {

  state = {
    date: null,
    room: null,
    startTime: null,
    endTime: null,
    slotId: null,
    open: false,
    valid: false,
    onClose: () => {},
    sessions: [],
    sessionId: null
  }

  formatTime(date) {
    let hours = date.getHours() % 12
    let min = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()
    let ampm = date.getHours() < 12 ? 'AM' : 'PM'
    return hours + ':' + min + ' ' + ampm
  }

  constructor() {
    super()
    this.onNameChange = this.onNameChange.bind(this)
    this.handleCreate = this.handleCreate.bind(this)
  }

  componentWillReceiveProps(props) {
    let state = this.state
    state.open = props.open
    state.onClose = props.onClose
    state.date = props.date
    state.room = props.room
    state.slotId = props.slotId
    state.sessionId = props.sessionId
    state.startTime = props.startTime
    state.endTime = props.endTime
    this.sessionRef = db.child('sessions')
    this.sessionRef.on('value', (snapshot) => {
      let state = this.state
      state.sessions = snapshot.val()
      this.setState(state)
    })
    this.setState(state)
  }

  componentWillUnmount() {
    this.sessionRef.off()
  }

  validate = (state) => state.sessionId !== null

  setState(nextState, callback) {
    nextState.valid = this.validate(nextState)
    super.setState(nextState, callback)
  }

  onNameChange(e) {
    let state = this.state
    state.name = e.target.value
    this.setState(state)
  }

  handleCreate = () => {
    let session = this.state.sessions[this.state.sessionId]
    session.slot_id = this.state.slotId
    session.date = this.state.date
    session.room = this.state.room.name
    session.start_time = this.state.startTime
    session.end_time = this.state.endTime

    db.child('sessions').child(session.id).set(session)
    db.child('schedule').child(this.state.date)
        .child('slots').child(this.state.slotId)
        .child('sessions').child(this.state.room.id)
        .set(session, () => this.handleClose())
  }

  handleClose = () => {
    let state = this.state
    state.open = false
    this.setState(state)
    state.onClose()
  }

  handleSessionChange = (event, index, value) => {
    let state = this.state
    state.sessionId = value
    this.setState(state)
  }

  render() {
    const actions = [
      <FlatButton
        label="Cancel"
        primary={true}
        onClick={this.handleClose} />,
      <FlatButton
        label="Update"
        primary={true}
        disabled={!this.state.valid}
        keyboardFocused={true}
        onClick={this.handleCreate} />,
    ]
    return (
      <Dialog
          title="Update Session"
          open={this.state.open}
          actions={actions}
          modal={false}
          onRequestClose={this.handleClose}>

        <SelectField floatingLabelText="Session" value={this.state.sessionId} onChange={this.handleSessionChange}>
          {Object.keys(this.state.sessions).map((sessionId) =>
            <MenuItem key={sessionId} value={sessionId} primaryText={this.state.sessions[sessionId].name}/>
          )}
        </SelectField>
      </Dialog>
    )
  }
}
