import React, { Component } from 'react'
import { Card, CardTitle, CardText, CardActions, Button, Grid, Cell,
  Dialog, DialogTitle, DialogContent, DialogActions } from 'react-mdl'
import { SelectField, Option } from 'react-mdl-extra'
import { db } from '../../config/constants'
import './DataSchedule.css'

class SessionCard extends Component {
  render() {
    return (
      <Cell col={this.props.col}>
        <Card style={{backgroundColor: this.props.color}}>
          <CardTitle>{this.props.session.name}</CardTitle>
          <CardText>{this.props.session.room}</CardText>
          <CardActions>
            <Button colored onClick={this.props.onEdit}>Update</Button>
          </CardActions>
        </Card>
      </Cell>
    )
  }
}

class BlankCard extends Component {
  render() {
    return (
      <Cell col={this.props.col}>
        <Card style={{backgroundColor: "#e1e1e1"}}>
          <CardActions>
            <Button colored onClick={this.props.onEdit}>Set</Button>
          </CardActions>
        </Card>
      </Cell>
    )
  }
}

export default class DateSchedule extends Component {

  state = {
    sessions: [],
    schedule: [],
    rooms: [],
    rows: [],

    originalSession: {},
    updatedSessionId: {},
    editDialog: false
  }

  colors = ['#89c541', '#fcc102']

  constructor() {
    super()
    this.renderRow = this.renderRow.bind(this)
    this.chooseSession = this.chooseSession.bind(this)
    this.updateSession = this.updateSession.bind(this)
    this.cancelChooseSession = this.cancelChooseSession.bind(this)
    this.handleDialogSelectionChange = this.handleDialogSelectionChange.bind(this)
  }

  componentDidMount() {
    this.roomRef = db.child('rooms')
    this.roomRef.on('value', (snapshot) => {
      let state = this.state
      state.rooms = Object.keys(snapshot.val())
      this.setState(state)
    })
    this.sessionsRef = db.child('sessions')
    this.sessionsRef.on('value', (snapshot => {
      let state = this.state
      state.sessions = snapshot.val()
      this.setState(state)
    }))
  }

  formatTime(date) {
    let hours = (date.getHours() + 1) % 12
    let min = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()
    let ampm = date.getHours() < 12 ? 'AM' : 'PM'
    return hours + ':' + min + ' ' + ampm
  }

  componentWillReceiveProps(props) {
    if (props.date) {
      this.dateRef = db.child('sessions_by_date').child(props.date)
      this.dateRef.on('value', (snapshot) => {
        let state = this.state
        let schedule = {}
        state.rows.map((slot) => {
          schedule[slot.slot] = slot
        })

        Object.values(snapshot.val()).map((session) => {
          let start = new Date(session.start_time)
          let end = new Date(session.end_time)
          let slot = this.formatTime(start) + '-' + this.formatTime(end)
          let row = schedule[slot] || {slot: slot, start_time: start, end_time: end}
          row[session.room] = session
          schedule[slot] = row
        })

        state.schedule = schedule
        state.rows = Object.values(schedule)
        this.setState(state)
      })
    }
  }

  componentWillUnmount() {
    this.roomRef.off()
    this.sessionsRef.off()
    this.dataRef.off()
  }

  chooseSession(session) {
    let state = this.state
    state.originalSession = session
    state.updatedSessionId = session.id
    state.editDialog = true
    this.setState(state)
  }

  updateSession() {
  let state = this.state
    if (state.updatedSessionId !== state.originalSession) {
      this.dateRef.child(state.originalSession.id).remove()

      let session = this.state.sessions[state.updatedSessionId]
      session.date = this.props.date
      session.start_time = state.originalSession.start_time
      session.end_time = state.originalSession.end_time
      session.room = state.originalSession.room

      this.dateRef.child(state.updatedSessionId).set(session)
    }

    state.originalSession = {}
    state.updatedSessionId = ""
    state.editDialog = false
    this.setState(state)
  }

  cancelChooseSession() {
    let state = this.state
    state.originalSession = {}
    state.updatedSessionId = ""
    state.editDialog = false
    this.setState(state)
  }

  renderRow(row) {
    const items = this.state.rooms.map((room) => {
      let session = row[room]
      if (session) {
        return (<SessionCard
          key={session.id}
          col={6}
          color={this.colors[this.state.rooms.indexOf(room)]}
          session={session}
          onEdit={(e) => this.chooseSession(session)} />)
      } else {
        return (
          <BlankCard
            key={row.slot + room}
            col={6}
            onEdit={(e) => this.chooseSession({
              room: room,
              start_time: row.end_time.toString(),
              end_time: row.end_time.toString(),
              slot: row.slot
            })}
            />
          )
      }
    })
    return (
      <Grid key={row.slot}>{items}</Grid>
    )
  }

  handleDialogSelectionChange = (e) => {
    let state = this.state
    state.updatedSessionId = e.target.value
    this.setState(state)
  }

  render() {
    const rows = this.state.rows.map((row) => this.renderRow(row))
    const availableSessions = Object.keys(this.state.sessions).map((sessionId) =>
      <option key={sessionId} value={sessionId}>{this.state.sessions[sessionId].name}</option>
    )
    return (
      <div>
        <Grid className="grid">{rows}</Grid>
        <Dialog className="update-dialog" open={this.state.editDialog}>
          <DialogTitle>Update Session</DialogTitle>
          <DialogContent>
            <p>Choose the session for <b>{this.state.originalSession.room}</b> during the <strong>{this.state.originalSession.slot}</strong> time slot.</p>
          </DialogContent>

          <select name="Sessions" value={this.state.updatedSessionId} onChange={this.handleDialogSelectionChange}>
            {availableSessions}
          </select>

          <DialogActions>
            <Button type='button' onClick={this.updateSession}>Update</Button>
            <Button type='button' onClick={this.cancelChooseSession}>Cancel</Button>
          </DialogActions>
        </Dialog>
      </div>
    )
  }
}
