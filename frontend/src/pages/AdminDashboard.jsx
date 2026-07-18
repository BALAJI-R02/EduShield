import { useState, useEffect } from 'react';
import axiosClient from '../api/axiosClient';
import Navbar from '../components/Navbar';
import { Plus, X, UserPlus, Upload, Trash2 } from 'lucide-react';

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

  const [csvFile, setCsvFile] = useState(null);
  const [uploadResult, setUploadResult] = useState(null);
  const [uploading, setUploading] = useState(false);

  const [mentorCsvFile, setMentorCsvFile] = useState(null);
  const [mentorUploadResult, setMentorUploadResult] = useState(null);
  const [mentorUploading, setMentorUploading] = useState(false);

  const [scholarshipCsvFile, setScholarshipCsvFile] = useState(null);
  const [scholarshipUploadResult, setScholarshipUploadResult] = useState(null);
  const [scholarshipUploading, setScholarshipUploading] = useState(false);
  const [attendanceCsvFile, setAttendanceCsvFile] = useState(null);
const [attendanceUploadResult, setAttendanceUploadResult] = useState(null);
const [attendanceUploading, setAttendanceUploading] = useState(false);

const [marksCsvFile, setMarksCsvFile] = useState(null);
const [marksUploadResult, setMarksUploadResult] = useState(null);
const [marksUploading, setMarksUploading] = useState(false);

const [feesCsvFile, setFeesCsvFile] = useState(null);
const [feesUploadResult, setFeesUploadResult] = useState(null);
const [feesUploading, setFeesUploading] = useState(false);

const handleAttendanceCsvUpload = async (e) => {
  e.preventDefault();
  if (!attendanceCsvFile) return;
  const uploadFormData = new FormData();
  uploadFormData.append('file', attendanceCsvFile);
  setAttendanceUploading(true);
  setAttendanceUploadResult(null);
  try {
    const res = await axiosClient.post('/admin/students/bulk-upload-attendance', uploadFormData);
    setAttendanceUploadResult(res.data);
    setAttendanceCsvFile(null);
  } catch (err) {
    const status = err?.response?.status;
    const msg = err?.response?.data || err?.message || 'Network error';
    alert(`Attendance upload failed\nStatus: ${status}\n${JSON.stringify(msg)}`);
    console.error('Attendance upload error:', err);
  } finally {
    setAttendanceUploading(false);
  }
};

const handleMarksCsvUpload = async (e) => {
  e.preventDefault();
  if (!marksCsvFile) return;
  const uploadFormData = new FormData();
  uploadFormData.append('file', marksCsvFile);
  setMarksUploading(true);
  setMarksUploadResult(null);
  try {
    const res = await axiosClient.post('/admin/students/bulk-upload-marks', uploadFormData);
    setMarksUploadResult(res.data);
    setMarksCsvFile(null);
  } catch (err) {
    const status = err?.response?.status;
    const msg = err?.response?.data || err?.message || 'Network error';
    alert(`Attendance upload failed\nStatus: ${status}\n${JSON.stringify(msg)}`);
    console.error('Attendance upload error:', err);
  } finally {
    setMarksUploading(false);
  }
};

const handleFeesCsvUpload = async (e) => {
  e.preventDefault();
  if (!feesCsvFile) return;
  const uploadFormData = new FormData();
  uploadFormData.append('file', feesCsvFile);
  setFeesUploading(true);
  setFeesUploadResult(null);
  try {
    const res = await axiosClient.post('/admin/students/bulk-upload-fees', uploadFormData);
    setFeesUploadResult(res.data);
    setFeesCsvFile(null);
  } catch (err) {
    const status = err?.response?.status;
    const msg = err?.response?.data || err?.message || 'Network error';
    alert(`Attendance upload failed\nStatus: ${status}\n${JSON.stringify(msg)}`);
    console.error('Attendance upload error:', err);
  } finally {
    setFeesUploading(false);
  }
};



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

  const handleDeleteScholarship = async (id, name) => {
    if (!window.confirm(`Delete scholarship "${name}"? This cannot be undone.`)) return;
    try {
      await axiosClient.delete(`/scholarships/${id}`);
      fetchScholarships();
    } catch (err) {
      alert('Failed to delete scholarship');
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

  const handleCsvUpload = async (e) => {
    e.preventDefault();
    if (!csvFile) return;

    const uploadFormData = new FormData();
    uploadFormData.append('file', csvFile);

    setUploading(true);
    setUploadResult(null);
    try {
      const res = await axiosClient.post('/admin/students/bulk-upload', uploadFormData);
      setUploadResult(res.data);
      setCsvFile(null);
    } catch (err) {
      const status = err?.response?.status;
      const msg = err?.response?.data || err?.message || 'Network error';
      alert(`Student upload failed\nStatus: ${status}\n${JSON.stringify(msg)}`);
      console.error('Student upload error:', err);
    } finally {
      setUploading(false);
    }
  };

  const handleMentorCsvUpload = async (e) => {
    e.preventDefault();
    if (!mentorCsvFile) return;

    const uploadFormData = new FormData();
    uploadFormData.append('file', mentorCsvFile);

    setMentorUploading(true);
    setMentorUploadResult(null);
    try {
      const res = await axiosClient.post('/admin/students/bulk-upload-mentors', uploadFormData);
      setMentorUploadResult(res.data);
      setMentorCsvFile(null);
    } catch (err) {
      const status = err?.response?.status;
      const msg = err?.response?.data || err?.message || 'Network error';
      alert(`Mentor upload failed\nStatus: ${status}\n${JSON.stringify(msg)}`);
      console.error('Mentor upload error:', err);
    } finally {
      setMentorUploading(false);
    }
  };

  const handleScholarshipCsvUpload = async (e) => {
    e.preventDefault();
    if (!scholarshipCsvFile) return;

    const uploadFormData = new FormData();
    uploadFormData.append('file', scholarshipCsvFile);

    setScholarshipUploading(true);
    setScholarshipUploadResult(null);
    try {
      const res = await axiosClient.post('/admin/students/bulk-upload-scholarships', uploadFormData);
      setScholarshipUploadResult(res.data);
      setScholarshipCsvFile(null);
      fetchScholarships();
    } catch (err) {
      const status = err?.response?.status;
      const msg = err?.response?.data || err?.message || 'Network error';
      alert(`Scholarship upload failed\nStatus: ${status}\n${JSON.stringify(msg)}`);
      console.error('Scholarship upload error:', err);
    } finally {
      setScholarshipUploading(false);
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
                    <th className="px-4 py-3">Action</th>
                  </tr>
                </thead>
                <tbody>
                  {scholarships.map((s) => (
                    <tr key={s.id} className="border-t border-white/10 hover:bg-white/5 transition">
                      <td className="px-4 py-3 font-medium text-white">{s.name}</td>
                      <td className="px-4 py-3 text-slate-300">{s.category}</td>
                      <td className="px-4 py-3 text-slate-300">₹{s.minIncome ?? 0} - ₹{s.maxIncome ?? '∞'}</td>
                      <td className="px-4 py-3 text-slate-300">{s.minMarks ?? '-'}%</td>
                      <td className="px-4 py-3 text-slate-300">{s.state ?? 'Any'}</td>
                      <td className="px-4 py-3 text-slate-300">{s.deadline}</td>
                      <td className="px-4 py-3">
                        <button
                          onClick={() => handleDeleteScholarship(s.id, s.name)}
                          className="flex items-center gap-1 text-red-400 hover:text-red-300 hover:bg-red-500/10 px-2 py-1 rounded-lg transition text-sm"
                          title="Delete scholarship"
                        >
                          <Trash2 size={14} /> Delete
                        </button>
                      </td>
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

          {/* Bulk Upload Students */}
          <div className="flex justify-between items-center mb-6 mt-10">
            <h2 className="text-xl font-bold text-white">Bulk Upload Students (CSV)</h2>
          </div>

          <div className="bg-white/5 backdrop-blur border border-white/10 p-6 rounded-xl mb-6">
            <p className="text-slate-400 text-sm mb-4">
              CSV columns: username,password,name,rollNo,department,year,category,annualIncome,state,mentorUsername (optional)
            </p>
            <form onSubmit={handleCsvUpload} className="flex items-center gap-3">
              <input
                type="file"
                accept=".csv"
                onChange={(e) => setCsvFile(e.target.files[0])}
                className="text-slate-300 text-sm file:mr-4 file:py-2 file:px-4 file:rounded-lg file:border-0 file:bg-violet-600 file:text-white file:cursor-pointer"
              />
              <button
                type="submit"
                disabled={!csvFile || uploading}
                className="flex items-center gap-1 bg-gradient-to-r from-violet-600 to-blue-600 text-white px-4 py-2 rounded-lg hover:opacity-90 transition disabled:opacity-50"
              >
                <Upload size={16} /> {uploading ? 'Uploading...' : 'Upload'}
              </button>
            </form>

            {uploadResult && (
              <div className="mt-4 p-4 bg-white/5 rounded-lg">
                <p className="text-emerald-300 font-medium">{uploadResult.successCount} students created successfully</p>
                {uploadResult.errors.length > 0 && (
                  <div className="mt-2">
                    <p className="text-amber-300 text-sm font-medium">{uploadResult.errors.length} issues:</p>
                    <ul className="text-slate-400 text-xs mt-1 space-y-1">
                      {uploadResult.errors.map((err, i) => (
                        <li key={i}>• {err}</li>
                      ))}
                    </ul>
                  </div>
                )}
              </div>
            )}
          </div>

          {/* Bulk Upload Mentors */}
          <div className="flex justify-between items-center mb-6 mt-10">
            <h2 className="text-xl font-bold text-white">Bulk Upload Mentors (CSV)</h2>
          </div>

          <div className="bg-white/5 backdrop-blur border border-white/10 p-6 rounded-xl mb-6">
            <p className="text-slate-400 text-sm mb-4">
              CSV columns: username,password,name,department
            </p>
            <form onSubmit={handleMentorCsvUpload} className="flex items-center gap-3">
              <input
                type="file"
                accept=".csv"
                onChange={(e) => setMentorCsvFile(e.target.files[0])}
                className="text-slate-300 text-sm file:mr-4 file:py-2 file:px-4 file:rounded-lg file:border-0 file:bg-violet-600 file:text-white file:cursor-pointer"
              />
              <button
                type="submit"
                disabled={!mentorCsvFile || mentorUploading}
                className="flex items-center gap-1 bg-gradient-to-r from-violet-600 to-blue-600 text-white px-4 py-2 rounded-lg hover:opacity-90 transition disabled:opacity-50"
              >
                <Upload size={16} /> {mentorUploading ? 'Uploading...' : 'Upload'}
              </button>
            </form>

            {mentorUploadResult && (
              <div className="mt-4 p-4 bg-white/5 rounded-lg">
                <p className="text-emerald-300 font-medium">{mentorUploadResult.successCount} mentors created successfully</p>
                {mentorUploadResult.errors.length > 0 && (
                  <div className="mt-2">
                    <p className="text-amber-300 text-sm font-medium">{mentorUploadResult.errors.length} issues:</p>
                    <ul className="text-slate-400 text-xs mt-1 space-y-1">
                      {mentorUploadResult.errors.map((err, i) => (
                        <li key={i}>• {err}</li>
                      ))}
                    </ul>
                  </div>
                )}
              </div>
            )}
          </div>

          {/* Bulk Upload Scholarships */}
          <div className="flex justify-between items-center mb-6 mt-10">
            <h2 className="text-xl font-bold text-white">Bulk Upload Scholarships (CSV)</h2>
          </div>

          <div className="bg-white/5 backdrop-blur border border-white/10 p-6 rounded-xl mb-6">
            <p className="text-slate-400 text-sm mb-4">
              CSV columns: name,minIncome,maxIncome,category,minMarks,state,deadline,description
            </p>
            <form onSubmit={handleScholarshipCsvUpload} className="flex items-center gap-3">
              <input
                type="file"
                accept=".csv"
                onChange={(e) => setScholarshipCsvFile(e.target.files[0])}
                className="text-slate-300 text-sm file:mr-4 file:py-2 file:px-4 file:rounded-lg file:border-0 file:bg-violet-600 file:text-white file:cursor-pointer"
              />
              <button
                type="submit"
                disabled={!scholarshipCsvFile || scholarshipUploading}
                className="flex items-center gap-1 bg-gradient-to-r from-violet-600 to-blue-600 text-white px-4 py-2 rounded-lg hover:opacity-90 transition disabled:opacity-50"
              >
                <Upload size={16} /> {scholarshipUploading ? 'Uploading...' : 'Upload'}
              </button>
            </form>

            {scholarshipUploadResult && (
              <div className="mt-4 p-4 bg-white/5 rounded-lg">
                <p className="text-emerald-300 font-medium">{scholarshipUploadResult.successCount} scholarships created successfully</p>
                {scholarshipUploadResult.errors.length > 0 && (
                  <div className="mt-2">
                    <p className="text-amber-300 text-sm font-medium">{scholarshipUploadResult.errors.length} issues:</p>
                    <ul className="text-slate-400 text-xs mt-1 space-y-1">
                      {scholarshipUploadResult.errors.map((err, i) => (
                        <li key={i}>• {err}</li>
                      ))}
                    </ul>
                  </div>
                )}
              </div>
            )}
          </div>
              {/* Bulk Upload Attendance */}
<div className="flex justify-between items-center mb-6 mt-10">
  <h2 className="text-xl font-bold text-white">Bulk Upload Attendance (CSV)</h2>
</div>
<div className="bg-white/5 backdrop-blur border border-white/10 p-6 rounded-xl mb-6">
  <p className="text-slate-400 text-sm mb-4">CSV columns: rollNo,month,year,totalDays,presentDays</p>
  <form onSubmit={handleAttendanceCsvUpload} className="flex items-center gap-3">
    <input type="file" accept=".csv" onChange={(e) => setAttendanceCsvFile(e.target.files[0])}
      className="text-slate-300 text-sm file:mr-4 file:py-2 file:px-4 file:rounded-lg file:border-0 file:bg-violet-600 file:text-white file:cursor-pointer" />
    <button type="submit" disabled={!attendanceCsvFile || attendanceUploading}
      className="flex items-center gap-1 bg-gradient-to-r from-violet-600 to-blue-600 text-white px-4 py-2 rounded-lg hover:opacity-90 transition disabled:opacity-50">
      <Upload size={16} /> {attendanceUploading ? 'Uploading...' : 'Upload'}
    </button>
  </form>
  {attendanceUploadResult && (
    <div className="mt-4 p-4 bg-white/5 rounded-lg">
      <p className="text-emerald-300 font-medium">{attendanceUploadResult.successCount} attendance records created</p>
      {attendanceUploadResult.errors.length > 0 && (
        <div className="mt-2">
          <p className="text-amber-300 text-sm font-medium">{attendanceUploadResult.errors.length} issues:</p>
          <ul className="text-slate-400 text-xs mt-1 space-y-1">
            {attendanceUploadResult.errors.map((err, i) => <li key={i}>• {err}</li>)}
          </ul>
        </div>
      )}
    </div>
  )}
</div>

{/* Bulk Upload Marks */}
<div className="flex justify-between items-center mb-6 mt-10">
  <h2 className="text-xl font-bold text-white">Bulk Upload Marks (CSV)</h2>
</div>
<div className="bg-white/5 backdrop-blur border border-white/10 p-6 rounded-xl mb-6">
  <p className="text-slate-400 text-sm mb-4">CSV columns: rollNo,subject,assessmentType,score,maxScore,assessmentDate</p>
  <form onSubmit={handleMarksCsvUpload} className="flex items-center gap-3">
    <input type="file" accept=".csv" onChange={(e) => setMarksCsvFile(e.target.files[0])}
      className="text-slate-300 text-sm file:mr-4 file:py-2 file:px-4 file:rounded-lg file:border-0 file:bg-violet-600 file:text-white file:cursor-pointer" />
    <button type="submit" disabled={!marksCsvFile || marksUploading}
      className="flex items-center gap-1 bg-gradient-to-r from-violet-600 to-blue-600 text-white px-4 py-2 rounded-lg hover:opacity-90 transition disabled:opacity-50">
      <Upload size={16} /> {marksUploading ? 'Uploading...' : 'Upload'}
    </button>
  </form>
  {marksUploadResult && (
    <div className="mt-4 p-4 bg-white/5 rounded-lg">
      <p className="text-emerald-300 font-medium">{marksUploadResult.successCount} marks records created</p>
      {marksUploadResult.errors.length > 0 && (
        <div className="mt-2">
          <p className="text-amber-300 text-sm font-medium">{marksUploadResult.errors.length} issues:</p>
          <ul className="text-slate-400 text-xs mt-1 space-y-1">
            {marksUploadResult.errors.map((err, i) => <li key={i}>• {err}</li>)}
          </ul>
        </div>
      )}
    </div>
  )}
</div>

{/* Bulk Upload Fees */}
<div className="flex justify-between items-center mb-6 mt-10">
  <h2 className="text-xl font-bold text-white">Bulk Upload Fee Status (CSV)</h2>
</div>
<div className="bg-white/5 backdrop-blur border border-white/10 p-6 rounded-xl mb-6">
  <p className="text-slate-400 text-sm mb-4">CSV columns: rollNo,semester,amountDue,dueDate,paid (true/false)</p>
  <form onSubmit={handleFeesCsvUpload} className="flex items-center gap-3">
    <input type="file" accept=".csv" onChange={(e) => setFeesCsvFile(e.target.files[0])}
      className="text-slate-300 text-sm file:mr-4 file:py-2 file:px-4 file:rounded-lg file:border-0 file:bg-violet-600 file:text-white file:cursor-pointer" />
    <button type="submit" disabled={!feesCsvFile || feesUploading}
      className="flex items-center gap-1 bg-gradient-to-r from-violet-600 to-blue-600 text-white px-4 py-2 rounded-lg hover:opacity-90 transition disabled:opacity-50">
      <Upload size={16} /> {feesUploading ? 'Uploading...' : 'Upload'}
    </button>
  </form>
  {feesUploadResult && (
    <div className="mt-4 p-4 bg-white/5 rounded-lg">
      <p className="text-emerald-300 font-medium">{feesUploadResult.successCount} fee records created</p>
      {feesUploadResult.errors.length > 0 && (
        <div className="mt-2">
          <p className="text-amber-300 text-sm font-medium">{feesUploadResult.errors.length} issues:</p>
          <ul className="text-slate-400 text-xs mt-1 space-y-1">
            {feesUploadResult.errors.map((err, i) => <li key={i}>• {err}</li>)}
          </ul>
        </div>
      )}
    </div>
  )}
</div>
        </div>
      </div>
    </div>
  );
}