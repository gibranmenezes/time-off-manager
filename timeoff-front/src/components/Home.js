import React from 'react';
import { Link } from 'react-router-dom';
import { FaUserFriends, FaCalendarAlt } from 'react-icons/fa';
import Layout from './Layout';

const Home = () => {
  return (
    <Layout>
      <div className="container mt-5">
        <h1 className="mb-4">Welcome to Vacation Management</h1>
        <p className="lead">Select an option to get started.</p>
        <div className="d-flex gap-3 mt-4">
          <Link to="/collaborators" className="btn btn-primary btn-lg d-flex align-items-center">
            <FaUserFriends className="me-2" />
            Manage Collaborators
          </Link>
          <Link to="/vacations" className="btn btn-success btn-lg d-flex align-items-center">
            <FaCalendarAlt className="me-2" />
            View Vacation Requests
          </Link>
        </div>
      </div>
    </Layout>
  );
};

export default Home;