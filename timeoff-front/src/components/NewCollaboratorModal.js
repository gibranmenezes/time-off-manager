import React, { useState } from 'react';
import { apiCall } from '../utils/api';

const departments = [
  'Administration',
  'Management',
  'Development',
  'IT',
  'Marketing',
  'Sales',
  'HR'
];

const roles = [
  'ADMIN',
  'COLLABORATOR',
  'MANAGER'
];

const NewCollaboratorModal = ({ show, onClose, onCreated }) => {
  const [formData, setFormData] = useState({
    name: '',
    username: '',
    email: '',
    password: '',
    role: '',
    department: '',
    managerId: ''
  });
  const [managers, setManagers] = useState([]);
  const [managersLoading, setManagersLoading] = useState(false);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const [managersLoaded, setManagersLoaded] = useState(false);

  const fetchManagers = async () => {
    if (managersLoaded) return;
    try {
      setManagersLoading(true);
      const response = await apiCall('collaborators?userRole=MANAGER&size=1000');
      setManagers(response.data || response.content);
      setManagersLoaded(true);
    } catch (err) {
      console.error("Failed to fetch managers:", err);
      setError("Failed to load managers list.");
    } finally {
      setManagersLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    let newFormData = { ...formData, [name]: value };
    
    // Resetar o managerId se a role não for 'COLLABORATOR'
    if (name === 'role' && value !== 'COLLABORATOR') {
      newFormData.managerId = null;
    }
    
    setFormData(newFormData);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const payload = {
        ...formData,
        managerId: formData.managerId ? Number(formData.managerId) : null
      };

      const response = await apiCall('collaborators', {
        method: 'POST',
        body: JSON.stringify(payload),
      });

      const newCollaborator = response.data || response.content;
      
      if (newCollaborator) {
        onCreated(newCollaborator);
        onClose();
      } else {
        setError('Failed to create collaborator: Invalid response from API.');
      }
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  if (!show) {
    return null;
  }

  return (
    <div className="modal d-block" tabIndex="-1" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
      <div className="modal-dialog">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">New Collaborator</h5>
            <button type="button" className="btn-close" onClick={onClose}></button>
          </div>
          <div className="modal-body">
            {error && <div className="alert alert-danger">{error}</div>}
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label htmlFor="name" className="form-label">Name</label>
                <input type="text" className="form-control" id="name" name="name" value={formData.name} onChange={handleChange} required />
              </div>
              <div className="mb-3">
                <label htmlFor="username" className="form-label">Username</label>
                <input type="text" className="form-control" id="username" name="username" value={formData.username} onChange={handleChange} required />
              </div>
              <div className="mb-3">
                <label htmlFor="email" className="form-label">Email</label>
                <input type="email" className="form-control" id="email" name="email" value={formData.email} onChange={handleChange} required />
              </div>
              <div className="mb-3">
                <label htmlFor="password" className="form-label">Password</label>
                <input type="password" className="form-control" id="password" name="password" value={formData.password} onChange={handleChange} required />
              </div>
              <div className="mb-3">
                <label htmlFor="role" className="form-label">Role</label>
                <select className="form-select" id="role" name="role" value={formData.role} onChange={handleChange} required>
                  <option value="">Select a role</option>
                  {roles.map((role) => (
                    <option key={role} value={role}>{role}</option>
                  ))}
                </select>
              </div>
              <div className="mb-3">
                <label htmlFor="department" className="form-label">Department</label>
                <select className="form-select" id="department" name="department" value={formData.department} onChange={handleChange} required>
                  <option value="">Select a department</option>
                  {departments.map((dept) => (
                    <option key={dept} value={dept}>{dept}</option>
                  ))}
                </select>
              </div>
              {formData.role === 'COLLABORATOR' && (
                <div className="mb-3">
                  <label htmlFor="managerId" className="form-label">Manager</label>
                  <select className="form-select" id="managerId" name="managerId" value={formData.managerId} onChange={handleChange} onFocus={fetchManagers}>
                    <option value="">Select a manager</option>
                    {managersLoading ? (
                      <option>Loading...</option>
                    ) : (
                      managers.map(manager => (
                        <option key={manager.id} value={manager.id}>
                          {manager.name}
                        </option>
                      ))
                    )}
                  </select>
                </div>
              )}
              <button type="submit" className="btn btn-primary w-100" disabled={loading}>
                {loading ? 'Creating...' : 'Create Collaborator'}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default NewCollaboratorModal;