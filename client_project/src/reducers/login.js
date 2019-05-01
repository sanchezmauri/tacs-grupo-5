import { LOGIN, LOGOUT } from '../actions/types'

export default (state = false, action) => {
    switch (action.type) {
        case LOGIN:
            return true;
        
        case LOGOUT:
            return false;
        
        default:
            return state;
    }
}