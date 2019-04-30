import React from 'react'
import { Router, Route } from 'react-router-dom'

import history from '../routes/history'
import * as urls from '../routes/urls'

import Header from './Header'
import Login from './Login'
import VenueSuggestions from './VenueSuggestions'
import VenueList from './VenueList'


const App = () => {
    return (
        <div className="ui container">
            <Router history={history}>
                <Header />
                
                <Route path={urls.HOME} exact component={VenueSuggestions} />
                <Route path={urls.LOGIN} component={Login} />
                <Route path={urls.VENUE_LIST} component={VenueList} />
            </Router>
        </div>
    );
}

export default App;