import React from 'react';
import { connect } from 'react-redux'

import { login } from '../../actions/auth';
import UserForm from './UserForm';
import { CREATE_USER } from '../../routes/paths';

class Login extends React.Component {
    render() {
        return (
            <UserForm
                title="Logueate a las listas"
                fields={['email', 'password']}
                buttonText="Login"
                submit={this.props.login}
                alternativeLink={{
                    pre: 'No tenés user?',
                    link: CREATE_USER,
                    message: 'Create un usuario'
                }}
            />
        );
    }
}

export default connect(
    null,
    { login }
)(Login);