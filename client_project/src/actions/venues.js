import * as types from './types';
import * as api from '../api';
import { requestAction } from './requestAction';


export const searchVenues = (latitude, longitude, query) => {
    const request = api.searchVenues(latitude, longitude, query);

    return requestAction(request, types.SEARCH_VENUES, null);
}

export const findCoords = () =>
    (dispatch, getState) => {
        const location = getState().coords;

        if (!location) {
            window.navigator.geolocation.getCurrentPosition(
                position => {

                    dispatch({
                        type: types.FIND_COORDS,
                        payload: position.coords
                    });
                },
                err => console.log("couldn't get position", err)
            );
        }
    }

export const selectVenue = venue => ({
    type: types.SELECT_VENUE,
    payload: venue
})

export const deselectVenue = venue => ({
    type: types.DESELECT_VENUE,
    payload: venue
})

export const clearVenueSelection = () => ({
    type: types.CLEAR_VENUE_SELECTION
})

