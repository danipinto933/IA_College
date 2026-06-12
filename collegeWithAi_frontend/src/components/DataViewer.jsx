export const DataViewer = ({ data }) => {
  if (!data) return null;

  const renderValue = (value) => {
    if (value === null || value === undefined) {
      return <span className="data-value" style={{ color: '#64748b' }}>N/A</span>;
    }
    if (typeof value === 'boolean') {
      return <span className="data-value">{value ? 'Sí' : 'No'}</span>;
    }
    if (Array.isArray(value)) {
      if (value.length === 0) {
        return <span className="data-value" style={{ color: '#64748b' }}>Vacío</span>;
      }
      return (
        <div className="data-value-list">
          {value.map((item, idx) => (
            <span key={idx} className="data-badge">
              {typeof item === 'object' ? (item.name || item.title || item.id || 'Item') : String(item)}
            </span>
          ))}
        </div>
      );
    }
    if (typeof value === 'object') {
      return <span className="data-value">{value.name || value.title || value.email || value.id || 'Objeto'}</span>;
    }
    return <span className="data-value">{String(value)}</span>;
  };

  const renderCard = (obj, idx) => {
    if (typeof obj !== 'object' || obj === null) {
      return (
        <div key={idx} className="data-card">
          <div className="data-row">
            <span className="data-key">Valor</span>
            {renderValue(obj)}
          </div>
        </div>
      );
    }

    return (
      <div key={idx} className="data-card">
        {Object.entries(obj).map(([key, value]) => (
          <div key={key} className="data-row">
            <span className="data-key">{key.replace(/([A-Z])/g, ' $1').trim()}</span>
            {renderValue(value)}
          </div>
        ))}
      </div>
    );
  };

  if (Array.isArray(data)) {
    if (data.length === 0) {
      return (
        <div style={{ textAlign: 'center', color: 'var(--text-secondary)', padding: '2rem' }}>
          <p>No se encontraron resultados.</p>
        </div>
      );
    }
    return (
      <div className="data-grid">
        {data.map((item, idx) => renderCard(item, idx))}
      </div>
    );
  }

  return (
    <div className="data-grid" style={{ gridTemplateColumns: '1fr', maxWidth: '600px', margin: '0 auto' }}>
      {renderCard(data, 0)}
    </div>
  );
};
