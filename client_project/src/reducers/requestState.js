import _ from 'lodash';
import { REQUEST, FETCH, ERROR } from '../actions/types';
import { isUnauthorizedError, isForbiddenError } from '../errors';

export const loadingReducer = (state = {}, action) => {
    if (action.type === REQUEST) {
        const subActionType = action.payload.subaction.type;

        if (action.payload.state === FETCH)
            return {
                ...state,
                [subActionType]: true
            };
        else
            return _.omit(state, subActionType);
    }

    return state;
}

export const errorsReducer = (state = {}, action) => {
    if (action.type === REQUEST) {
        const subActionType = action.payload.subaction.type;

        if (action.payload.state === ERROR)
            return {
                ...state,
                [subActionType]: action.payload.error
            };
        else
            return _.omit(state, subActionType);
    }

    return state;
}