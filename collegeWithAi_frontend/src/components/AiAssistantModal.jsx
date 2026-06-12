import { useState } from 'react';

export const AiAssistantModal = ({ isOpen, onClose, onSubmit, aiLoading, aiResponse }) => {
  const [aiPrompt, setAiPrompt] = useState('');

  if (!isOpen) return null;

  const handleSubmit = () => {
    if (!aiPrompt.trim()) return;
    onSubmit(aiPrompt);
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Asistente de IA</h2>
          <button className="modal-close" onClick={onClose}>×</button>
        </div>

        <textarea
          className="modal-input"
          placeholder="Escribe tu mensaje para la IA aquí..."
          value={aiPrompt}
          onChange={(e) => setAiPrompt(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === 'Enter' && !e.shiftKey) {
              e.preventDefault();
              handleSubmit();
            }
          }}
        />

        <button
          onClick={handleSubmit}
          disabled={aiLoading || !aiPrompt.trim()}
          style={{ width: '100%' }}
        >
          {aiLoading ? 'Enviando...' : 'Enviar Mensaje'}
        </button>

        {aiResponse && (
          <div className="ai-response">
            <strong>Respuesta:</strong>
            <p style={{ marginTop: '0.5rem', marginBottom: 0 }}>{aiResponse}</p>
          </div>
        )}
      </div>
    </div>
  );
};
