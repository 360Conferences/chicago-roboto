import React, { Component } from 'react'
import { Card, CardTitle, CardText, CardActions, Button, Grid, Cell,
  Dialog, DialogTitle, DialogContent, DialogActions } from 'react-mdl'
import { SelectField, Option } from 'react-mdl-extra'
import { db } from '../../config/constants'
import './DataSchedule.css'
import NewSlotDialog from './NewSlotDialog'
import EditSlotDialog from './EditSlotDialog'

class SessionCard extends Component {
  render() {
    return (
      <Cell col={this.props.col}>
        <Card className="session" style={{backgroundColor: this.props.color}}>
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
        <Card className="session blank" style={{backgroundColor: "#e1e1e1"}}>
          <CardTitle>&nbsp;</CardTitle>
          <CardText>{this.props.roomName}</CardText>
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
    date: "",
    schedule: {slots:{}},
    sessions: [],
    rooms: {},

    originalSession: {},
    updatedSessionId: {},
    editSlot: false,
    editDate: null,
    editRoom: null,
    editSlotId: null,
    editStartTime: null,
    editEndTime: null,
    createNewSlot: false
  }

  colors = ['#89c541', '#fcc102']

  constructor() {
    super()
    this.renderSlot = this.renderSlot.bind(this)
    this.chooseSession = this.chooseSession.bind(this)
    this.updateSession = this.updateSession.bind(this)
    this.cancelChooseSession = this.cancelChooseSession.bind(this)
    this.handleDialogSelectionChange = this.handleDialogSelectionChange.bind(this)
    this.newSlot = this.newSlot.bind(this)
  }

  componentWillReceiveProps(props) {
    let state = this.state
    state.date = props.date
    this.setState(state)

    this.roomsRef = db.child('rooms')
    this.roomsRef.on('value', (snapshot) => {
      let state = this.state
      state.rooms = Object.values(snapshot.val())
      this.setState(state)
    })
    this.scheduleRef = db.child('schedule').child(props.date)
    this.scheduleRef.on('value', (snapshot) => {
      let state = this.state
      state.schedule = snapshot.val()
      this.setState(state)
    })
    this.sessionsRef = db.child('sessions')
    this.sessionsRef.on('value', (snapshot) => {
      let state = this.state
      state.sessions = snapshot.val()
      this.setState(state)
    })
  }

  componentWillUnmount() {
    this.scheduleRef.off()
    this.sessionsRef.off()
  }

  formatTime(date) {
    let hours = (date.getHours() + 1) % 12
    let min = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()
    let ampm = date.getHours() < 12 ? 'AM' : 'PM'
    return hours + ':' + min + ' ' + ampm
  }

  chooseSession = (slot) => {
    let state = this.state
    state.editDate = slot.date
    state.editRoom = slot.room
    state.editSlotId = slot.slotId
    state.editStartTime = slot.startTime
    state.editEndTime = slot.endTime
    state.editSlot = true
    this.setState(state)
  }

  updateSession() {
    let state = this.state
    if (state.updatedSessionId !== state.originalSession) {
      this.dateRef.child(state.originalSession.id).remove()

      let session = this.state.sessions[state.updatedSessionId]
      session.date = (state.date.getMonth() + 1) + "-" + state.date.getDate() + "-" + state.date.getFullYear()
      session.start_time = state.originalSession.start_time
      session.end_time = state.originalSession.end_time
      session.room = state.originalSession.room

      this.dateRef.child(state.updatedSessionId).set(session)
    }

    state.originalSession = {}
    state.updatedSessionId = ""
    state.editSlot = false
    this.setState(state)
  }

  cancelChooseSession() {
    let state = this.state
    state.originalSession = {}
    state.updatedSessionId = ""
    state.editSlot = false
    this.setState(state)
  }

  newSlot() {
    let state = this.state
    state.createNewSlot = true
    this.setState(state)
  }

  renderSlot(slot) {
    const rooms = this.state.rooms
    const items = rooms.map((room) => {
      let sessions = slot.sessions || {}
      let session = sessions[room.id]
      if (session) {
        return (<SessionCard
          key={session.id}
          col={12 / rooms.length}
          color={this.colors[rooms.indexOf(room)]}
          session={session}
          onEdit={(e) => this.chooseSession(session)} />)
      } else {
        return (
          <BlankCard
            key={slot.name + room.name}
            col={12 / rooms.length}
            roomName={room.name}
            onEdit={(e) => this.chooseSession({
              date: slot.date,
              room: room,
              slotId: slot.id,
              startTime: slot.start_time.toString(),
              endTime: slot.end_time.toString(),
            })}
            />
          )
      }
    })
    return (
      <Grid key={slot.name} className="time-slot">
        <Cell col={2}>
          <div style={{padding: '8px'}}>{slot.name}</div></Cell>
        <Cell col={8}>
          <Grid>{items}</Grid>
        </Cell>
      </Grid>
    )
  }

  handleDialogSelectionChange = (e) => {
    let state = this.state
    state.updatedSessionId = e.target.value
    this.setState(state)
  }

  render() {
    if (!this.state.schedule || !this.state.sessions) {
      return (<h1>Loading...</h1>)
    }

    const slots = Object.values(this.state.schedule.slots).map((slot) => this.renderSlot(slot))
    const availableSessions = Object.keys(this.state.sessions).map((sessionId) =>
      <option key={sessionId} value={sessionId}>{this.state.sessions[sessionId].name}</option>
    )
    return (
      <div>
        <Grid className="grid">
          <Cell col={12}>
            {slots}
          </Cell>
          <Cell col={12}>
            <Card>
              <CardActions>
                <Button colored onClick={this.newSlot}>New Slot</Button>
              </CardActions>
            </Card>
          </Cell>
        </Grid>
        <EditSlotDialog
            date={this.state.editDate}
            room={this.state.editRoom}
            slotId={this.state.editSlotId}
            startTime={this.state.editStartTime}
            endTime={this.state.editEndTime}
            open={this.state.editSlot}
            onClose={() => {
              let state = this.state
              state.editDate = null
              state.editRoom = null
              state.editSlotId = null
              state.editStartTime = null
              state.editEndTime = null
              state.editSlot = false
              this.setState(state)
            }}
            />
        <NewSlotDialog date={this.state.date} open={this.state.createNewSlot} onClose={() => {
          let state = this.state
          state.createNewSlot = false
          this.setState(state)
        }} />
      </div>
    )
  }
}
