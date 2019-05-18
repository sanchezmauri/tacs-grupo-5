import { CREATE_LIST, CHANGE_LIST_NAME, FETCH_LISTS, DELETE_LIST, ADD_VENUES_TO_LIST, REMOVE_VENUE_FROM_LIST, VISIT_VENUE } from '../actions/types';
import _ from 'lodash';
import { getCompletedRequestAction } from '../actions/requestAction';

/*
state is like
{ listId: listObj }

listObj is like
{ id, name, venues }
*/
export default (state = {}, action) => {
    action = getCompletedRequestAction(action);

    if (!action) return state;

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
            return _.omit(state, action.payload);
        
        case CHANGE_LIST_NAME:
            let newLists = { ...state };
            newLists[action.payload.id].name = action.payload.newName;
            return newLists;
        
        case ADD_VENUES_TO_LIST:
            const addListId = action.payload.listId;
            let addList = state[addListId];
            if (!addList.venues)
                addList.venues = []
            
            addList.venues = addList.venues.concat(action.payload.venues);
            return { ...state }; // retornar copia para que redux avise
        
        case REMOVE_VENUE_FROM_LIST:
            let {listId, venueId} = action.payload;
            let rmList = state[listId];
            rmList.venues = rmList.venues.filter(venue => venue.id !== venueId);
            return { ...state, [listId]: _.clone(rmList) };

        case VISIT_VENUE:
            let listId2 = action.payload.listId;
            let venueId2 = action.payload.venueId;
            console.log(`VISIT_VENUE: changing ${venueId2} from list ${listId2}`);
            const visitlist = state[listId2];
            let venueToVisit = visitlist.venues.find(
                venue => venue.id === venueId2
            );
            
            if (venueToVisit) {
                console.log(`found venue ${venueToVisit}`);
                //venueToVisit = _.cloneDeep(venueToVisit);
                venueToVisit.visited = true;

                return { ...state, [listId2]: _.cloneDeep(visitlist) };
            } else {
                console.log(`Something is wrong: tried to visit inexistent venue ${venueId2}`);

                return state;
            }


        default:
            return state;
    }
}