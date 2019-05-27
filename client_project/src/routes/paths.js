export const LOGIN = '/login';
export const CREATE_USER = '/users/new';
export const HOME = '/';

// lists paths
export const LISTS = '/lists';
export const LIST = '/lists/:id';
export const LIST_VENUES_SEARCH = '/lists/:id/venues/search';

export const linkWithId = (path, id) => path.replace(':id', id)