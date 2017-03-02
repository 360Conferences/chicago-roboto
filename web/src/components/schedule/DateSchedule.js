import React, { Component } from 'react'
import { Card, CardTitle, CardText, CardActions } from 'material-ui/Card'
import FlatButton from 'material-ui/FlatButton'
import { Button, Grid, Cell } from 'react-mdl'
import { db } from '../../config/constants'
import NewSlotDialog from './NewSlotDialog'
import EditSlotDialog from './EditSlotDialog'
import NewRoomDialog from './NewRoomDialog'
import './DateSchedule.css'

class SessionCard extends Component {
  render() {
    return (
      <Cell col={this.props.col}>
        <Card className="session" style={{backgroundColor: this.props.color}}>
          <CardTitle>{this.props.session.name}</CardTitle>
          <CardText>{this.props.session.room}</CardText>
          <CardActions>
            <FlatButton primary={true} onClick={this.props.onEdit}>Update</FlatButton>
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
            <FlatButton primary={true} onClick={this.props.onEdit}>Set</FlatButton>
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
    editDate: null,
    editRoom: null,
    editSlotId: null,
    editSessionId: null,
    editStartTime: null,
    editEndTime: null,
    editSlot: false,
    createNewSlot: false
  }

  colors = ['#89c541', '#fcc102']

  constructor() {
    super()
    this.renderSlot = this.renderSlot.bind(this)
    this.chooseSession = this.chooseSession.bind(this)
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
      let schedule = snapshot.val()
      schedule.slots = schedule.slots || {}
      this.updateState({schedule: schedule})
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

  chooseSession = (slot) => {
    this.updateState({
      editDate: slot.date,
      editRoom: slot.room,
      editSlotId: slot.slotId,
      editSessionId: slot.sessionId,
      editStartTime: slot.startTime,
      editEndTime: slot.endTime,
      editSlot: true,
    })
  }

  newSlot = () => { this.updateState({createNewSlot: true}) }

  newRoom = () => { this.updateState({createNewRoom: true}) }

  updateState = (newCmpts) => {
    let state = this.state
    Object.assign(state, newCmpts)
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
          onEdit={(e) => this.chooseSession({
            date: slot.date,
            room: room,
            slotId: slot.id,
            startTime: slot.start_time.toString(),
            endTime: slot.end_time.toString(),
            sessionId: session.id
          })} />)
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

  render() {
    if (!this.state.schedule || !this.state.sessions) {
      return (<h1>Loading...</h1>)
    }

    const rooms = Object.values(this.state.rooms).map((room) =>
      <Cell key={room.id} col={12 / this.state.rooms.length}>
        <h5 className="room-title">{room.name}</h5>
      </Cell>
    )
    const slots = Object.values(this.state.schedule.slots).map((slot) => this.renderSlot(slot))
    const availableSessions = Object.keys(this.state.sessions).map((sessionId) =>
      <option key={sessionId} value={sessionId}>{this.state.sessions[sessionId].name}</option>
    )
    return (
      <div>
        <Grid className="grid">
          <Cell col={12}>
            <Grid className="rooms">
              <Cell col={2}>
                <FlatButton colored onClick={this.newRoom}>New Room</FlatButton>
              </Cell>
              <Cell col={8}>
                <Grid>
                  {rooms}
                </Grid>
              </Cell>
            </Grid>
            {slots}
          </Cell>
          <Cell col={12}>
            <Card>
              <CardActions>
                <FlatButton colored onClick={this.newSlot}>New Slot</FlatButton>
              </CardActions>
            </Card>
          </Cell>
        </Grid>
        <EditSlotDialog
            date={this.state.editDate}
            room={this.state.editRoom}
            slotId={this.state.editSlotId}
            sessionId={this.state.editSessionId}
            startTime={this.state.editStartTime}
            endTime={this.state.editEndTime}
            open={this.state.editSlot}
            onClose={() => {
              let state = this.state
              state.editDate = null
              state.editRoom = null
              state.editSlotId = null
              state.editSessionId = null
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
        <NewRoomDialog open={this.state.createNewRoom} onClose={() => this.updateState({createNewRoom: false})} />
      </div>
    )
  }
}
