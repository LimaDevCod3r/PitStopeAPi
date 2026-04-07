import { Outlet, NavLink } from 'react-router-dom';
import './Layout.css';

const navLinks = [
  { to: '/dashboard', label: 'Dashboard', icon: '📊' },
  { to: '/clients', label: 'Clientes', icon: '👤' },
  { to: '/vehicles', label: 'Veiculos', icon: '🚗' },
  { to: '/service-orders', label: 'Ordens de Servico', icon: '📋' },
];

export default function Layout() {
  return (
    <>
      <header className="layout-header">
        <div className="layout-header-inner">
          <div className="layout-brand">
            🔧
            <span className="brand-name">PitStop</span>
          </div>
        </div>
      </header>
      <div className="layout-body">
        <aside className="layout-sidebar">
          <nav className="sidebar-nav">
            {navLinks.map((link) => (
              <NavLink
                key={link.to}
                to={link.to}
                className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
              >
                <span className="nav-icon">{link.icon}</span>
                <span className="nav-label">{link.label}</span>
              </NavLink>
            ))}
          </nav>
        </aside>
        <main className="layout-main">
          <Outlet />
        </main>
      </div>
    </>
  );
}
