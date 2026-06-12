const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

export const fetchCategoryData = async (basePath, finalPath) => {
  const response = await fetch(`${API_BASE_URL}/${basePath}${finalPath}`);
  if (!response.ok) {
    throw new Error(`Error: ${response.status} ${response.statusText}`);
  }
  return response.json();
};

export const askAi = async (prompt) => {
  const response = await fetch(`${API_BASE_URL}/chat/preguntar`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ mensaje: prompt })
  });
  if (!response.ok) {
    throw new Error(`Error: ${response.status} ${response.statusText}`);
  }
  return response.text();
};
