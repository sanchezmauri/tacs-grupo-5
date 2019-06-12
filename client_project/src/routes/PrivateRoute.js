import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import { LOGIN } from './paths';

function PrivateRoute({ component: Component, store, allowedRoles, ...rest }) {
    return (
        <Route
            {...rest}
            render={props => {
                const user = store.getState().user;
                const authenticated = user.loggedIn;
                const authorized =  allowedRoles.includes(user.role);

                if (authenticated && authorized)
                    return <Component {...props} />;
                
                else if (authenticated && !authorized)
                    // todo: forbidden
                    return (
                        <Redirect
                            to={{
                                pathname: LOGIN,
                                state: { from: props.location }
                            }}
                        />
                    );
                
                else
                    return (
                        <Redirect
                            to={{
                                pathname: LOGIN, // todo: unauthorized
                                state: { from: props.location }
                            }}
                        />
                    );
            }}
        />
    );
}

export default PrivateRoute;