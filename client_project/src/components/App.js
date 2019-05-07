import React from 'react'
import { Router, Route } from 'react-router-dom'

import history from '../routes/history'
import * as paths from '../routes/paths'

import Header from './Header'
import Login from './Login'
import ListVenueSearch from './venues/ListVenueSearch'
// import VenueSuggestions from './venues/VenueSuggestions'
import ListsHome from './lists/ListsHome';
import ListHome from './lists/ListHome'


const App = () => {
    return (
        // con Router mapeas las rutas a los componentes
        // cuando hacés un Link con una ruta,
        // este router te renderea el componente para la ruta
        // aca no se renderiza nada, nada más se especifican rutas->componentes
        <div className="ui container">
            <Router history={history}>
                <Header />
                
                <Route path={paths.LOGIN} component={Login} />
                <Route path={paths.HOME} exact component={ListsHome} />
                <Route path={paths.LISTS} exact component={ListsHome} />
                <Route path={paths.LIST} exact component={ListHome} />
                <Route path={paths.LIST_VENUES_SEARCH} component={ListVenueSearch} />
            </Router>
        </div>
    );
}

export default App;