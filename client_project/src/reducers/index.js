import { combineReducers } from 'redux';
import login from './login';
import search from './search';
import coords from './coords';
import lists from './lists';

export default combineReducers({
    loggedIn: login,
    searchVenues: search,
    coords,
    lists
})