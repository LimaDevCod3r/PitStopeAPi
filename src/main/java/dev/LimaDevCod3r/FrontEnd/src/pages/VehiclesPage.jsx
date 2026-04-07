import { useState, useEffect, useCallback } from 'react';
import { Car } from 'lucide-react';
import api from '../lib/api';
import Modal from '../components/Modal';

export default function VehiclesPage() {
  const [vehicles, setVehicles] = useState([]);
  const [clients, setClients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({ brand: '', model: '', year: '', plate: '', color: '', customerId: '' });
  const [error, setError] = useState('');

  const fetchData = useCallback(async () => {
    setLoading(true);
    try {
      const [vRes, cRes] = await Promise.all([
        api.get('/veiculos?page=0&size=100&sortBy=brand'),
        api.get('/clientes?page=0&size=100&sortBy=name'),
      ]);
      setVehicles(vRes.data?.content ?? []);
      setClients(cRes.data?.content ?? []);
      setLoading(false);
    } catch {
      setVehicles([]);
      setClients([]);
      setLoading(false);
    }
  }, []);

  useEffect(() => { fetchData(); }, [fetchData]);

  const openModal = (vehicle = null) => {
    if (vehicle) {
      setEditing(vehicle);
      setForm({
        brand: vehicle.brand, model: vehicle.model, year: vehicle.year,
        plate: vehicle.plate, color: vehicle.color, customerId: vehicle.customerId ?? '',
      });
    } else {
      setEditing(null);
      setForm({ brand: '', model: '', year: '', plate: '', color: '', customerId: '' });
    }
    setError('');
    setModalOpen(true);
  };

  const closeModal = () => { setModalOpen(false); setEditing(null); setError(''); };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const payload = { ...form, customerId: parseInt(form.customerId) };
      if (editing) {
        await api.put(`/veiculos/${editing.id}`, payload);
      } else {
        await api.post('/veiculos', payload);
      }
      closeModal();
      fetchData();
    } catch (err) {
      setError(err.response?.data?.message || err.response?.data || 'Erro ao salvar veiculo');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Tem certeza que deseja excluir este veiculo?')) return;
    try {
      await api.delete(`/veiculos/${id}`);
      fetchData();
    } catch (err) {
      setError(err.response?.data?.message || err.response?.data || 'Erro ao excluir veiculo');
    }
  };

  const getCustomerName = (v) => {
    if (v.customerName) return v.customerName;
    if (v.customer?.name) return v.customer.name;
    const c = clients.find((cl) => cl.id === v.customerId);
    return c?.name ?? '-';
  };

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <h1 className="page-title">Veiculos</h1>
          <p className="page-subtitle">{vehicles.length} veiculos cadastrados</p>
        </div>
        <button className="btn btn-primary" onClick={() => openModal()}>
          + Novo Veiculo
        </button>
      </div>

      {error && <div className="alert alert-error">{error}</div>}

      {loading ? (
        <div className="spinner" />
      ) : vehicles.length === 0 ? (
        <div className="empty-state card">
          <div className="empty-state-icon"><Car size={48} /></div>
          <h3>Nenhum veiculo encontrado</h3>
          <p className="text-muted">Cadastre o primeiro veiculo clicando no botao acima</p>
        </div>
      ) : (
        <div className="table-wrapper">
          <table className="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Marca</th>
                <th>Modelo</th>
                <th>Ano</th>
                <th>Placa</th>
                <th>Cor</th>
                <th>Cliente</th>
                <th>Acoes</th>
              </tr>
            </thead>
            <tbody>
              {vehicles.map((v) => (
                <tr key={v.id}>
                  <td>{v.id}</td>
                  <td><strong>{v.brand}</strong></td>
                  <td>{v.model}</td>
                  <td>{v.year}</td>
                  <td>{v.plate}</td>
                  <td>{v.color}</td>
                  <td>{getCustomerName(v)}</td>
                  <td>
                    <div className="actions-cell">
                      <button className="btn btn-sm btn-outline" onClick={() => openModal(v)}>Editar</button>
                      <button className="btn btn-sm btn-danger" onClick={() => handleDelete(v.id)}>Excluir</button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      <Modal isOpen={modalOpen} onClose={closeModal} title={editing ? 'Editar Veiculo' : 'Novo Veiculo'}>
        <form onSubmit={handleSubmit}>
          <div className="form-grid form-grid-2">
            <div className="form-group">
              <label className="form-label">Marca</label>
              <input className="form-input" value={form.brand} onChange={(e) => setForm({...form, brand: e.target.value})} required />
            </div>
            <div className="form-group">
              <label className="form-label">Modelo</label>
              <input className="form-input" value={form.model} onChange={(e) => setForm({...form, model: e.target.value})} required />
            </div>
            <div className="form-group">
              <label className="form-label">Ano</label>
              <input className="form-input" value={form.year} onChange={(e) => setForm({...form, year: e.target.value})} required maxLength={4} placeholder="2024" />
            </div>
            <div className="form-group">
              <label className="form-label">Placa</label>
              <input className="form-input" value={form.plate} onChange={(e) => setForm({...form, plate: e.target.value.toUpperCase()})} required maxLength={7} placeholder="ABC1D23" />
            </div>
            <div className="form-group">
              <label className="form-label">Cor</label>
              <input className="form-input" value={form.color} onChange={(e) => setForm({...form, color: e.target.value})} required />
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
