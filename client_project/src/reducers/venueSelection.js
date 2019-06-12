import { SELECT_VENUE, DESELECT_VENUE, CLEAR_VENUE_SELECTION, CLEAR_ALL } from "../actions/types";

export default (state = {}, action) => {
    switch (action.type) {
        case SELECT_VENUE:
            return { ...state, [action.payload.id]: true };
        
        case DESELECT_VENUE:
            return { ...state, [action.payload.id]: false };
        
        case CLEAR_VENUE_SELECTION:
        case CLEAR_ALL:
            return {};
        
        default:
            return state;
    }
}