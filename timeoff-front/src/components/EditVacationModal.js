import React, { useState, useEffect } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { apiCall } from '../utils/api';
import { parseISO } from 'date-fns';

const EditVacationModal = ({ show, onClose, vacation, onUpdated }) => {
  const [formData, setFormData] = useState({ startDate: null, endDate: null });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (vacation) {
      setFormData({
        startDate: parseISO(vacation.startDate),
        endDate: parseISO(vacation.endDate)
      });
    }
  }, [vacation]);

  const handleDateChange = (date, name) => {
    setFormData({ ...formData, [name]: date });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const payload = {
        startDate: formData.startDate.toISOString().split('T')[0],
        endDate: formData.endDate.toISOString().split('T')[0],
      };
      await apiCall(`vacations/${vacation.id}`, { method: 'PATCH', body: JSON.stringify(payload) });
      onUpdated();
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
            <h5 className="modal-title">Edit Vacation for {vacation?.collaboratorName}</h5>
            <button type="button" className="btn-close" onClick={onClose}></button>
          </div>
          <div className="modal-body">
            {error && <div className="alert alert-danger">{error}</div>}
            <form onSubmit={handleSubmit}>
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
                {loading ? 'Updating...' : 'Update Vacation'}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EditVacationModal;