import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './components/Login';
import Home from './components/Home';
import CollaboratorsPage from './components/CollaboratorsPage';
import VacationsPage from './components/VacationsPage';
import ProtectedRoute from './components/ProtectedRoute';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/dashboard" element={<Home />} />
        <Route 
          path="/collaborators" 
          element={
            <ProtectedRoute allowedRoles={['ADMIN', 'MANAGER']}>
              <CollaboratorsPage />
            </ProtectedRoute>
          } 
        />
        <Route 
          path="/vacations" 
          element={
            <ProtectedRoute allowedRoles={['ADMIN', 'MANAGER', 'COLLABORATOR']}>
              <VacationsPage />
            </ProtectedRoute>
          } 
        />
      </Routes>
    </Router>
  );
}

export default App;