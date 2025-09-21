import React, { useState } from 'react';
import { FaEye, FaEdit, FaUserSlash, FaCalendarPlus, FaSort, FaSortUp, FaSortDown } from 'react-icons/fa';

const CollaboratorsTable = ({ collaborators, onDetails, onEdit, onDelete, onRequestVacation, userRole, openDetailsId, sortConfig, onSort }) => {
  const [allChecked, setAllChecked] = useState(false);
  const [checkedItems, setCheckedItems] = useState({});

  const handleAllCheckboxChange = (e) => {
    const isChecked = e.target.checked;
    setAllChecked(isChecked);
    const newCheckedItems = {};
    collaborators.forEach((collab) => {
      newCheckedItems[collab.id] = isChecked;
    });
    setCheckedItems(newCheckedItems);
  };

  const handleCheckboxChange = (id) => (e) => {
    setCheckedItems({
      ...checkedItems,
      [id]: e.target.checked,
    });
    if (!e.target.checked) {
      setAllChecked(false);
    }
  };
  
  const canModify = userRole === 'ADMIN';

  const getSortIcon = (key) => {
    if (sortConfig.key === key) {
      if (sortConfig.direction === 'ascending') {
        return <FaSortUp className="ms-1" />;
      } else {
        return <FaSortDown className="ms-1" />;
      }
    }
    return <FaSort className="ms-1 text-muted" />;
  };

  return (
    <div className="table-responsive">
      <table className="table table-hover table-striped">
        <thead className="table-light">
          <tr>
            <th>
              <input type="checkbox" onChange={handleAllCheckboxChange} checked={allChecked} />
            </th>
            <th onClick={() => onSort('name')} style={{ cursor: 'pointer' }}>
              Name {getSortIcon('name')}
            </th>
            <th onClick={() => onSort('email')} style={{ cursor: 'pointer' }}>
              Email {getSortIcon('email')}
            </th>
            <th onClick={() => onSort('department')} style={{ cursor: 'pointer' }}>
              Department {getSortIcon('department')}
            </th>
            <th onClick={() => onSort('role')} style={{ cursor: 'pointer' }}>
              Role {getSortIcon('role')}
            </th>
            <th onClick={() => onSort('active')} style={{ cursor: 'pointer' }}>
              Status {getSortIcon('active')}
            </th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {collaborators.map((collaborator) => (
            <React.Fragment key={collaborator.id}>
              <tr>
                <td>
                  <input
                    type="checkbox"
                    checked={!!checkedItems[collaborator.id]}
                    onChange={handleCheckboxChange(collaborator.id)}
                  />
                </td>
                <td>{collaborator.name}</td>
                <td>{collaborator.email}</td>
                <td>{collaborator.department}</td>
                <td>{collaborator.role}</td>
                <td>
                  <span className={`badge rounded-pill ${collaborator.active ? 'bg-success' : 'bg-danger'}`}>
                    {collaborator.active ? 'Active' : 'Inactive'}
                  </span>
                </td>
                <td>
                  <div className="d-flex gap-2">
                    <button className="btn btn-sm btn-outline-info" onClick={() => onDetails(collaborator.id)} title="View details">
                      <FaEye />
                    </button>
                    {canModify && (
                      <>
                        <button className="btn btn-sm btn-outline-warning" onClick={() => onEdit(collaborator.id)} title="Edit collaborator">
                          <FaEdit />
                        </button>
                        <button className="btn btn-sm btn-outline-danger" onClick={() => onDelete(collaborator.id)} title="Delete collaborator">
                          <FaUserSlash />
                        </button>
                      </>
                    )}
                    <button className="btn btn-sm btn-outline-success" onClick={() => onRequestVacation(collaborator.id)} title="Request vacation">
                      <FaCalendarPlus />
                    </button>
                  </div>
                </td>
              </tr>
              {openDetailsId === collaborator.id && (
                <tr className="bg-light">
                  <td colSpan="7">
                    <div className="p-3">
                      <h5>Collaborator Details</h5>
                      <p><strong>ID:</strong> {collaborator.id}</p>
                      <p><strong>Username:</strong> {collaborator.username}</p>
                      <p><strong>Email:</strong> {collaborator.email}</p>
                      <p><strong>Department:</strong> {collaborator.department}</p>
                      <p><strong>Role:</strong> {collaborator.role}</p>
                      <p><strong>Situation:</strong> {collaborator.active ? 'Active' : 'Inactive'}</p>
                      <p><strong>Manager Name:</strong> {collaborator.managerName || 'N/A'}</p>
                      <p><strong>Created At:</strong> {new Date(collaborator.createdAt).toLocaleString()}</p>
                    </div>
                  </td>
                </tr>
              )}
            </React.Fragment>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default CollaboratorsTable;