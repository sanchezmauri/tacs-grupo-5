import history from '../routes/history';
import * as paths from '../routes/paths';
import statusCodes from 'http-status-codes'
import { REQUEST, DONE, FETCH, ERROR } from './types';

export const requestAction = (requestPromise, actionType, navigateTo) =>
    dispatch => 
        requestPromise.then(response => {
            dispatch({
                type: actionType,
                payload: response.data
            });

            if (navigateTo) {
                history.push(navigateTo);
            }
        }).catch(error => {
            console.log("Error with request", requestPromise, "action type:", actionType);

            if (error.response) {
                if (error.response.status === statusCodes.UNAUTHORIZED) {
                    history.push(paths.LOGIN);
                }
            }
        })


export const requestActionWithState = (requestPromise, actionType, payload = null) =>
    dispatch => {
        let requestAction = {
            type: REQUEST,
            payload: {
                state: FETCH,
                subaction: {
                    type: actionType
                }
            }
        };

        // first, dispatch loading
        dispatch(requestAction);

        requestPromise.then(response => {
            requestAction.payload.state = DONE;

            requestAction.payload.subaction.payload = payload ? payload : response.data;

            dispatch(requestAction);
        }).catch(error => {
            console.log("Error with request", requestPromise, "action type:", actionType);
            requestAction.payload.state = ERROR;
            requestAction.payload.error = error;

            dispatch(requestAction);
        })
    }

export const getCompletedRequestAction = action => {
    if (action.type === REQUEST && action.payload.state === DONE) {
        return action.payload.subaction;
    }

    return null;
}
    
    