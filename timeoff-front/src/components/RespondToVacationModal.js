import React, { useState } from 'react';
import { apiCall } from '../utils/api';

const RespondToVacationModal = ({ show, onClose, vacation, onResponded }) => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleResponse = async (status) => {
    setLoading(true);
    setError(null);
    try {
      await apiCall(`vacations/${vacation.id}/response?response=${status}`, { method: 'PUT' });
      onResponded();
      onClose();
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
            <h5 className="modal-title">Respond to Vacation Request</h5>
            <button type="button" className="btn-close" onClick={onClose}></button>
          </div>
          <div className="modal-body">
            <p><strong>Collaborator:</strong> {vacation?.collaboratorName}</p>
            <p><strong>Period:</strong> {vacation?.startDate} to {vacation?.endDate}</p>
            <p className="lead mt-3">What is your response?</p>
            {error && <div className="alert alert-danger">{error}</div>}
            <div className="d-flex justify-content-around mt-3">
              <button className="btn btn-success" onClick={() => handleResponse('APPROVED')} disabled={loading}>
                Approve
              </button>
              <button className="btn btn-danger" onClick={() => handleResponse('DENIED')} disabled={loading}>
                Deny
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RespondToVacationModal;