import { SEARCH_VENUES } from '../actions/types';

export default (state = [], action) => {
    switch (action.type) {
        case SEARCH_VENUES:
            return action.payload;
        
        default:
            return state;
    }
}