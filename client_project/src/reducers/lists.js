import { CREATE_LIST, CHANGE_LIST_NAME, FETCH_LISTS, DELETE_LIST } from '../actions/types';
import { omit } from 'lodash';

export default (state = {}, action) => {
    switch (action.type) {
        case FETCH_LISTS:
            const count = action.payload.length;
            let listsById = {};

            for (let i = 0; i < count; i++) {
                const list = action.payload[i];
                listsById[list.id] = list;
            }

            return listsById;

        case CREATE_LIST:
            return {
                ...state,
                [action.payload.id]: action.payload
            }
        
        case DELETE_LIST:
            console.log('del list reducer');
            return omit(state, action.payload);
        
        case CHANGE_LIST_NAME:
            let newLists = { ...state };
            newLists[action.payload.id].name = action.payload.newName;
            return newLists;

        default:
            return state;
    }
}