import { useState, useEffect } from 'react';
import axiosClient from '../api/axiosClient';
import Navbar from '../components/Navbar';
import { Plus, X, UserPlus } from 'lucide-react';

export default function AdminDashboard() {
  const [scholarships, setScholarships] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [loading, setLoading] = useState(true);
  const [formData, setFormData] = useState({
    name: '', minIncome: '', maxIncome: '', category: 'GENERAL',
    minMarks: '', state: '', deadline: '', description: ''
  });

  const [showUserForm, setShowUserForm] = useState(false);
  const [userFormData, setUserFormData] = useState({
    username: '', password: '', role: 'STUDENT',
    name: '', department: '', rollNo: '', year: '',
    category: 'GENERAL', annualIncome: '', state: ''
  });

  const fetchScholarships = async () => {
    try {
      const res = await axiosClient.get('/scholarships');
      setScholarships(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchScholarships();
  }, []);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axiosClient.post('/scholarships', {
        ...formData,
        minIncome: parseFloat(formData.minIncome) || null,
        maxIncome: parseFloat(formData.maxIncome) || null,
        minMarks: parseFloat(formData.minMarks) || null,
      });
      setShowForm(false);
      setFormData({ name: '', minIncome: '', maxIncome: '', category: 'GENERAL', minMarks: '', state: '', deadline: '', description: '' });
      fetchScholarships();
    } catch (err) {
      alert('Failed to create scholarship');
      console.error(err);
    }
  };

  const handleUserChange = (e) => {
    setUserFormData({ ...userFormData, [e.target.name]: e.target.value });
  };

  const handleUserSubmit = async (e) => {
    e.preventDefault();
    try {
      await axiosClient.post('/auth/register', {
        ...userFormData,
        year: userFormData.year ? parseInt(userFormData.year) : null,
        annualIncome: userFormData.annualIncome ? parseFloat(userFormData.annualIncome) : null,
      });
      alert('User created successfully!');
      setShowUserForm(false);
      setUserFormData({ username: '', password: '', role: 'STUDENT', name: '', department: '', rollNo: '', year: '', category: 'GENERAL', annualIncome: '', state: '' });
    } catch (err) {
      alert('Failed to create user — username may already exist');
      console.error(err);
    }
  };

  const inputClass = "bg-white/5 border border-white/10 rounded-lg px-3 py-2 text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-violet-500";

  return (
    <div className="min-h-screen bg-slate-950 relative overflow-hidden">
      <div className="absolute top-[-10%] left-[-10%] w-[500px] h-[500px] bg-violet-600/20 rounded-full blur-3xl animate-float pointer-events-none" />
      <div className="absolute bottom-[-10%] right-[-10%] w-[500px] h-[500px] bg-blue-600/20 rounded-full blur-3xl animate-float pointer-events-none" style={{ animationDelay: '2s' }} />

      <div className="relative z-10">
        <Navbar />
        <div className="max-w-5xl mx-auto p-6">

          {/* Scholarships Section */}
          <div className="flex justify-between items-center mb-6">
            <h1 className="text-2xl font-bold text-white">Scholarships</h1>
            <button
              onClick={() => setShowForm(!showForm)}
              className="flex items-center gap-1 bg-gradient-to-r from-violet-600 to-blue-600 text-white px-4 py-2 rounded-lg hover:opacity-90 transition shadow-lg shadow-violet-600/20"
            >
              {showForm ? <X size={18} /> : <Plus size={18} />}
              {showForm ? 'Cancel' : 'Add Scholarship'}
            </button>
          </div>

          {showForm && (
            <form onSubmit={handleSubmit} className="bg-white/5 backdrop-blur border border-white/10 p-6 rounded-xl mb-6 grid grid-cols-2 gap-4">
              <input name="name" placeholder="Scholarship Name" value={formData.name} onChange={handleChange} required className={`${inputClass} col-span-2`} />
              <input name="minIncome" type="number" placeholder="Min Income" value={formData.minIncome} onChange={handleChange} className={inputClass} />
              <input name="maxIncome" type="number" placeholder="Max Income" value={formData.maxIncome} onChange={handleChange} className={inputClass} />
              <select name="category" value={formData.category} onChange={handleChange} className={inputClass}>
                <option value="GENERAL" className="bg-slate-900">GENERAL</option>
                <option value="OBC" className="bg-slate-900">OBC</option>
                <option value="SC" className="bg-slate-900">SC</option>
                <option value="ST" className="bg-slate-900">ST</option>
                <option value="ANY" className="bg-slate-900">ANY</option>
              </select>
              <input name="minMarks" type="number" placeholder="Min Marks %" value={formData.minMarks} onChange={handleChange} className={inputClass} />
              <input name="state" placeholder="State" value={formData.state} onChange={handleChange} className={inputClass} />
              <input name="deadline" type="date" value={formData.deadline} onChange={handleChange} required className={inputClass} />
              <textarea name="description" placeholder="Description" value={formData.description} onChange={handleChange} className={`${inputClass} col-span-2`} rows={3} />
              <button type="submit" className="col-span-2 bg-gradient-to-r from-violet-600 to-blue-600 text-white py-2 rounded-lg hover:opacity-90 transition shadow-lg shadow-violet-600/20">
                Create Scholarship
              </button>
            </form>
          )}

          {loading ? (
            <p className="text-slate-400">Loading...</p>
          ) : (
            <div className="bg-white/5 backdrop-blur border border-white/10 rounded-xl overflow-hidden">
              <table className="w-full text-left text-sm">
                <thead className="bg-white/5 text-slate-400">
                  <tr>
                    <th className="px-4 py-3">Name</th>
                    <th className="px-4 py-3">Category</th>
                    <th className="px-4 py-3">Income Range</th>
                    <th className="px-4 py-3">Min Marks</th>
                    <th className="px-4 py-3">State</th>
                    <th className="px-4 py-3">Deadline</th>
                  </tr>
                </thead>
                <tbody>
                  {scholarships.map((s) => (
                    <tr key={s.id} className="border-t border-white/10">
                      <td className="px-4 py-3 font-medium text-white">{s.name}</td>
                      <td className="px-4 py-3 text-slate-300">{s.category}</td>
                      <td className="px-4 py-3 text-slate-300">₹{s.minIncome ?? 0} - ₹{s.maxIncome ?? '∞'}</td>
                      <td className="px-4 py-3 text-slate-300">{s.minMarks ?? '-'}%</td>
                      <td className="px-4 py-3 text-slate-300">{s.state ?? 'Any'}</td>
                      <td className="px-4 py-3 text-slate-300">{s.deadline}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
              {scholarships.length === 0 && (
                <p className="text-center text-slate-500 py-8">No scholarships yet</p>
              )}
            </div>
          )}

          {/* Create User Account Section */}
          <div className="flex justify-between items-center mb-6 mt-10">
            <h2 className="text-xl font-bold text-white">Create User Account</h2>
            <button
              onClick={() => setShowUserForm(!showUserForm)}
              className="flex items-center gap-1 bg-gradient-to-r from-emerald-600 to-teal-600 text-white px-4 py-2 rounded-lg hover:opacity-90 transition shadow-lg shadow-emerald-600/20"
            >
              {showUserForm ? <X size={18} /> : <UserPlus size={18} />}
              {showUserForm ? 'Cancel' : 'Create Account'}
            </button>
          </div>

          {showUserForm && (
            <form onSubmit={handleUserSubmit} className="bg-white/5 backdrop-blur border border-white/10 p-6 rounded-xl mb-6 grid grid-cols-2 gap-4">
              <input name="username" placeholder="Username" value={userFormData.username} onChange={handleUserChange} required className={inputClass} />
              <input name="password" type="password" placeholder="Password" value={userFormData.password} onChange={handleUserChange} required className={inputClass} />
              <select name="role" value={userFormData.role} onChange={handleUserChange} className={inputClass}>
                <option value="STUDENT" className="bg-slate-900">STUDENT</option>
                <option value="MENTOR" className="bg-slate-900">MENTOR</option>
                <option value="ADMIN" className="bg-slate-900">ADMIN</option>
              </select>
              <input name="name" placeholder="Full Name" value={userFormData.name} onChange={handleUserChange} className={inputClass} />
              <input name="department" placeholder="Department" value={userFormData.department} onChange={handleUserChange} className={inputClass} />

              {userFormData.role === 'STUDENT' && (
                <>
                  <input name="rollNo" placeholder="Roll No" value={userFormData.rollNo} onChange={handleUserChange} className={inputClass} />
                  <input name="year" type="number" placeholder="Year" value={userFormData.year} onChange={handleUserChange} className={inputClass} />
                  <select name="category" value={userFormData.category} onChange={handleUserChange} className={inputClass}>
                    <option value="GENERAL" className="bg-slate-900">GENERAL</option>
                    <option value="OBC" className="bg-slate-900">OBC</option>
                    <option value="SC" className="bg-slate-900">SC</option>
                    <option value="ST" className="bg-slate-900">ST</option>
                  </select>
                  <input name="annualIncome" type="number" placeholder="Annual Income" value={userFormData.annualIncome} onChange={handleUserChange} className={inputClass} />
                  <input name="state" placeholder="State" value={userFormData.state} onChange={handleUserChange} className={`${inputClass} col-span-2`} />
                </>
              )}

              <button type="submit" className="col-span-2 bg-gradient-to-r from-emerald-600 to-teal-600 text-white py-2 rounded-lg hover:opacity-90 transition">
                Create Account
              </button>
            </form>
          )}

        </div>
      </div>
    </div>
  );
}