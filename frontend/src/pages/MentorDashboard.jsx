import { useState, useEffect } from 'react';
import axiosClient from '../api/axiosClient';
import Navbar from '../components/Navbar';
import { AlertTriangle, CheckCircle, AlertCircle } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const riskBadge = (level) => {
  if (level === 'HIGH') return <span className="flex items-center gap-1 bg-rose-500/10 text-rose-300 px-2 py-1 rounded-full text-xs font-medium border border-rose-500/20"><AlertTriangle size={12} /> HIGH</span>;
  if (level === 'MEDIUM') return <span className="flex items-center gap-1 bg-amber-500/10 text-amber-300 px-2 py-1 rounded-full text-xs font-medium border border-amber-500/20"><AlertCircle size={12} /> MEDIUM</span>;
  return <span className="flex items-center gap-1 bg-emerald-500/10 text-emerald-300 px-2 py-1 rounded-full text-xs font-medium border border-emerald-500/20"><CheckCircle size={12} /> LOW</span>;
};

export default function MentorDashboard() {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchStudentsAndRisk = async () => {
      try {
        const mentorRes = await axiosClient.get('/mentors/me');
        const mentorId = mentorRes.data.id;

        const res = await axiosClient.get(`/students/by-mentor/${mentorId}`);
        const studentsWithRisk = await Promise.all(
          res.data.map(async (student) => {
            try {
              const riskRes = await axiosClient.post(`/risk/compute/${student.id}`);
              return { ...student, risk: riskRes.data };
            } catch {
              return { ...student, risk: null };
            }
          })
        );
        setStudents(studentsWithRisk);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchStudentsAndRisk();
  }, []);

  return (
    <div className="min-h-screen bg-slate-950 relative overflow-hidden">
      <div className="absolute top-[-10%] left-[-10%] w-[500px] h-[500px] bg-violet-600/20 rounded-full blur-3xl animate-float pointer-events-none" />
      <div className="absolute bottom-[-10%] right-[-10%] w-[500px] h-[500px] bg-blue-600/20 rounded-full blur-3xl animate-float pointer-events-none" style={{ animationDelay: '2s' }} />

      <div className="relative z-10">
        <Navbar />
        <div className="max-w-5xl mx-auto p-6">
          <h1 className="text-2xl font-bold text-white mb-6">My Students</h1>

          {loading ? (
            <p className="text-slate-400">Loading students...</p>
          ) : (
            <div className="bg-white/5 backdrop-blur border border-white/10 rounded-xl overflow-hidden">
              <table className="w-full text-left text-sm">
                <thead className="bg-white/5 text-slate-400">
                  <tr>
                    <th className="px-4 py-3">Name</th>
                    <th className="px-4 py-3">Roll No</th>
                    <th className="px-4 py-3">Attendance</th>
                    <th className="px-4 py-3">Marks Avg</th>
                    <th className="px-4 py-3">Risk Level</th>
                    <th className="px-4 py-3"></th>
                  </tr>
                </thead>
                <tbody>
                  {students.map((s) => (
                    <tr key={s.id} className="border-t border-white/10">
                      <td className="px-4 py-3 font-medium text-white">{s.name}</td>
                      <td className="px-4 py-3 text-slate-300">{s.rollNo}</td>
                      <td className="px-4 py-3 text-slate-300">{s.risk?.attendancePct ?? '-'}%</td>
                      <td className="px-4 py-3 text-slate-300">{s.risk?.marksAvg ?? '-'}%</td>
                      <td className="px-4 py-3">{s.risk ? riskBadge(s.risk.riskLevel) : '-'}</td>
                      <td className="px-4 py-3">
                        <button
                          onClick={() => navigate(`/mentor/student/${s.id}`)}
                          className="text-violet-300 hover:text-violet-200 text-sm font-medium"
                        >
                          View Details →
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              {students.length === 0 && (
                <p className="text-center text-slate-500 py-8">No students assigned yet</p>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}