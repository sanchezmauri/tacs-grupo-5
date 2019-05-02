import React from 'react'
import { Router, Route } from 'react-router-dom'

import history from '../routes/history'
import * as paths from '../routes/paths'

import Header from './Header'
import Login from './Login'
import SearchVenues from './venues/SearchVenues'
// import VenueSuggestions from './venues/VenueSuggestions'
import VenueList from './lists/VenueList'


const App = () => {
    return (
        <div className="ui container">
            <Router history={history}>
                <Header />
                
                <Route path={paths.HOME} exact component={SearchVenues} />
                <Route path={paths.LOGIN} component={Login} />
                <Route path={paths.VENUE_LIST} component={VenueList} />
            </Router>
        </div>
    );
}

export default App;