import React from 'react';
import { connect } from 'react-redux'

import { createUser } from '../../actions/auth';
import UserForm from './UserForm';
import { LOGIN } from '../../routes/paths';

class Login extends React.Component {
    render() {
        return (
            <UserForm
                title="Crear Usuario"
                fields={['name', 'email', 'password']}
                submit={this.props.createUser}
                buttonText="Crear"
                alternativeLink={{
                    pre: 'Ya tenés un usuario?',
                    link: LOGIN,
                    message: 'Login'
                }}
            />
        );
    }
}

export default connect(
    null,
    { createUser }
)(Login);