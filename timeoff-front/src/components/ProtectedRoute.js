import React from 'react';
import { Navigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';

const isAuthorized = (allowedRoles) => {
  const token = localStorage.getItem('authToken');
  if (!token) {
    return false;
  }

  try {
    const decodedToken = jwtDecode(token);
    console.log("Decoded Token Payload:", decodedToken);
    const userRole = decodedToken.role;
    
    return allowedRoles.includes(userRole);
  } catch (error) {
    console.error("Failed to decode token", error);
    return false;
  }
};

const ProtectedRoute = ({ children, allowedRoles }) => {
  if (!isAuthorized(allowedRoles)) {
    return <Navigate to="/" replace />;
  }

  return children;
};

export default ProtectedRoute;