import { useState, useEffect, useCallback } from 'react';
import api from '../lib/api';
import Modal from '../components/Modal';

const STATUS_LABELS = {
  OPEN: 'Aberta',
  IN_PROGRESS: 'Em Progresso',
  COMPLETED: 'Concluida',
  CANCELLED: 'Cancelada',
};

export default function ServiceOrdersPage() {
  const [orders, setOrders] = useState([]);
  const [vehicles, setVehicles] = useState([]);
  const [clients, setClients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({ description: '', estimatedPrice: '', vehicleId: '', customerId: '', status: 'OPEN' });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const fetchData = useCallback(async () => {
    setLoading(true);
    try {
      const [oRes, vRes, cRes] = await Promise.all([
        api.get('/ordens-servico?page=0&size=100&sortBy=id'),
        api.get('/veiculos?page=0&size=100&sortBy=brand'),
        api.get('/clientes?page=0&size=100&sortBy=name'),
      ]);
      setOrders(oRes.data?.content ?? []);
      setVehicles(vRes.data?.content ?? []);
      setClients(cRes.data?.content ?? []);
      setLoading(false);
    } catch {
      setOrders([]);
      setVehicles([]);
      setClients([]);
      setLoading(false);
    }
  }, []);

  useEffect(() => { fetchData(); }, [fetchData]);

  const openModal = (order = null) => {
    if (order) {
      setEditing(order);
      setForm({
        description: order.description,
        estimatedPrice: order.estimatedPrice?.toString() ?? '',
        vehicleId: order.vehicleId ?? '',
        customerId: order.customerId ?? '',
        status: order.status ?? 'OPEN',
      });
    } else {
      setEditing(null);
      setForm({ description: '', estimatedPrice: '', vehicleId: '', customerId: '', status: 'OPEN' });
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
      const payload = {
        description: form.description,
        estimatedPrice: parseFloat(form.estimatedPrice),
        vehicleId: parseInt(form.vehicleId),
        customerId: parseInt(form.customerId),
        status: form.status,
      };
      if (editing) {
        await api.put(`/ordens-servico/${editing.id}`, payload);
        setSuccess('Ordem atualizada com sucesso');
      } else {
        await api.post('/ordens-servico', payload);
        setSuccess('Ordem criada com sucesso');
      }
      closeModal();
      fetchData();
    } catch (err) {
      setError(err.response?.data?.message || err.response?.data || 'Erro ao salvar ordem de servico');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Tem certeza que deseja excluir esta ordem de servico?')) return;
    try {
      await api.delete(`/ordens-servico/${id}`);
      setSuccess('Ordem excluida com sucesso');
      fetchData();
    } catch (err) {
      setError(err.response?.data?.message || err.response?.data || 'Erro ao excluir ordem');
    }
  };

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <h1 className="page-title">Ordens de Servico</h1>
          <p className="page-subtitle">{orders.length} ordens de servico</p>
        </div>
        <button className="btn btn-primary" onClick={() => openModal()}>
          + Nova Ordem
        </button>
      </div>

      {error && <div className="alert alert-error">{error}</div>}
      {success && <div className="alert alert-success">{success}</div>}

      {loading ? (
        <div className="spinner" />
      ) : orders.length === 0 ? (
        <div className="empty-state card">
          <div className="empty-state-icon">📋</div>
          <h3>Nenhuma ordem de servico encontrada</h3>
          <p className="text-muted">Cadastre a primeira ordem clicando no botao acima</p>
        </div>
      ) : (
        <div className="table-wrapper">
          <table className="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Cliente</th>
                <th>Veiculo</th>
                <th>Descricao</th>
                <th>Valor Estimado</th>
                <th>Status</th>
                <th>Acoes</th>
              </tr>
            </thead>
            <tbody>
              {orders.map((o) => (
                <tr key={o.id}>
                  <td>{o.id}</td>
                  <td><strong>{o.customerName ?? '-'}</strong></td>
                  <td>{o.vehicleBrand} {o.vehicleModel} - {o.vehiclePlate}</td>
                  <td className="desc-cell">{o.description}</td>
                  <td>R$ {parseFloat(o.estimatedPrice ?? 0).toFixed(2)}</td>
                  <td><span className={`badge badge-${o.status?.toLowerCase()}`}>{STATUS_LABELS[o.status] || o.status}</span></td>
                  <td>
                    <div className="actions-cell">
                      <button className="btn btn-sm btn-outline" onClick={() => openModal(o)}>Editar</button>
                      <button className="btn btn-sm btn-danger" onClick={() => handleDelete(o.id)}>Excluir</button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      <Modal isOpen={modalOpen} onClose={closeModal} title={editing ? 'Editar Ordem' : 'Nova Ordem de Servico'}>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label">Descricao</label>
            <textarea className="form-input" value={form.description} onChange={(e) => setForm({...form, description: e.target.value})} rows={3} required />
          </div>
          <div className="form-grid form-grid-2">
            <div className="form-group">
              <label className="form-label">Valor Estimado (R$)</label>
              <input type="number" step="0.01" min="0.01" className="form-input" value={form.estimatedPrice} onChange={(e) => setForm({...form, estimatedPrice: e.target.value})} required placeholder="0.00" />
            </div>
            <div className="form-group">
              <label className="form-label">Status</label>
              <select className="form-select" value={form.status} onChange={(e) => setForm({...form, status: e.target.value})}>
                <option value="OPEN">Aberta</option>
                <option value="IN_PROGRESS">Em Progresso</option>
                <option value="COMPLETED">Concluida</option>
                <option value="CANCELLED">Cancelada</option>
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Cliente</label>
              <select className="form-select" value={form.customerId} onChange={(e) => setForm({...form, customerId: e.target.value})} required>
                <option value="">Selecione</option>
                {clients.map((c) => (
                  <option key={c.id} value={c.id}>{c.name}</option>
                ))}
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Veiculo</label>
              <select className="form-select" value={form.vehicleId} onChange={(e) => setForm({...form, vehicleId: e.target.value})} required>
                <option value="">Selecione</option>
                {vehicles.map((v) => (
                  <option key={v.id} value={v.id}>{v.brand} {v.model} - {v.plate}</option>
                ))}
              </select>
            </div>
          </div>
          <div className="form-actions">
            <button type="button" className="btn btn-outline" onClick={closeModal}>Cancelar</button>
            <button type="submit" className="btn btn-primary">{editing ? 'Salvar' : 'Criar'}</button>
          </div>
        </form>
      </Modal>
    </div>
  );
}
