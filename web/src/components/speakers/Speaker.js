import React, { Component } from 'react'
import { db } from '../../config/constants'

export default class Speaker extends Component {

  state = {
    user: null
  }

  componentDidMount() {
    this.ref = db.child('speakers').child(this.props.params.speakerId)
    this.ref.on('value', snapshot => {
          this.setState({
            user: snapshot.val()
          })
        })
  }

  componentWillUnmount() {
    this.ref.off()
  }

  render() {
    if (this.state.user) {
      return (
        <div className="Speaker">
          <h1>{this.state.user.name}</h1>
        </div>
      )
    } else {
      return <h1>Loading...</h1>
    }
  }

}
