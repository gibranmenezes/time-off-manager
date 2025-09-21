import React, { useState } from 'react';
import { FaEye, FaEdit, FaCalendarTimes, FaCheck } from 'react-icons/fa';
import { differenceInCalendarDays, parseISO } from 'date-fns';

const VacationsTable = ({ vacations, onDetails, onEdit, onRespond, onCancel, openDetailsId, userRole, decodedToken }) => {
  const [allChecked, setAllChecked] = useState(false);
  const [checkedItems, setCheckedItems] = useState({});
  
  const handleAllCheckboxChange = (e) => {
    const isChecked = e.target.checked;
    setAllChecked(isChecked);
    const newCheckedItems = {};
    vacations.forEach((vacation) => {
      newCheckedItems[vacation.id] = isChecked;
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
  
  const getStatusColor = (status) => {
    switch (status) {
      case 'APPROVED': return 'bg-success';
      case 'DENIED': return 'bg-danger';
      case 'PENDING': return 'bg-secondary';
      case 'CANCELLED': return 'bg-warning';
      default: return 'bg-info';
    }
  };

  const calculateDays = (startDate, endDate) => {
    const start = parseISO(startDate);
    const end = parseISO(endDate);
    return differenceInCalendarDays(end, start) + 1;
  };
  
  const isManagerOrAdmin = userRole === 'ADMIN' || userRole === 'MANAGER';
  const isVacationPending = vacation => vacation.status === 'PENDING';
  const isOwnVacation = vacation => decodedToken && vacation.collaboratorId === decodedToken.id;
  const isCancelled = vacation => vacation.status === 'CANCELLED';

  return (
    <div className="table-responsive">
      <table className="table table-hover table-striped">
        <thead className="table-light">
          <tr>
            <th>
              <input type="checkbox" onChange={handleAllCheckboxChange} checked={allChecked} />
            </th>
            <th>Collaborator Name</th>
            <th>Start Date</th>
            <th>End Date</th>
            <th>Days</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {vacations.map((vacation) => (
            <React.Fragment key={vacation.id}>
              <tr>
                <td>
                  <input
                    type="checkbox"
                    checked={!!checkedItems[vacation.id]}
                    onChange={handleCheckboxChange(vacation.id)}
                  />
                </td>
                <td>{vacation.collaboratorName}</td>
                <td>{vacation.startDate}</td>
                <td>{vacation.endDate}</td>
                <td>{calculateDays(vacation.startDate, vacation.endDate)}</td>
                <td>
                  <span className={`badge rounded-pill ${getStatusColor(vacation.status)}`}>
                    {vacation.status}
                  </span>
                </td>
                <td>
                  <div className="d-flex gap-2">
                    <button className="btn btn-sm btn-outline-info" onClick={() => onDetails(vacation)} title="Details">
                      <FaEye />
                    </button>
                    {isVacationPending(vacation) && isManagerOrAdmin && (
                      <button className="btn btn-sm btn-outline-success" onClick={() => onRespond(vacation)} title="Respond">
                        <FaCheck />
                      </button>
                    )}
                    {(!isCancelled(vacation) && (isManagerOrAdmin || (isVacationPending(vacation) && isOwnVacation(vacation)))) && (
                      <button className="btn btn-sm btn-outline-warning" onClick={() => onEdit(vacation)} title="Edit">
                        <FaEdit />
                      </button>
                    )}
                    {(isManagerOrAdmin || isOwnVacation(vacation)) && vacation.status !== 'CANCELLED' && (
                      <button className="btn btn-sm btn-outline-danger" onClick={() => onCancel(vacation.id)} title="Cancel">
                        <FaCalendarTimes />
                      </button>
                    )}
                  </div>
                </td>
              </tr>
              {openDetailsId === vacation.id && (
                <tr className="bg-light">
                  <td colSpan="7">
                    <div className="p-3">
                      <h5>Vacation Details</h5>
                      <p><strong>Collaborator ID:</strong> {vacation.collaboratorId}</p>
                      <p><strong>Start Date:</strong> {vacation.startDate}</p>
                      <p><strong>End Date:</strong> {vacation.endDate}</p>
                      <p><strong>Status:</strong> {vacation.status}</p>
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

export default VacationsTable;