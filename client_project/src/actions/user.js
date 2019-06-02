import * as types from './types';
import * as api from '../api';
import history from '../routes/history';
import * as paths from '../routes/paths';
import * as role from '../role';
import { requestActionWithState } from './requestAction';

/* 
    redux funciona despachando acciones,
    solo despachando acciones cambias el estado.
    
    Pero esas acciones necesitan hacer cosas (fetchs de recursos).
    Los reducers tienen que ser puros.
    Entonces hay que usar una librería que sea un middleware
    que procesa las acciones, dando un hook para
    hacer efectos de lados. Vi 2:
    redux-saga
    redux-thunk
    La primera es más pro pero más compleja.
    Uso redux-thunk porque es más simple.
    
    En redux-thunk, estos action creators
    pueden devolver funciones en vez acciones {type, payload}.
    En estas funciones medio que podes hacer lo que se te cante
    (según lo que vi).
*/
export const login = (userData) =>
    requestActionWithState(
        api.login(userData),
        types.LOGIN,
        null,
        respData => {
            let path =  respData.role === role.SYSUSER ?
                        paths.HOME :
                        paths.SEARCH_USERS;
            
            history.push(path);
        }
    )

export const createUser = (userData) =>
    requestActionWithState(
        api.createUser(userData),
        types.CREATE_USER,
        null,
        discarded => history.push(paths.LOGIN)
    )

export const logout = () => 
    requestActionWithState(
        api.logout(),
        types.LOGOUT,
        null,
        (discarded, dispatch) => {
            dispatch({type: types.CLEAR_ALL});
            history.push(paths.LOGIN);
        }
    )