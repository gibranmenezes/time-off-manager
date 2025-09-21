import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FaUserFriends, FaCalendarAlt, FaSignOutAlt } from 'react-icons/fa';

const Layout = ({ children }) => {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('authToken');
    navigate('/');
  };

  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#f8f9fa' }}>
      <aside className="bg-dark text-white p-4 d-flex flex-column" style={{ width: '250px' }}>
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h3 className="mb-0">
            <Link to="/dashboard" className="text-white text-decoration-none">TimeOff</Link>
          </h3>
          <button 
            className="btn btn-dark d-flex align-items-center"
            onClick={handleLogout}
            title="Sign Out"
            style={{ padding: '0.5rem' }}
          >
            <FaSignOutAlt />
          </button>
        </div>
        <div className="flex-grow-1">
          <nav className="nav flex-column">
            <Link to="/collaborators" className="nav-link text-white d-flex align-items-center mb-2">
              <FaUserFriends className="me-2" />
              Collaborators
            </Link>
            <Link to="/vacations" className="nav-link text-white d-flex align-items-center">
              <FaCalendarAlt className="me-2" />
              Vacations
            </Link>
          </nav>
        </div>
      </aside>
      <main className="flex-grow-1 p-4">
        {children}
      </main>
    </div>
  );
};

export default Layout;