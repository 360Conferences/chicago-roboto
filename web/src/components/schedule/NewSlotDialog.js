import React, { Component } from 'react'
import injectTapEventPlugin from 'react-tap-event-plugin';
import Dialog from 'material-ui/Dialog'
import TimePicker from 'material-ui/TimePicker'
import TextField from 'material-ui/TextField'
import FlatButton from 'material-ui/FlatButton'
import { db } from '../../config/constants'

export default class NewSlotDialog extends Component {

  state = {
    date: null,
    open: false,
    valid: false,
    onClose: () => {},
    name: null,
    startTime: null,
    endTime: null,
    defaultName: null
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
    this.setState(state)
  }

  validate = (state) => state.date != null && state.startTime != null && state.endTime != null

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
      db.child('schedule').child(this.state.date).child('slots').push({
        name: this.state.name || this.state.defaultName,
        start_time: this.state.startTime.toISOString(),
        end_time: this.state.endTime.toISOString(),
        sessions: {},
      }, () => this.handleClose())
  }

  handleClose = () => {
    let state = this.state
    state.open = false
    this.setState(state)
    state.onClose()
  }

  handleStartTimeChanged = (e, date) => {
    let state = this.state
    state.startTime = date
    this.setState(state)
    this.onTimeUpdated()
  }

  handleEndTimeChanged = (e, date) => {
    let state = this.state
    state.endTime = date
    this.setState(state)
    this.onTimeUpdated()
  }

  onTimeUpdated = () => {
    let state = this.state
    if (state.startTime && state.endTime) {
      state.defaultName = this.formatTime(state.startTime) + ' - ' + this.formatTime(state.endTime)
      this.setState(state)
    }
  }

  render() {
    const actions = [
      <FlatButton
        label="Cancel"
        primary={true}
        onClick={this.handleClose} />,
      <FlatButton
        label="Create"
        primary={true}
        disabled={!this.state.valid}
        keyboardFocused={true}
        onClick={this.handleCreate} />,
    ]
    return (
      <Dialog
          title="New Time Slot"
          open={this.state.open}
          actions={actions}
          modal={false}
          onRequestClose={this.handleClose}>
        <TextField floatingLabelText={this.state.defaultName || "Name (Optional)"} onChange={this.onNameChange}/>
        <TimePicker hintText="Start Time" onChange={this.handleStartTimeChanged}/>
        <TimePicker hintText="End Time" onChange={this.handleEndTimeChanged}/>
      </Dialog>
    )
  }
}
