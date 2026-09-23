import { useEffect, useState, useCallback } from 'react';
import api from '../api/api';
import { useAuth } from '../AuthContext';

function timeAgo(dateString) {
  const seconds = Math.max(0, Math.floor((Date.now() - new Date(dateString).getTime()) / 1000));
  if (seconds < 15) return 'Just now';
  if (seconds < 60) return `${seconds} seconds ago`;
  const minutes = Math.floor(seconds / 60);
  if (minutes < 60) return `${minutes} minute${minutes > 1 ? 's' : ''} ago`;
  const hours = Math.floor(minutes / 60);
  if (hours < 24) return `${hours} hour${hours > 1 ? 's' : ''} ago`;
  return new Date(dateString).toLocaleString();
}

function statusClass(status) {
  return 'status-' + status.toLowerCase().replace(/_/g, '-');
}

export default function Dashboard() {
  const { user, logout } = useAuth();
  const [bus, setBus] = useState(null);
  const [error, setError] = useState('');
  const [showNotifs, setShowNotifs] = useState(false);
  const [notifications, setNotifications] = useState([]);

  const loadBus = useCallback(async () => {
    try {
      const res = await api.get('/bus/my');
      setBus(res.data);
      setError('');
    } catch (err) {
      if (err.message.includes('login') || err.message.includes('expired')) {
        logout();
        return;
      }
      setError(err.message);
    }
  }, [logout]);

  const loadNotifications = async () => {
    try {
      const res = await api.get('/notifications');
      setNotifications(res.data);
    } catch (err) {
      setError(err.message);
    }
  };

  useEffect(() => {
    loadBus();
    const interval = setInterval(loadBus, 10000); // auto-refresh every 10 sec
    return () => clearInterval(interval);
  }, [loadBus]);

  const handleTrack = () => {
    if (!bus || bus.lat == null || bus.lng == null) {
      setError('Bus location is not available yet.');
      return;
    }
    window.open(`https://www.google.com/maps?q=${bus.lat},${bus.lng}`, '_blank', 'noopener');
  };

  const toggleNotifications = async () => {
    const next = !showNotifs;
    setShowNotifs(next);
    if (next) await loadNotifications();
  };

  return (
    <div className="container">
      <h1>Smart School Bus Safety System</h1>

      <div className="dashboard">
        <div className="topbar">
          <h2>Parent Dashboard</h2>
          <div>
            {user && <span>Hi, {user.name}</span>}{' '}
            <button className="secondary" onClick={logout}>Logout</button>
          </div>
        </div>

        {error && <p className="error">{error}</p>}

        {bus ? (
          <>
            <p>Bus Number: <span>{bus.busNumber}</span></p>
            <p>Status: <b className={statusClass(bus.status)}>{bus.status.replace(/_/g, ' ')}</b></p>
            <p>Current Location: <span>{bus.locationName}</span></p>
            <p>Last Updated: <span>{timeAgo(bus.updatedAt)}</span></p>
          </>
        ) : (
          !error && <p>Loading bus data...</p>
        )}

        <button onClick={handleTrack}>Track Bus</button>
        <button onClick={toggleNotifications}>View Notifications</button>

        {showNotifs && (
          <div>
            <h3>Notifications</h3>
            <ul>
              {notifications.length === 0 && <li>No notifications yet.</li>}
              {notifications.map((n) => (
                <li key={n.id}>{n.message} ({timeAgo(n.createdAt)})</li>
              ))}
            </ul>
          </div>
        )}
      </div>
    </div>
  );
}
