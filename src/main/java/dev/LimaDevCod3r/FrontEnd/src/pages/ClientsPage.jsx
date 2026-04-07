import { useState, useEffect, useCallback } from 'react';
import api from '../lib/api';
import Modal from '../components/Modal';

export default function ClientsPage() {
  const [clients, setClients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({ name: '', email: '', phone: '', cpf: '' });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const fetchClients = useCallback(async () => {
    setLoading(true);
    try {
      const res = await api.get('/clientes?page=0&size=100&sortBy=name');
      const data = res.data?.content ?? [];
      setClients(data);
      setLoading(false);
    } catch {
      setClients([]);
      setLoading(false);
    }
  }, []);

  useEffect(() => { fetchClients(); }, [fetchClients]);

  const openModal = (client = null) => {
    if (client) {
      setEditing(client);
      setForm({ name: client.name, email: client.email, phone: client.phone, cpf: client.cpf });
    } else {
      setEditing(null);
      setForm({ name: '', email: '', phone: '', cpf: '' });
    }
    setError('');
    setModalOpen(true);
  };

  const closeModal = () => { setModalOpen(false); setEditing(null); setError(''); };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    try {
      if (editing) {
        await api.put(`/clientes/${editing.id}`, form);
        setSuccess('Cliente atualizado com sucesso');
      } else {
        await api.post('/clientes', form);
        setSuccess('Cliente criado com sucesso');
      }
      closeModal();
      fetchClients();
    } catch (err) {
      setError(err.response?.data?.message || err.response?.data || 'Erro ao salvar cliente');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Tem certeza que deseja excluir este cliente?')) return;
    try {
      await api.delete(`/clientes/${id}`);
      setSuccess('Cliente excluido com sucesso');
      fetchClients();
    } catch (err) {
      setError(err.response?.data?.message || err.response?.data || 'Erro ao excluir cliente');
    }
  };

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <h1 className="page-title">Clientes</h1>
          <p className="page-subtitle">{clients.length} clientes cadastrados</p>
        </div>
        <button className="btn btn-primary" onClick={() => openModal()}>
          + Novo Cliente
        </button>
      </div>

      {error && <div className="alert alert-error">{error}</div>}
      {success && <div className="alert alert-success">{success}</div>}

      {loading ? (
        <div className="spinner" />
      ) : clients.length === 0 ? (
        <div className="empty-state card">
          <div className="empty-state-icon">👤</div>
          <h3>Nenhum cliente encontrado</h3>
          <p className="text-muted">Cadastre o primeiro cliente clicando no botão acima</p>
        </div>
      ) : (
        <div className="table-wrapper">
          <table className="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Email</th>
                <th>Telefone</th>
                <th>CPF</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {clients.map((c) => (
                <tr key={c.id}>
                  <td>{c.id}</td>
                  <td><strong>{c.name}</strong></td>
                  <td>{c.email}</td>
                  <td>{c.phone}</td>
                  <td>{c.cpf}</td>
                  <td>
                    <div className="actions-cell">
                      <button className="btn btn-sm btn-outline" onClick={() => openModal(c)}>Editar</button>
                      <button className="btn btn-sm btn-danger" onClick={() => handleDelete(c.id)}>Excluir</button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      <Modal isOpen={modalOpen} onClose={closeModal} title={editing ? 'Editar Cliente' : 'Novo Cliente'}>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label">Nome</label>
            <input className="form-input" value={form.name} onChange={(e) => setForm({...form, name: e.target.value})} required />
          </div>
          <div className="form-group">
            <label className="form-label">Email</label>
            <input type="email" className="form-input" value={form.email} onChange={(e) => setForm({...form, email: e.target.value})} required />
          </div>
          <div className="form-group">
            <label className="form-label">Telefone</label>
            <input className="form-input" value={form.phone} onChange={(e) => setForm({...form, phone: e.target.value})} required />
          </div>
          <div className="form-group">
            <label className="form-label">CPF</label>
            <input className="form-input" value={form.cpf} onChange={(e) => setForm({...form, cpf: e.target.value})} required maxLength={11} />
          </div>
          <div className="form-actions">
            <button type="button" className="btn btn-outline" onClick={closeModal}>Cancelar</button>
            <button type="submit" className="btn btn-primary">{editing ? 'Salvar' : 'Cadastrar'}</button>
          </div>
        </form>
      </Modal>
    </div>
  );
}
