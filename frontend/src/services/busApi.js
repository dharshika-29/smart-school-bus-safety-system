import api from '../api/api';

// ---------- Routes ----------
export const getRoutes = () => api.get('/routes').then(res => res.data);
export const createRoute = (data) => api.post('/routes', data).then(res => res.data);

// ---------- Students ----------
export const getStudents = () => api.get('/students').then(res => res.data);
export const createStudent = (data) => api.post('/students', data).then(res => res.data);

// ---------- Parents ----------
export const getParents = () => api.get('/parents').then(res => res.data);
export const createParent = (data) => api.post('/parents', data).then(res => res.data);

// ---------- Drivers ----------
export const getDrivers = () => api.get('/drivers').then(res => res.data);
export const createDriver = (data) => api.post('/drivers', data).then(res => res.data);

// ---------- Buses ----------
export const getBuses = () => api.get('/bus').then(res => res.data);

// ---------- Trips ----------
export const getTrips = () => api.get('/trips').then(res => res.data);
export const startTrip = (busId, driverId, routeId) =>
  api.post('/trips/start', { busId, driverId, routeId }).then(res => res.data);
export const endTrip = (tripId) => api.put(`/trips/${tripId}/end`).then(res => res.data);

// ---------- Locations ----------
export const updateLocation = (busId, latitude, longitude, address) =>
  api.post('/locations', { busId, latitude, longitude, address }).then(res => res.data);
export const getLatestLocation = (busId) =>
  api.get(`/locations/bus/${busId}/latest`).then(res => res.data);

// ---------- Notifications ----------
export const getNotifications = () => api.get('/notifications').then(res => res.data);