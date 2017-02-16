import React, { Component } from 'react'
import { Tabs, Tab, DataTable, TableHeader, Icon } from 'react-mdl'
import { db } from '../../config/constants'
import DateSchedule from './DateSchedule'

export default class Schedule extends Component {

  state = {
    activeTab: 0,
    dates: []
  }

  constructor() {
    super()
    this.updateActiveTab = this.updateActiveTab.bind(this)
  }

  componentDidMount() {
    this.datesRef = db.child('session_dates')
    this.datesRef.on('value', (snapshot) => {
      let state = this.state
      state.dates = snapshot.val()
      this.setState(state)
    })
  }

  componentWillUnmount() {
    this.datesRef.off()
  }

  updateActiveTab(tabId) {
    let state = this.state
    state.activeTab = tabId
    this.setState(state)
  }

  render() {
    return (
      <div className="demo-tabs mdl-grid">
        <div className="mdl-cell mdl-cell--12-col">
          <Tabs ripple activeTab={this.state.activeTab} onChange={this.updateActiveTab}>
            {this.state.dates.map((date) => <Tab key={date}>{date}</Tab>)}
            <Tab><Icon name="add" />New</Tab>
          </Tabs>
          <DateSchedule date={this.state.dates[this.state.activeTab]} />
        </div>
      </div>
    )
  }
}
