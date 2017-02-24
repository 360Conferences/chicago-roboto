import React, { Component } from 'react'
import injectTapEventPlugin from 'react-tap-event-plugin';
import Dialog from 'material-ui/Dialog'
import TimePicker from 'material-ui/TimePicker'
import TextField from 'material-ui/TextField'
import FlatButton from 'material-ui/FlatButton'
import { db } from '../../config/constants'
import { idify } from '../../helpers/TextHelpers'

export default class NewSlotDialog extends Component {

  state = {
    open: false,
    valid: false,
    onClose: () => {},
    name: null,
  }

  constructor() {
    super()
  }

  componentWillReceiveProps(props) {
    let state = this.state
    state.open = props.open
    state.onClose = props.onClose
    this.setState(state)
  }

  validate = (state) => state.name != null

  setState(nextState, callback) {
    nextState.valid = this.validate(nextState)
    super.setState(nextState, callback)
  }

  onNameChange = (e) => {
    let state = this.state
    state.name = e.target.value
    this.setState(state)
  }

  handleCreate = () => {
    db.child('rooms').child(this.state.name).set({
      id: idify(this.state.name),
      name: this.state.name,
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
          title="New Room"
          open={this.state.open}
          actions={actions}
          modal={false}
          onRequestClose={this.handleClose}>
        <TextField floatingLabelText="Name" onChange={this.onNameChange}/>
      </Dialog>
    )
  }
}
