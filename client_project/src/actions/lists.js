import * as api from '../api';
import { requestActionWithState } from './requestAction';
import { CREATE_LIST, CHANGE_LIST_NAME, FETCH_LISTS, DELETE_LIST, ADD_VENUES_TO_LIST, REMOVE_VENUE_FROM_LIST } from './types';

export const fetchLists = () =>
    requestActionWithState(api.fetchLists(), FETCH_LISTS)

export const createList = (listName) =>
    requestActionWithState(api.createList(listName), CREATE_LIST)

export const deleteList = (id) =>
    requestActionWithState(api.deleteList(id), DELETE_LIST, id)

export const changeListName = (id, newName) =>
    requestActionWithState(api.changeName(id, newName), CHANGE_LIST_NAME)

export const addVenuesToList = (listId, venues) =>
    requestActionWithState(
        api.addVenuesToList(listId, venues),
        ADD_VENUES_TO_LIST,
        { listId, venues }
    )

export const removeVenueFromList = (listId, venueId) =>
    requestActionWithState(
        api.removeVenueFromList(listId, venueId),
        REMOVE_VENUE_FROM_LIST,
        { listId, venueId }
    )