import React from 'react'
import { Router, Route } from 'react-router-dom'

import history from '../routes/history'
import * as paths from '../routes/paths'

import Header from './Header'
import Login from './Login'
import SearchVenues from './venues/SearchVenues'
// import VenueSuggestions from './venues/VenueSuggestions'
import VenueList from './lists/VenueList'
import ListsHome from './lists/ListsHome';


const App = () => {
    return (
        // con Router mapeas las rutas a los componentes
        // cuando hacés un Link con una ruta,
        // este router te renderea el componente para la ruta
        // aca no se renderiza nada, nada más se especifican rutas->componentes
        <div className="ui container">
            <Router history={history}>
                <Header />
                
                <Route path={paths.HOME} exact component={SearchVenues} />
                <Route path={paths.LOGIN} component={Login} />
                <Route path={paths.LISTS} component={ListsHome} />
                <Route path={paths.LIST} component={VenueList} />
            </Router>
        </div>
    );
}

export default App;