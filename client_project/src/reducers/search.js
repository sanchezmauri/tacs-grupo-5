import { SEARCH_VENUES, CLEAR_ALL } from '../actions/types';

export default (state = [], action) => {
    switch (action.type) {
        case SEARCH_VENUES:
            return action.payload;

        case CLEAR_ALL:
            return [];
        
        default:
            return state;
    }
}