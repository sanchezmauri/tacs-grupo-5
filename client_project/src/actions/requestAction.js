import history from '../routes/history';
import * as paths from '../routes/paths';
import statusCodes from 'http-status-codes'

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