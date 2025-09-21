import React, { useState, useEffect } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { apiCall } from '../utils/api';

const NewVacationModal = ({ show, onClose, onCreated, preselectedCollaboratorId }) => {
  const [collaborators, setCollaborators] = useState([]);
  const [formData, setFormData] = useState({
    collaboratorId: '',
    startDate: null,
    endDate: null,
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [collaboratorsLoading, setCollaboratorsLoading] = useState(false);
  const [collaboratorsLoaded, setCollaboratorsLoaded] = useState(false);

  useEffect(() => {
    if (!show) return;

    if (preselectedCollaboratorId) {
      setFormData(prevData => ({
        ...prevData,
        collaboratorId: String(preselectedCollaboratorId)
      }));
    }

    if (!collaboratorsLoaded) {
      fetchCollaborators();
    }

  }, [show, preselectedCollaboratorId, collaboratorsLoaded]);

  const fetchCollaborators = async () => {
    try {
      setCollaboratorsLoading(true);
      const response = await apiCall('collaborators?size=1000');
      setCollaborators(response.data || response.content);
      setCollaboratorsLoaded(true);
    } catch (err) {
      console.error("Failed to fetch collaborators:", err);
      setError("Failed to load collaborators list.");
    } finally {
      setCollaboratorsLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleDateChange = (date, name) => {
    setFormData({ ...formData, [name]: date });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    const vacationData = {
      collaboratorId: Number(formData.collaboratorId),
      startDate: formData.startDate.toISOString().split('T')[0],
      endDate: formData.endDate.toISOString().split('T')[0],
    };

    try {
      const response = await apiCall('vacations', {
        method: 'POST',
        body: JSON.stringify(vacationData),
      });

      if (response.data || response.content) {
        onCreated();
        onClose();
      } else {
        setError('Failed to create vacation: Invalid response.');
      }
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  if (!show) return null;

  return (
    <div className="modal d-block" tabIndex="-1" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
      <div className="modal-dialog">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">New Vacation</h5>
            <button type="button" className="btn-close" onClick={onClose}></button>
          </div>
          <div className="modal-body">
            {error && <div className="alert alert-danger">{error}</div>}
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label htmlFor="collaboratorId" className="form-label">Collaborator</label>
                <select 
                  className="form-select" 
                  id="collaboratorId" 
                  name="collaboratorId" 
                  value={formData.collaboratorId} 
                  onChange={handleChange}
                  required
                >
                  <option value="">Select a collaborator</option>
                  {collaboratorsLoading ? (
                    <option disabled>Loading...</option>
                  ) : (
                    collaborators.map(collab => (
                      <option key={collab.id} value={collab.id}>
                        {collab.name}
                      </option>
                    ))
                  )}
                </select>
              </div>
              <div className="mb-3">
                <label htmlFor="startDate" className="form-label">Start Date</label>
                <DatePicker 
                  selected={formData.startDate} 
                  onChange={(date) => handleDateChange(date, 'startDate')} 
                  className="form-control" 
                  required
                />
              </div>
              <div className="mb-3">
                <label htmlFor="endDate" className="form-label">End Date</label>
                <DatePicker 
                  selected={formData.endDate} 
                  onChange={(date) => handleDateChange(date, 'endDate')} 
                  className="form-control" 
                  required
                />
              </div>
              <button type="submit" className="btn btn-primary w-100" disabled={loading}>
                {loading ? 'Creating...' : 'Create Vacation'}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default NewVacationModal;