import { REQUEST, DONE, FETCH, ERROR } from './types';

export const requestActionWithState = (requestPromise, actionType, payload = null, successSideEffect = null) =>
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

            if (successSideEffect)
                successSideEffect(response.data, dispatch);
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
    
    