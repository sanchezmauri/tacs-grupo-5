import React from 'react';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';

import * as paths from '../routes/paths';
import { logout } from '../actions/user';
import history from '../routes/history'


class Header extends React.Component {
    renderLogin() {
        let text = '';
        let clickAction = null;

        if (this.props.loggedIn) {
            text = 'log out';
            clickAction = click => this.props.logout();
        } else {
            text = 'log in';
            clickAction = click => history.push(paths.LOGIN);
        }

        return <button className="ui button" onClick={clickAction}>{text}</button>;
    }

    renderNav() {
        return this.props.options.map(option =>
            (<Link key={option.link} to={option.link}
                className="item">
                {option.text}
            </Link>)
        );
    }

    render() {
        return (
            <div className="ui secondary menu">
                {this.renderNav()}
                
                <div className="right menu">
                    {this.renderLogin()}
                </div>
            </div>
        );
    }
}

Header.NONE_OPTIONS = [
    {link: paths.CREATE_USER, text: 'Crear Usuario'},
    {link: paths.LOGIN, text: 'Login'}
];

Header.SYSUSER_OPTIONS = [
    {link: paths.LISTS, text: 'Mis Listas'}
];

Header.ROOT_OPTIONS = [
    {link: paths.SEARCH_USERS, text: 'Inspeccionar Usuarios'},
    {link: paths.VENUE_COUNT, text: 'Lugares'},
];



const mapStateToProps = state => ({
    loggedIn: state.user.loggedIn,
    options: Header[state.user.role + '_OPTIONS']
})

export default connect(
    mapStateToProps,
    { logout }
)(Header);