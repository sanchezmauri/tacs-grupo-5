import * as types from './types';
import {login as loginRequest, logout as logoutRequest} from '../api';
import history from '../routes/history';
import {HOME as HOME_ROUTE, LOGIN as LOGIN_ROUTE} from '../routes/paths';
import statusCodes from 'http-status-codes'

export const login = (email, password) =>
    dispatch => {
        loginRequest(email, password).then((response) => {
            dispatch({type: types.LOGIN});
            history.push(HOME_ROUTE);
        }).catch((error) => {
            console.log("login error: ");
            if (error.response) {
                if (error.response.status === statusCodes.UNAUTHORIZED) {
                    console.log("mostrar error en login!? como?")
                }
            }
        });
    }


export const logout = () =>
    (dispatch) => {
        logoutRequest().then(response => {
            dispatch({ type: types.LOGOUT });
            history.push(LOGIN_ROUTE);
        }).catch(error => {
            console.log("Error login out", error);
        })
    }