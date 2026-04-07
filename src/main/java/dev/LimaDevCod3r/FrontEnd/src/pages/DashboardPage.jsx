import { useState, useEffect } from 'react';
import api from '../lib/api';

const stats = [
  { key: 'totalClients', label: 'Clientes', color: 'var(--color-info)', icon: '👤' },
  { key: 'totalVehicles', label: 'Veiculos', color: 'var(--color-primary)', icon: '🚗' },
  { key: 'totalServiceOrders', label: 'Ordens de Servico', color: 'var(--color-secondary)', icon: '📋' },
];

export default function DashboardPage() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      api.get('/clientes?page=0&size=1'),
      api.get('/veiculos?page=0&size=1'),
      api.get('/ordens-servico?page=0&size=1'),
    ]).then(([clients, vehicles, orders]) => {
      setData({
        totalClients: clients.data?.totalElements ?? 0,
        totalVehicles: vehicles.data?.totalElements ?? 0,
        totalServiceOrders: orders.data?.totalElements ?? 0,
      });
      setLoading(false);
    }).catch(() => setLoading(false));
  }, []);

  return (
    <div className="page">
      <h1 className="page-title">Dashboard</h1>
      <p className="page-subtitle">Visao geral da oficina</p>

      <div className="stats-grid">
        {stats.map((stat) => (
          <div key={stat.key} className="stat-card">
            <div className="stat-icon">{stat.icon}</div>
            <div className="stat-value" style={{ color: stat.color }}>
              {loading ? '...' : data?.[stat.key] ?? 0}
            </div>
            <div className="stat-label">{stat.label}</div>
          </div>
        ))}
      </div>

      <div className="welcome-card card">
        <h2 className="card-title">Bem-vindo ao PitStop!</h2>
        <p className="text-muted">
          Use o menu lateral para navegar entre as secoes.
          Cadastre clientes, veiculos e gerencie ordens de servico.
        </p>
      </div>
    </div>
  );
}
