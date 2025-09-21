import React, { useState, useEffect, useCallback, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaSearch, FaPlus } from 'react-icons/fa';
import { jwtDecode } from 'jwt-decode';
import Layout from './Layout';
import CollaboratorsTable from './CollaboratorsTable';
import NewCollaboratorModal from './NewCollaboratorModal';
import NewVacationModal from './NewVacationModal';
import { apiCall } from '../utils/api';

const CollaboratorsPage = () => {
  const [collaborators, setCollaborators] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filters, setFilters] = useState({ id: '', name: '', department: '', userRole: '' });
  const [searchField, setSearchField] = useState('name');
  const [searchValue, setSearchValue] = useState('');
  const [showActive, setShowActive] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [showVacationModal, setShowVacationModal] = useState(false);
  const [preselectedCollaboratorId, setPreselectedCollaboratorId] = useState(null);
  const [searchTrigger, setSearchTrigger] = useState(0);
  const [openDetailsId, setOpenDetailsId] = useState(null);
  const [sortConfig, setSortConfig] = useState({ key: 'id', direction: 'descending' });
  
  const navigate = useNavigate();

  const token = localStorage.getItem('authToken');
  const decodedToken = token ? jwtDecode(token) : null;
  const userRole = decodedToken ? decodedToken.role : null;
  const canModify = userRole === 'ADMIN';

  const fetchCollaborators = useCallback(async () => {
    try {
      setLoading(true);
      const searchFilter = {};
      if (searchValue) {
        searchFilter[searchField] = searchValue;
      }

      const combinedFilters = {
        ...searchFilter,
        department: filters.department,
        userRole: filters.userRole,
      };

      if (showActive) {
        combinedFilters.active = true;
      }

      const queryString = new URLSearchParams(combinedFilters).toString();
      const endpoint = `collaborators?${queryString}`;
      
      const response = await apiCall(endpoint);
      
      let fetchedData = [];
      if (response.data) {
        fetchedData = response.data;
      } else if (response.content) {
        fetchedData = response.content;
      }
      
      setCollaborators(fetchedData);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [searchField, searchValue, filters.department, filters.userRole, showActive]);

  useEffect(() => {
    fetchCollaborators();
  }, [fetchCollaborators, searchTrigger]);

  const sortedCollaborators = useMemo(() => {
    let sortedData = [...collaborators];
    if (sortConfig !== null) {
      sortedData.sort((a, b) => {
        if (a[sortConfig.key] < b[sortConfig.key]) {
          return sortConfig.direction === 'ascending' ? -1 : 1;
        }
        if (a[sortConfig.key] > b[sortConfig.key]) {
          return sortConfig.direction === 'ascending' ? 1 : -1;
        }
        return 0;
      });
    }
    return sortedData;
  }, [collaborators, sortConfig]);

  const handleSort = (key) => {
    let direction = 'ascending';
    if (sortConfig.key === key && sortConfig.direction === 'ascending') {
      direction = 'descending';
    } else if (sortConfig.key === key && sortConfig.direction === 'descending') {
      direction = 'ascending';
    }
    setSortConfig({ key, direction });
  };

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

  const handleActiveChange = (e) => {
    setShowActive(e.target.checked);
  };

  const handleCreateClick = () => {
    setShowModal(true);
  };

  const handleModalClose = () => {
    setShowModal(false);
  };

  const handleNewCollaboratorCreated = () => {
    setSearchTrigger(prevKey => prevKey + 1);
  };
  
  const handleDetailsClick = (id) => {
    setOpenDetailsId(openDetailsId === id ? null : id);
  };

  const handleEditClick = (id) => {
    console.log(`Edit collaborator with ID: ${id}`);
  };

  const handleDeleteClick = async (id) => {
    try {
      if (window.confirm('Are you sure you want to inactivate this collaborator?')) {
        await apiCall(`collaborators/${id}`, { method: 'DELETE' });
        setCollaborators(collaborators.filter(c => c.id !== id));
        console.log(`Collaborator with ID: ${id} inactivated successfully.`);
      }
    } catch (err) {
      console.error(`Error inactivating collaborator with ID: ${id}`, err);
      setError(`Failed to inactivate collaborator: ${err.message}`);
    }
  };

  const onRequestVacation = (collaboratorId) => {
    setPreselectedCollaboratorId(collaboratorId);
    setShowVacationModal(true);
  };

  const handleVacationCreated = () => {
    setShowVacationModal(false);
    setPreselectedCollaboratorId(null);
    navigate('/vacations');
  };

  const handleVacationModalClose = () => {
    setShowVacationModal(false);
    setPreselectedCollaboratorId(null);
  };

  const searchFieldsOptions = ['id', 'name', 'username', 'email'];
  
  const departments = [
    'Administration', 'Management', 'Development', 'IT', 'Marketing', 'Sales', 'HR'
  ];
  
  const roles = [
    'ADMIN', 'COLLABORATOR', 'MANAGER'
  ];

  return (
    <Layout>
      <div className="container mt-4">
        <h1 className="mb-4">Collaborators</h1>
        
        <div className="d-flex flex-wrap justify-content-between align-items-center mb-4 p-3 bg-light rounded shadow-sm">
          <div className="d-flex align-items-center flex-grow-1 gap-2 me-3">
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
              type={searchField === 'id' ? 'number' : 'text'} 
              className="form-control" 
              value={searchValue}
              onChange={handleSearchChange}
              placeholder={`Search by ${searchField}...`}
              style={{ width: '200px' }}
            />
            <select 
              name="department"
              className="form-select"
              value={filters.department}
              onChange={handleFilterChange}
              style={{ width: '150px' }}
            >
              <option value="">Department</option>
              {departments.map(dept => <option key={dept} value={dept}>{dept}</option>)}
            </select>
            <select 
              name="userRole"
              className="form-select"
              value={filters.userRole}
              onChange={handleFilterChange}
              style={{ width: '150px' }}
              disabled={userRole === 'MANAGER'}
            >
              <option value="">Role</option>
              {roles.map(role => <option key={role} value={role}>{role}</option>)}
            </select>
            <div className="form-check me-2">
              <input 
                className="form-check-input" 
                type="checkbox" 
                id="showActive" 
                checked={showActive} 
                onChange={handleActiveChange}
              />
              <label className="form-check-label" htmlFor="showActive">
                Active
              </label>
            </div>
            <button className="btn btn-primary" onClick={handleSearchClick} title="Search">
              <FaSearch />
            </button>
          </div>
          {canModify && (
            <button className="btn btn-primary mt-2 mt-md-0" onClick={handleCreateClick}>
              <FaPlus /> New Collaborator
            </button>
          )}
        </div>

        {loading && <div className="text-center">Loading...</div>}
        {error && <div className="alert alert-danger">{error}</div>}

        {!loading && !error && (
          <CollaboratorsTable 
            collaborators={sortedCollaborators} 
            onDetails={handleDetailsClick}
            onEdit={handleEditClick}
            onDelete={handleDeleteClick}
            onRequestVacation={onRequestVacation}
            userRole={userRole}
            openDetailsId={openDetailsId}
            sortConfig={sortConfig}
            onSort={handleSort}
          />
        )}
      </div>

      <NewCollaboratorModal 
        show={showModal} 
        onClose={handleModalClose} 
        onCreated={handleNewCollaboratorCreated} 
      />

      <NewVacationModal
        show={showVacationModal}
        onClose={handleVacationModalClose}
        onCreated={handleVacationCreated}
        preselectedCollaboratorId={preselectedCollaboratorId}
      />
    </Layout>
  );
};

export default CollaboratorsPage;