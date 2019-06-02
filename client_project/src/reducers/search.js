import { SEARCH_VENUES, CLEAR_ALL } from '../actions/types';
import { getCompletedRequestAction } from '../actions/requestAction';

export default (state = [], action) => {
    if (action.type === CLEAR_ALL) return [];

    action = getCompletedRequestAction(action);

    if (!action) return state;

    switch (action.type) {
        case SEARCH_VENUES:
            return action.payload;
    
        default:
            return state;
    }
}