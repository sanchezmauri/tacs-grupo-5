import * as types from './types';
import * as api from '../api';
import history from '../routes/history';
import * as paths from '../routes/paths';
import statusCodes from 'http-status-codes'

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
export const login = (email, password) =>
    dispatch => {
        api.login(email, password).then((response) => {
            dispatch({type: types.LOGIN});
            history.push(paths.HOME);
        }).catch((error) => {
            console.log("login error: ", error);
            if (error.response) {
                if (error.response.status === statusCodes.UNAUTHORIZED) {
                    console.log("mostrar error en login!? como? mandar error por accion?");
                }
            }
        });
    }

export const logout = () =>
    dispatch => {
        api.logout().then(response => {
            dispatch({ type: types.LOGOUT });
            history.push(paths.LOGIN);
        }).catch(error => {
            console.log("logout error", error);
        })
    }
