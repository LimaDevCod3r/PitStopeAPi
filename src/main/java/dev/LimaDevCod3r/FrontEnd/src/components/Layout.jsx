import { Outlet, NavLink } from 'react-router-dom';
import { LayoutDashboard, Users, Car, Wrench } from 'lucide-react';
import './Layout.css';

const navLinks = [
  { to: '/dashboard', label: 'Dashboard', icon: LayoutDashboard },
  { to: '/clients', label: 'Clientes', icon: Users },
  { to: '/vehicles', label: 'Veiculos', icon: Car },
  { to: '/service-orders', label: 'Ordens de Servico', icon: Wrench },
];

export default function Layout() {
  return (
    <>
      <header className="layout-header">
        <div className="layout-header-inner">
          <div className="layout-brand">
            <Wrench className="brand-icon" size={24} />
            <span className="brand-name">PitStop</span>
          </div>
        </div>
      </header>
      <div className="layout-body">
        <aside className="layout-sidebar">
          <nav className="sidebar-nav">
            {navLinks.map((link) => {
              const Icon = link.icon;
              return (
                <NavLink
                  key={link.to}
                  to={link.to}
                  className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
                >
                  <Icon className="nav-icon" size={18} />
                  <span className="nav-label">{link.label}</span>
                </NavLink>
              );
            })}
          </nav>
        </aside>
        <main className="layout-main">
          <Outlet />
        </main>
      </div>
    </>
  );
}
