import React from 'react';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';

import * as paths from '../routes/paths';
import { logout } from '../actions/auth';
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

    render() {
        return (
            <div className="ui secondary menu">
                <Link to={paths.HOME}
                    className="item">
                    Buscar Lugares
                </Link>

                <Link to={paths.LISTS}
                    className="item">
                    Mis Listas
                </Link>
                
                <div className="right menu">
                    {this.renderLogin()}
                </div>
            </div>
        );
    }
}

const mapStateToProps = state => ({
    loggedIn: state.loggedIn
})

export default connect(
    mapStateToProps,
    { logout }
)(Header);