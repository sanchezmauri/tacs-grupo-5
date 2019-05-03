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

        return <button onClick={clickAction}>{text}</button>;
    }

    render() {
        return (
            <nav>
                <ul>
                    <li>
                        <Link to={paths.SEARCH}>Buscar Lugares</Link>
                    </li>
                    
                    <li>
                        <Link to={paths.LISTS}>Mis Listas</Link>
                    </li>

                    <li>
                        {this.renderLogin()}
                    </li>
                </ul>
            </nav>
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