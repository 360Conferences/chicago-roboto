import React, { Component, } from 'react'
import { browserHistory } from 'react-router'
import { db } from '../../config/constants'
import MDLComponent from '../../helpers/MDLComponent'

export default class EditSession extends Component {

  state = {
    session: null
  }

  constructor() {
    super()
    this.save = this.save.bind(this)
  }

  componentDidMount() {
    this.ref = db.child('sessions').child(this.props.params.sessionId)
    this.ref.on('value', snapshot => {
          this.setState({
            session: snapshot.val()
          })
        })
  }

  componentWillUnmount() {
    this.ref.off()
  }

  save() {
    this.ref.set(this.state.session)
        .then(() => browserHistory.goBack())
  }

  render() {
    if (this.state.session) {
      return (
        <div className="Session mdl-layout__content">
          <form action="#">
            <div>
              <button className="mdl-button mdl-js-button mdl-button--raised" onClick={this.save}>Save</button>
            </div>
            <ul className="mdl-list">
              <li className="mdl-list__item">
                <span className="mdl-list__item-primary-content">
                  <MDLComponent>
                    <div className="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                      <input
                        className="mdl-textfield__input"
                        type="text"
                        id="input-name"
                        ref={(c) => (this.inputRef = c)}
                        value={this.state.session.name} onChange={(e) => {
                            let session = this.state.session
                            session.name = e.target.value
                            this.setState({
                              session: session
                            })
                        }}/>
                      <label className="mdl-textfield__label" htmlFor="input-name">Name</label>
                    </div>
                  </MDLComponent>
                </span>
              </li>
              <li className="mdl-list__item">
                <span className="mdl-list__item-primary-content">
                  <MDLComponent>
                    <div className="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                      <textarea className="mdl-textfield__input" type="text" id="input-description" value={this.state.session.description} onChange={(e) => {
                        let session = this.state.session
                        session.description = e.target.value
                        this.setState({
                          session: session
                        })
                      }}/>
                      <label className="mdl-textfield__label" htmlFor="input-description">Description</label>
                    </div>
                  </MDLComponent>
                </span>
              </li>
            </ul>
          </form>
        </div>
      )
    } else {
      return <h1>Loading...</h1>
    }
  }
}
