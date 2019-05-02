import { LOGIN, LOGOUT } from '../actions/types'

// todo: esto no se qué tan bueno es
// es que si no, tendría que preguntar en cada página?
const initialValue = document.cookie.includes('PLAY_SESSION');

export default (state = initialValue, action) => {
    switch (action.type) {
        case LOGIN:
            return true;
        
        case LOGOUT:
            return false;
        
        default:
            return state;
    }
}