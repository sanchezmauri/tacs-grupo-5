import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:9000/api';
axios.defaults.headers['Content-Type'] ='application/json;charset=utf-8';
axios.defaults.withCredentials = true;

export const login = (email, password) => {
    return axios.post('/security/login', {
        email,
        password
    })
}

export const logout = () => {
    return axios.post('/security/logout');
}

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