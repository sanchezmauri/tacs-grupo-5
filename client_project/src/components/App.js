import React from 'react'
// todo: ver como hacer que chequee errores en cuanto cambian y redirigir a login o mostrar forbidden
// import { connect } from 'react-redux';
import { Router, Route } from 'react-router-dom'

import history from '../routes/history'
import * as paths from '../routes/paths'

import PrivateRoute from '../routes/PrivateRoute';
import Header from './Header'
import Login from './user/Login'
import CreateUser from './user/CreateUser'
import ListVenueSearch from './venues/ListVenueSearch'
// import VenueSuggestions from './venues/VenueSuggestions'
import ListsHome from './lists/ListsHome';
import ListHome from './lists/ListHome'
import * as role from '../role';
import UserPage from './admin/UserPage';


const App = props => {
    return (
        // con Router mapeas las rutas a los componentes
        // cuando hacés un Link con una ruta,
        // este router te renderea el componente para la ruta
        // aca no se renderiza nada, nada más se especifican rutas->componentes

        <div className="ui container">
            <Router history={history}>
                <Header />
                
                <Route path={paths.LOGIN} component={Login} />
                <Route path={paths.CREATE_USER} component={CreateUser} />

                <PrivateRoute path={paths.HOME} exact
                    component={ListsHome}
                    store={props.store}
                    allowedRoles={[role.SYSUSER]}
                />
                <PrivateRoute path={paths.LISTS} exact
                    component={ListsHome}
                    store={props.store}
                    allowedRoles={[role.SYSUSER]}
                />
                <PrivateRoute path={paths.LIST} exact
                    component={ListHome}
                    store={props.store}
                    allowedRoles={[role.SYSUSER]}
                />
                <PrivateRoute path={paths.LIST_VENUES_SEARCH}
                    component={ListVenueSearch}
                    store={props.store}
                    allowedRoles={[role.SYSUSER]}
                />


                {/* admin routes */}
                <PrivateRoute path={paths.SEARCH_USERS} exact
                    component={UserPage}
                    store={props.store}
                    allowedRoles={[role.ROOT]}
                />
                <PrivateRoute path={paths.VENUE_COUNT} exact
                    component={UserPage}
                    store={props.store}
                    allowedRoles={[role.ROOT]}
                />
            </Router>
        </div>
    );
}

export default App;