import axios from 'axios';

const api = axios.create({
    baseURL: 'https://smart-school-bus-safety-system-production.up.railway.app/api',
    headers: {
        'Content-Type': 'application/json'
    }
});

// Request interceptor to add Authorization token if available
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default api;