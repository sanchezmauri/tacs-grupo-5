import { combineReducers } from 'redux';
import login from './login';
import search from './search';
import coords from './coords';
import lists from './lists';
import {loadingReducer, errorsReducer} from './requestState';
import venueSelection from './venueSelection';
import _ from 'lodash';

export default combineReducers({
    loggedIn: login,
    searchVenues: search,
    coords,
    lists,
    loading: loadingReducer,
    errors: errorsReducer,
    venueSelection
})

export const isVenueSelected = (venueId, state) =>
    _.has(state.venueSelection, venueId) && state.venueSelection[venueId]