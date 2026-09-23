import { useEffect, useState } from 'react';
import { useAuth } from '../AuthContext';
import {
  getRoutes, createRoute,
  getParents, createParent,
  getDrivers, createDriver,
  getStudents, createStudent,
  getBuses, updateLocation,
} from '../services/busApi';

export default function AdminPage() {
  const { logout } = useAuth();
  const [routes, setRoutes] = useState([]);
  const [parents, setParents] = useState([]);
  const [drivers, setDrivers] = useState([]);
  const [students, setStudents] = useState([]);
  const [buses, setBuses] = useState([]);
  const [error, setError] = useState('');
  const [msg, setMsg] = useState('');

  const [routeForm, setRouteForm] = useState({ routeName: '', startPoint: '', endPoint: '' });
  const [parentForm, setParentForm] = useState({ name: '', phone: '', email: '' });
  const [driverForm, setDriverForm] = useState({ name: '', phone: '', licenseNumber: '', email: '' });
  const [studentForm, setStudentForm] = useState({ name: '', className: '', rollNumber: '', parentId: '', busId: '' });
  const [locForm, setLocForm] = useState({ busId: '', latitude: '', longitude: '', address: '' });

  const loadAll = async () => {
    try {
      const [r, p, d, s, b] = await Promise.all([getRoutes(), getParents(), getDrivers(), getStudents(), getBuses()]);
      setRoutes(r); setParents(p); setDrivers(d); setStudents(s); setBuses(b);
    } catch (err) {
      setError(err.message);
    }
  };

  useEffect(() => { loadAll(); }, []);

  const handleSubmit = async (fn, formData, resetForm) => {
    setError(''); setMsg('');
    try {
      await fn(formData);
      setMsg('Added successfully!');
      resetForm();
      loadAll();
    } catch (err) {
      setError(err.message);
    }
  };

  const handleUseMyLocation = () => {
    if (!navigator.geolocation) {
      setError('Geolocation not supported by this browser');
      return;
    }
    navigator.geolocation.getCurrentPosition(
      (pos) => {
        setLocForm({ ...locForm, latitude: pos.coords.latitude.toFixed(6), longitude: pos.coords.longitude.toFixed(6) });
      },
      () => setError('Could not get your location. Enter manually.')
    );
  };

  const handleLocationSubmit = async (e) => {
    e.preventDefault();
    setError(''); setMsg('');
    try {
      await updateLocation(Number(locForm.busId), Number(locForm.latitude), Number(locForm.longitude), locForm.address);
      setMsg('Bus location updated!');
      setLocForm({ busId: '', latitude: '', longitude: '', address: '' });
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="container">
      <h1>Admin Panel — Smart School Bus Safety System</h1>
      <div className="dashboard">
        <div className="topbar">
          <h2>Manage System Data</h2>
          <button className="secondary" onClick={logout}>Logout</button>
        </div>

        {error && <p className="error">{error}</p>}
        {msg && <p style={{ color: 'green' }}>{msg}</p>}

        {/* ROUTE FORM */}
        <h3>Add Route</h3>
        <form onSubmit={(e) => { e.preventDefault(); handleSubmit(createRoute, routeForm, () => setRouteForm({ routeName: '', startPoint: '', endPoint: '' })); }}>
          <input placeholder="Route Name" value={routeForm.routeName} onChange={(e) => setRouteForm({ ...routeForm, routeName: e.target.value })} required />
          <input placeholder="Start Point" value={routeForm.startPoint} onChange={(e) => setRouteForm({ ...routeForm, startPoint: e.target.value })} required />
          <input placeholder="End Point" value={routeForm.endPoint} onChange={(e) => setRouteForm({ ...routeForm, endPoint: e.target.value })} required />
          <button type="submit">Add Route</button>
        </form>
        <ul>{routes.map(r => <li key={r.id}>{r.routeName} ({r.startPoint} → {r.endPoint})</li>)}</ul>

        {/* PARENT FORM */}
        <h3>Add Parent</h3>
        <form onSubmit={(e) => { e.preventDefault(); handleSubmit(createParent, parentForm, () => setParentForm({ name: '', phone: '', email: '' })); }}>
          <input placeholder="Name" value={parentForm.name} onChange={(e) => setParentForm({ ...parentForm, name: e.target.value })} required />
          <input placeholder="Phone" value={parentForm.phone} onChange={(e) => setParentForm({ ...parentForm, phone: e.target.value })} required />
          <input placeholder="Email" value={parentForm.email} onChange={(e) => setParentForm({ ...parentForm, email: e.target.value })} required />
          <button type="submit">Add Parent</button>
        </form>
        <ul>{parents.map(p => <li key={p.id}>{p.name} — {p.phone} — {p.email}</li>)}</ul>

        {/* DRIVER FORM */}
        <h3>Add Driver</h3>
        <form onSubmit={(e) => { e.preventDefault(); handleSubmit(createDriver, driverForm, () => setDriverForm({ name: '', phone: '', licenseNumber: '', email: '' })); }}>
          <input placeholder="Name" value={driverForm.name} onChange={(e) => setDriverForm({ ...driverForm, name: e.target.value })} required />
          <input placeholder="Phone" value={driverForm.phone} onChange={(e) => setDriverForm({ ...driverForm, phone: e.target.value })} required />
          <input placeholder="License Number" value={driverForm.licenseNumber} onChange={(e) => setDriverForm({ ...driverForm, licenseNumber: e.target.value })} required />
          <input placeholder="Email" value={driverForm.email} onChange={(e) => setDriverForm({ ...driverForm, email: e.target.value })} />
          <button type="submit">Add Driver</button>
        </form>
        <ul>{drivers.map(d => <li key={d.id}>{d.name} — {d.licenseNumber}</li>)}</ul>

        {/* STUDENT FORM */}
        <h3>Add Student</h3>
        <form onSubmit={(e) => { e.preventDefault(); handleSubmit(createStudent, { ...studentForm, parentId: studentForm.parentId || null, busId: studentForm.busId || null }, () => setStudentForm({ name: '', className: '', rollNumber: '', parentId: '', busId: '' })); }}>
          <input placeholder="Name" value={studentForm.name} onChange={(e) => setStudentForm({ ...studentForm, name: e.target.value })} required />
          <input placeholder="Class" value={studentForm.className} onChange={(e) => setStudentForm({ ...studentForm, className: e.target.value })} required />
          <input placeholder="Roll Number" value={studentForm.rollNumber} onChange={(e) => setStudentForm({ ...studentForm, rollNumber: e.target.value })} />
          <select value={studentForm.parentId} onChange={(e) => setStudentForm({ ...studentForm, parentId: e.target.value })}>
            <option value="">Select Parent</option>
            {parents.map(p => <option key={p.id} value={p.id}>{p.name}</option>)}
          </select>
          <button type="submit">Add Student</button>
        </form>
        <ul>{students.map(s => <li key={s.id}>{s.name} — {s.className} — Parent: {s.parentName || 'None'}</li>)}</ul>

        {/* LOCATION UPDATE (simulates driver sending GPS) */}
        <h3>Update Bus Location (Driver Simulation)</h3>
        <form onSubmit={handleLocationSubmit}>
          <select value={locForm.busId} onChange={(e) => setLocForm({ ...locForm, busId: e.target.value })} required>
            <option value="">Select Bus</option>
            {buses.map(b => <option key={b.id} value={b.id}>{b.busNumber}</option>)}
          </select>
          <button type="button" onClick={handleUseMyLocation}>Use My Current Location</button>
          <input placeholder="Latitude" value={locForm.latitude} onChange={(e) => setLocForm({ ...locForm, latitude: e.target.value })} required />
          <input placeholder="Longitude" value={locForm.longitude} onChange={(e) => setLocForm({ ...locForm, longitude: e.target.value })} required />
          <input placeholder="Address (e.g. Anna Nagar Main Road)" value={locForm.address} onChange={(e) => setLocForm({ ...locForm, address: e.target.value })} />
          <button type="submit">Update Location</button>
        </form>
      </div>
    </div>
  );
}