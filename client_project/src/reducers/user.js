import { LOGIN, LOGOUT } from '../actions/types'
import { getCompletedRequestAction } from '../actions/requestAction';
import * as role from '../role';
import * as cookies from '../cookies';

// todo: esto no se qué tan bueno es
// es que si no, tendría que preguntar en cada página?
const initialValue = {
    loggedIn: document.cookie.includes('PLAY_SESSION'),
    role: cookies.get(role.KEY) || role.NONE
};

export default (state = initialValue, action) => {
    action = getCompletedRequestAction(action);

    if (!action) return state;

    switch (action.type) {
        case LOGIN:
            return { ...state, loggedIn: true, role: action.payload.role };
            
        case LOGOUT:
            return { ...state, loggedIn: false, role: role.NONE }
        
        default:
            return state;
    }
}