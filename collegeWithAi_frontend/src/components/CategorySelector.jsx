export const CategorySelector = ({
  categories,
  activeCategory,
  onSelectCategory,
  onBack,
  onOpenAi,
  onMethodClick,
  loading
}) => {
  if (!activeCategory) {
    return (
      <div className="button-grid">
        {categories.map((cat) => (
          <button
            key={cat.basePath}
            onClick={() => onSelectCategory(cat)}
            disabled={loading}
          >
            <span style={{ fontSize: '1.5rem' }}>{cat.icon}</span>
            {cat.name}
          </button>
        ))}
        <button
          onClick={onOpenAi}
          style={{ background: 'linear-gradient(135deg, #6366f1, #a855f7)', borderColor: 'transparent' }}
        >
          <span style={{ fontSize: '1.5rem' }}>🤖</span>
          Consultar IA
        </button>
      </div>
    );
  }

  return (
    <div className="button-grid">
      <button
        onClick={onBack}
        style={{ background: '#334155', borderColor: 'transparent' }}
      >
        <span style={{ fontSize: '1.5rem' }}>⬅️</span>
        Volver
      </button>
      {activeCategory.methods.map((method, idx) => (
        <button
          key={idx}
          onClick={() => onMethodClick(method)}
          disabled={loading}
          style={{ fontSize: '0.9rem' }}
        >
          {method.label}
        </button>
      ))}
    </div>
  );
};
