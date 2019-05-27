import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:9000/api';
axios.defaults.headers['Content-Type'] ='application/json;charset=utf-8';
axios.defaults.withCredentials = true;

// userData = { email, password }
export const login = (userData) =>
    axios.post('/security/login', userData)

export const logout = () =>
    axios.post('/security/logout');

// userData = { name, email, password }
export const createUser = (userData) =>
    axios.post('/users', userData)

export const searchVenues = (latitude, longitude, query) => {
    const params = {
        latitude,
        longitude,
        query
    };

    return axios.get(
        '/venues', { params }
    );
} 

export const fetchLists = () =>
    axios.get('/lists');

export const createList = (name) =>
    axios.post('/lists', { name });

export const deleteList = (id) =>
    axios.delete(`/lists/${id}`);

export const changeName = (id, name) =>
    axios.patch(`/lists/${id}`, { name });

export const addVenuesToList = (listId, venues) => {
    return axios.post(`/lists/${listId}/venues`, venues)
}

export const removeVenueFromList = (listId, venueId) =>
    axios.delete(`/lists/${listId}/venues`, { data: { id: venueId }  })
    

export const visitVenue = (listId, venueId) =>
    axios.patch(`/lists/${listId}/venues/${venueId}`)