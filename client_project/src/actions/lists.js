import * as api from '../api';
import { requestAction } from './requestAction';
// import * as paths from '../routes/paths';
import { CREATE_LIST, CHANGE_LIST_NAME, FETCH_LISTS, DELETE_LIST } from './types';

export const fetchListsRequest = () =>
    requestAction(api.fetchLists(), FETCH_LISTS, null)

export const createList = (newList) =>
    ({ type: CREATE_LIST, payload: newList })

export const deleteList = (id) =>
    ({ type: DELETE_LIST, payload: id })


export const changeName = (id, newName) =>
    ({
        type: CHANGE_LIST_NAME,
        payload: {id, newName}
    })