import { FIND_COORDS } from '../actions/types';

export default (state = null, action) => {
    switch (action.type) {
        case FIND_COORDS:
            return action.payload;
        
        default:
            return state;
    }
}