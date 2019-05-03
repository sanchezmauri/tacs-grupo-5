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
                    console.log("got position ok:", position.coords);

                    dispatch({
                        type: types.FIND_COORDS,
                        payload: position.coords
                    });
                },
                err => console.log("couldn't get position", err)
            );
        }
    }