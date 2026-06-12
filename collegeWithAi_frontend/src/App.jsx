import { useState } from 'react';
import { CATEGORIES } from './constants/categories';
import { fetchCategoryData, askAi } from './services/api';
import { CategorySelector } from './components/CategorySelector';
import { DataViewer } from './components/DataViewer';
import { AiAssistantModal } from './components/AiAssistantModal';
import './App.css';

function App() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [activeEndpoint, setActiveEndpoint] = useState('');

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [aiResponse, setAiResponse] = useState(null);
  const [aiLoading, setAiLoading] = useState(false);

  const [activeCategory, setActiveCategory] = useState(null);

  const fetchCategoryMethod = async (category, method) => {
    let finalPath = method.path;
    if (method.needsParam) {
      const paramValue = prompt(`Ingrese ${method.paramName}:`);
      if (!paramValue) return;
      finalPath = method.path + encodeURIComponent(paramValue);
    }

    setLoading(true);
    setError(null);
    setActiveEndpoint(`${category.name} - ${method.label}`);
    try {
      const result = await fetchCategoryData(category.basePath, finalPath);
      setData(result);
    } catch (err) {
      setError(err.message);
      setData(null);
    } finally {
      setLoading(false);
    }
  };

  const handleSelectCategory = (category) => {
    setActiveCategory(category);
    // Cargar por defecto el método "Todos" (path === '')
    const defaultMethod = category.methods.find(m => m.path === '' && !m.needsParam);
    if (defaultMethod) {
      fetchCategoryMethod(category, defaultMethod);
    } else {
      setData(null);
      setActiveEndpoint('');
    }
  };

  const handleBack = () => {
    setActiveCategory(null);
    setData(null);
    setActiveEndpoint('');
  };

  const handleMethodClick = (method) => {
    if (activeCategory) {
      fetchCategoryMethod(activeCategory, method);
    }
  };

  const handleAiSubmit = async (promptText) => {
    setAiLoading(true);
    setAiResponse(null);
    try {
      const result = await askAi(promptText);
      setAiResponse(result);
    } catch (err) {
      setAiResponse(`Error: ${err.message}`);
    } finally {
      setAiLoading(false);
    }
  };

  return (
    <div className="container">
      <header>
        <h1>College Manager AI</h1>
        <p style={{ color: 'var(--text-secondary)', fontSize: '1.2rem' }}>
          Panel de Control del Sistema Académico
        </p>
      </header>

      <CategorySelector
        categories={CATEGORIES}
        activeCategory={activeCategory}
        onSelectCategory={handleSelectCategory}
        onBack={handleBack}
        onOpenAi={() => setIsModalOpen(true)}
        onMethodClick={handleMethodClick}
        loading={loading}
      />

      <div className="result-container">
        <div className="result-header">
          <span className="result-title">
            {loading ? 'Cargando...' : activeEndpoint ? `Resultados: ${activeEndpoint}` : 'Seleccione una categoría'}
          </span>
          {loading && <div className="loading-spinner"></div>}
        </div>

        {error && (
          <div style={{ color: '#ef4444', padding: '1rem', background: '#450a0a', borderRadius: '8px', border: '1px solid #991b1b', marginBottom: '1.5rem' }}>
            <strong>Error:</strong> {error}
            <p style={{ fontSize: '0.9rem', marginTop: '0.5rem', marginBottom: 0 }}>
              Asegúrate de que el backend esté ejecutándose
            </p>
          </div>
        )}

        {data && !loading && <DataViewer data={data} />}

        {!data && !loading && !error && (
          <div style={{ textAlign: 'center', color: 'var(--text-secondary)', padding: '2rem' }}>
            <p>Haz clic en un botón para ver los datos correspondientes.</p>
          </div>
        )}
      </div>

      <AiAssistantModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSubmit={handleAiSubmit}
        aiLoading={aiLoading}
        aiResponse={aiResponse}
      />
    </div>
  );
}

export default App;
