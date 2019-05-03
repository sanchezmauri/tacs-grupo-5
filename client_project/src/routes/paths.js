export const LOGIN = '/login';
export const HOME = '/';
export const SEARCH = '/venues/search';

// lists paths
export const LISTS = '/lists';
export const LIST = '/lists/:id';

export const linkToList = listId => LIST.replace(':id', listId)