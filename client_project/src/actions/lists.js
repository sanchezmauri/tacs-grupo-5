import * as api from '../api';
import { requestActionWithState } from './requestAction';
import { CREATE_LIST, CHANGE_LIST_NAME, FETCH_LISTS, DELETE_LIST } from './types';

export const fetchLists = () =>
    requestActionWithState(api.fetchLists(), FETCH_LISTS)

export const createList = (listName) =>
    requestActionWithState(api.createList(listName), CREATE_LIST)

export const deleteList = (id) =>
    requestActionWithState(api.deleteList(id), DELETE_LIST, id)

export const changeName = (id, newName) =>
    requestActionWithState(api.changeName(id, newName), CHANGE_LIST_NAME)