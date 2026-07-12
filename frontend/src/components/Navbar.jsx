import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { LogOut, ShieldCheck } from 'lucide-react';

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const links = [];
  if (user?.role === 'ADMIN') {
    links.push({ path: '/admin', label: 'Admin' });
    links.push({ path: '/mentor', label: 'Mentor View' });
    links.push({ path: '/student', label: 'Student View' });
  } else if (user?.role === 'MENTOR') {
    links.push({ path: '/mentor', label: 'My Students' });
  } else if (user?.role === 'STUDENT') {
    links.push({ path: '/student', label: 'My Dashboard' });
  }

  return (
    <nav className="bg-slate-950/80 backdrop-blur border-b border-white/10 px-6 py-4 flex justify-between items-center sticky top-0 z-20">
      <div className="flex items-center gap-6">
        <div className="flex items-center gap-2">
          <ShieldCheck className="text-violet-400" size={24} />
          <span className="font-bold text-lg text-white">EduShield</span>
        </div>
        <div className="flex gap-1">
          {links.map((link) => (
            <button
              key={link.path}
              onClick={() => navigate(link.path)}
              className={`px-3 py-1.5 rounded-lg text-sm font-medium transition ${
                location.pathname.startsWith(link.path)
                  ? 'bg-violet-500/20 text-violet-300'
                  : 'text-slate-400 hover:bg-white/5'
              }`}
            >
              {link.label}
            </button>
          ))}
        </div>
      </div>
      <div className="flex items-center gap-4">
        <span className="text-sm text-slate-400">
          {user?.username} <span className="text-slate-500">({user?.role})</span>
        </span>
        <button
          onClick={handleLogout}
          className="flex items-center gap-1 text-sm text-rose-400 hover:text-rose-300 font-medium"
        >
          <LogOut size={16} /> Logout
        </button>
      </div>
    </nav>
  );
}