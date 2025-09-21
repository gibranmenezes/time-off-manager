export const apiCall = async (endpoint, options = {}) => {
  const token = localStorage.getItem('authToken');

  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const response = await fetch(`http://localhost:8080/${endpoint}`, {
    ...options,
    headers,
  });

  if (!response.ok) {
    if (response.status === 401) {
      console.error('Unauthorized! Redirecting to login.');
      localStorage.removeItem('authToken');
      window.location.href = '/'; 
      return;
    }

    let errorData = await response.json();
    let errorMessage = errorData.message || `API Error: ${response.statusText}`;

    if (errorData.errors && errorData.errors.length > 0) {
      errorMessage = errorData.errors.map(err => err.description).join('; ');
    }

    throw new Error(errorMessage);
  }

  return response.json();
};