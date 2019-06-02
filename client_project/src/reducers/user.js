import { LOGIN, LOGOUT } from '../actions/types'
import { getCompletedRequestAction } from '../actions/requestAction';
import * as role from '../role';

// todo: esto no se qué tan bueno es
// es que si no, tendría que preguntar en cada página?
const initialValue = {
    loggedIn: false, // document.cookie.includes('PLAY_SESSION'),
    role: role.NONE // todo: descomentar linea arriba y ver como obtener el rol (desde la sesion?)
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