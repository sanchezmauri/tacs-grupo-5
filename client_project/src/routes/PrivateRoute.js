import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import { LOGIN } from './paths';

function PrivateRoute({ component: Component, store, ...rest }) {
    return (
        <Route
            {...rest}
            render={props => {
                return (
                    store.getState().user.loggedIn ? (
                        <Component {...props} />
                    ) : (
                        <Redirect
                            to={{
                                pathname: LOGIN,
                                state: { from: props.location }
                            }}
                        />
                    )
                );
            }}
        />
    );
}

export default PrivateRoute;