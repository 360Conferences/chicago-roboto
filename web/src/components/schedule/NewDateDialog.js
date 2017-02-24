import React, { Component } from 'react'
import injectTapEventPlugin from 'react-tap-event-plugin';
import Dialog from 'material-ui/Dialog'
import DatePicker from 'material-ui/DatePicker'
import TextField from 'material-ui/TextField'
import FlatButton from 'material-ui/FlatButton'
import { db } from '../../config/constants'
import { idify } from '../../helpers/TextHelpers'

export default class NewDateDialog extends Component {

  state = {
    open: false,
    valid: false,
    date: null,
    name: "",
    onClose: () => {},
    name: null,
  }

  constructor() {
    super()
  }

  componentWillReceiveProps(props) {
    this.updateState({
      open: props.open,
      onClose: props.onClose,
    })
  }

  validate = (state) => state.date != null

  setState(nextState, callback) {
    nextState.valid = this.validate(nextState)
    super.setState(nextState, callback)
  }

  onNameChange = (e) => { this.updateState({name: e.target.value}) }

  handleCreate = () => {
    let formatted = this.formatDate(this.state.date)
    db.child('schedule').child(formatted).set({date: formatted, name: this.state.name, slots: []})
    db.child('session_dates').push(formatted, () => this.handleClose())
  }

  handleClose = () => {
    this.updateState({open: false})
    this.state.onClose()
  }

  handleChange = (event, date) => {
    this.updateState({date: date})
  }

  handleNameChange = (event, value) => {
    this.updateState({name: value})
  }

  formatDate = (date) => {
    let day = date.getDate()
    let month = date.getMonth() + 1
    let year = date.getFullYear()
    return year + '-' + month + '-' + day
  }

  updateState = (newCmpts) => {
    let state = this.state
    Object.assign(state, newCmpts)
    this.setState(state)
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
        <TextField hintText="Name" value={this.state.name} onChange={this.handleNameChange}/>
        <DatePicker
            hintText="Select Date"
            value={this.state.date}
            formatDate={this.formatDate}
            onChange={this.handleChange} />
      </Dialog>
    )
  }
}
