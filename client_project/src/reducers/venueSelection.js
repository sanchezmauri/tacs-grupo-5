import { SELECT_VENUE, DESELECT_VENUE, CLEAR_VENUE_SELECTION } from "../actions/types";

export default (state = {}, action) => {
    switch (action.type) {
        case SELECT_VENUE:
            return { ...state, [action.payload.id]: true };
        
        case DESELECT_VENUE:
            return { ...state, [action.payload.id]: false };
        
        case CLEAR_VENUE_SELECTION:
            return {};
        
        default:
            return state;
    }
}