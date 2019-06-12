import { FIND_COORDS, CLEAR_ALL } from '../actions/types';

export default (state = null, action) => {
    switch (action.type) {
        case FIND_COORDS:
            return action.payload;
        
        case CLEAR_ALL:
            return null;
        
        default:
            return state;
    }
}