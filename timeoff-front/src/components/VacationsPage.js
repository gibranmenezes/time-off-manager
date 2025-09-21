import React, { useState, useEffect, useCallback } from 'react';
import { FaPlus, FaSearch } from 'react-icons/fa';
import { jwtDecode } from 'jwt-decode';
import Layout from './Layout';
import VacationsTable from './VacationsTable';
import NewVacationModal from './NewVacationModal';
import EditVacationModal from './EditVacationModal';
import RespondToVacationModal from './RespondToVacationModal';
import { apiCall } from '../utils/api';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

const VacationsPage = () => {
  const [vacations, setVacations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filters, setFilters] = useState({ vacationId: '', collaboratorName: '', status: '', fromDate: null, toDate: null });
  const [searchField, setSearchField] = useState('vacationId');
  const [searchValue, setSearchValue] = useState('');
  const [showNewVacationModal, setShowNewVacationModal] = useState(false);
  const [showEditVacationModal, setShowEditVacationModal] = useState(false);
  const [showRespondModal, setShowRespondModal] = useState(false);
  const [searchTrigger, setSearchTrigger] = useState(0);
  const [openDetailsId, setOpenDetailsId] = useState(null);
  const [selectedVacation, setSelectedVacation] = useState(null);

  const token = localStorage.getItem('authToken');
  const decodedToken = token ? jwtDecode(token) : null;
  const userRole = decodedToken ? decodedToken.role : null;
  
  const fetchVacations = useCallback(async () => {
    try {
      setLoading(true);
      const combinedFilters = {};

      if (searchValue) {
        combinedFilters[searchField] = searchValue;
      }

      if (filters.status) {
        combinedFilters.status = filters.status;
      }
      if (filters.fromDate) {
        combinedFilters.fromDate = filters.fromDate.toISOString().split('T')[0];
      }
      if (filters.toDate) {
        combinedFilters.toDate = filters.toDate.toISOString().split('T')[0];
      }

      const queryString = new URLSearchParams(combinedFilters).toString();
      const endpoint = `vacations?${queryString}`;
      
      const response = await apiCall(endpoint);
      
      setVacations(response.data || response.content);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [searchValue, searchField, filters.status, filters.fromDate, filters.toDate]);

  useEffect(() => {
    fetchVacations();
  }, [fetchVacations, searchTrigger]);

  const handleSearchClick = () => {
    setSearchTrigger(prev => prev + 1);
  };

  const handleSearchChange = (e) => {
    setSearchValue(e.target.value);
  };

  const handleFieldChange = (e) => {
    setSearchField(e.target.value);
    setSearchValue('');
  };

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters(prevFilters => ({
      ...prevFilters,
      [name]: value,
    }));
  };

  const handleDateChange = (date, name) => {
    setFilters(prevFilters => ({ ...prevFilters, [name]: date }));
  };

  const handleCreateClick = () => setShowNewVacationModal(true);
  const handleModalClose = () => {
    setShowNewVacationModal(false);
    setShowEditVacationModal(false);
    setShowRespondModal(false);
    setSelectedVacation(null);
  };
  const handleVacationCreated = () => setSearchTrigger(prev => prev + 1);

  const handleEditClick = (vacation) => {
    setSelectedVacation(vacation);
    setShowEditVacationModal(true);
  };

  const handleRespondClick = (vacation) => {
    setSelectedVacation(vacation);
    setShowRespondModal(true);
  };

  const handleCancelClick = async (id) => {
    try {
      if (window.confirm('Are you sure you want to cancel this vacation request?')) {
        await apiCall(`vacations/${id}`, { method: 'DELETE' });
        setSearchTrigger(prev => prev + 1);
      }
    } catch (err) {
      setError(err.message);
    }
  };

  const handleDetailsClick = (id) => setOpenDetailsId(openDetailsId === id ? null : id);
  const searchFieldsOptions = ['vacationId', 'collaboratorName'];
  const statusOptions = ['PENDING', 'APPROVED', 'DENIED', 'CANCELLED'];

  return (
    <Layout>
      <div className="container mt-4">
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h1 className="mb-0">Vacations</h1>
          <button className="btn btn-primary" onClick={handleCreateClick}>
            <FaPlus className="me-2"/>New Vacation
          </button>
        </div>
        <div className="d-flex flex-wrap justify-content-between align-items-center mb-4 p-3 bg-light rounded shadow-sm">
          <div className="d-flex flex-grow-1 align-items-center gap-2 me-3">
            <label htmlFor="searchField" className="form-label mb-0">Search by:</label>
            <select
              id="searchField"
              className="form-select"
              value={searchField}
              onChange={handleFieldChange}
              style={{ width: '150px' }}
            >
              {searchFieldsOptions.map(field => (
                <option key={field} value={field}>
                  {field.charAt(0).toUpperCase() + field.slice(1)}
                </option>
              ))}
            </select>
            <input 
              type="text" 
              className="form-control" 
              value={searchValue}
              onChange={handleSearchChange}
              placeholder={`Search by ${searchField}...`}
              style={{ width: '200px' }}
            />
            <select 
              name="status"
              className="form-select"
              value={filters.status}
              onChange={handleFilterChange}
              style={{ width: '180px' }}
            >
              <option value="">Status</option>
              {statusOptions.map(status => <option key={status} value={status}>{status}</option>)}
            </select>
            <DatePicker
              selected={filters.fromDate}
              onChange={(date) => handleDateChange(date, 'fromDate')}
              className="form-control"
              placeholderText="From Date"
              style={{ width: '180px' }}
            />
            <DatePicker
              selected={filters.toDate}
              onChange={(date) => handleDateChange(date, 'toDate')}
              className="form-control"
              placeholderText="To Date"
              style={{ width: '180px' }}
            />
            <button className="btn btn-primary" onClick={handleSearchClick} title="Search">
              <FaSearch />
            </button>
          </div>
        </div>

        {loading && <div className="text-center">Loading...</div>}
        {error && <div className="alert alert-danger">{error}</div>}
        {!loading && !error && (
          <VacationsTable
            vacations={vacations}
            onDetails={handleDetailsClick}
            onEdit={handleEditClick}
            onRespond={handleRespondClick}
            onCancel={handleCancelClick}
            openDetailsId={openDetailsId}
            userRole={userRole}
            decodedToken={decodedToken}
          />
        )}
      </div>

      <NewVacationModal show={showNewVacationModal} onClose={handleModalClose} onCreated={handleVacationCreated} />
      <EditVacationModal show={showEditVacationModal} onClose={handleModalClose} vacation={selectedVacation} onUpdated={handleVacationCreated} />
      <RespondToVacationModal show={showRespondModal} onClose={handleModalClose} vacation={selectedVacation} onResponded={handleVacationCreated} />

    </Layout>
  );
};

export default VacationsPage;