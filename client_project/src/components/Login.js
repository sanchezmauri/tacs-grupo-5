import React from 'react';
import { connect } from 'react-redux'

import { login } from '../actions/auth'

class Login extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            email: '',
            password: ''
        };

        this.onSubmit = this.onSubmit.bind(this);
    }

    onSubmit(submissionEvent) {
        submissionEvent.preventDefault();

        this.props.login(
            this.state.email,
            this.state.password
        )
    }

    render() {
        return (
            <form onSubmit={this.onSubmit}>
                <div>
                    <label>Email:</label>
                    <input
                        type="text"
                        value={this.state.email}
                        onChange={change => this.setState({email: change.target.value})}
                    />
                </div>

                <div>
                    <label>Password:</label>
                    <input
                        type="text"
                        value={this.state.password}
                        onChange={change => this.setState({password: change.target.value})}
                    />
                </div>

                <div>
                    <input type="submit" value="Submit" />
                </div>
            </form>
        );
    }
}

export default connect(
    null,
    { login }
)(Login);