import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axiosClient from '../api/axiosClient';
import Navbar from '../components/Navbar';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { ArrowLeft } from 'lucide-react';

export default function StudentDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [student, setStudent] = useState(null);
  const [riskHistory, setRiskHistory] = useState([]);
  const [scholarships, setScholarships] = useState([]);
  const [logs, setLogs] = useState([]);
  const [note, setNote] = useState('');
  const [mentorId, setMentorId] = useState(null);

  useEffect(() => {
    axiosClient.get('/mentors/me').then(res => setMentorId(res.data.id));
  }, []);

  const fetchAll = async () => {
    try {
      const [studentRes, historyRes, scholarshipRes, logsRes] = await Promise.all([
        axiosClient.get(`/students/${id}`),
        axiosClient.get(`/risk/history/${id}`),
        axiosClient.get(`/scholarships/match/${id}`),
        axiosClient.get(`/interventions/student/${id}`),
      ]);
      setStudent(studentRes.data);
      setRiskHistory(historyRes.data.map(r => ({
        date: new Date(r.computedAt).toLocaleDateString(),
        risk: r.riskScoreValue
      })));
      setScholarships(scholarshipRes.data);
      setLogs(logsRes.data);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => { fetchAll(); }, [id]);

  const handleAddLog = async (e) => {
    e.preventDefault();
    if (!note.trim() || !mentorId) return;
    try {
      await axiosClient.post('/interventions', {
        studentId: id,
        mentorId,
        note,
        logDate: new Date().toISOString().split('T')[0],
      });
      setNote('');
      fetchAll();
    } catch (err) {
      alert('Failed to add log');
    }
  };

  if (!student) return (
    <div className="min-h-screen bg-slate-950 flex items-center justify-center text-slate-400">Loading...</div>
  );

  return (
    <div className="min-h-screen bg-slate-950 relative overflow-hidden">
      <div className="absolute top-[-10%] left-[-10%] w-[500px] h-[500px] bg-violet-600/20 rounded-full blur-3xl animate-float pointer-events-none" />
      <div className="absolute bottom-[-10%] right-[-10%] w-[500px] h-[500px] bg-blue-600/20 rounded-full blur-3xl animate-float pointer-events-none" style={{ animationDelay: '2s' }} />

      <div className="relative z-10">
        <Navbar />
        <div className="max-w-4xl mx-auto p-6">
          <button onClick={() => navigate(-1)} className="flex items-center gap-1 text-slate-400 hover:text-slate-200 mb-4 text-sm">
            <ArrowLeft size={16} /> Back
          </button>

          <h1 className="text-2xl font-bold text-white mb-1">{student.name}</h1>
          <p className="text-slate-400 mb-6">{student.rollNo} · {student.department} · Year {student.year}</p>

          <div className="bg-white/5 backdrop-blur border border-white/10 p-6 rounded-xl mb-6">
            <h2 className="font-semibold text-white mb-4">Risk Trend</h2>
            {riskHistory.length > 0 ? (
              <ResponsiveContainer width="100%" height={250}>
                <LineChart data={riskHistory}>
                  <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.1)" />
                  <XAxis dataKey="date" tick={{ fontSize: 12, fill: '#94a3b8' }} />
                  <YAxis tick={{ fontSize: 12, fill: '#94a3b8' }} />
                  <Tooltip contentStyle={{ backgroundColor: '#1e1b2e', border: '1px solid rgba(255,255,255,0.1)', borderRadius: 8, color: '#fff' }} />
                  <Line type="monotone" dataKey="risk" stroke="#a78bfa" strokeWidth={2} />
                </LineChart>
              </ResponsiveContainer>
            ) : (
              <p className="text-slate-500 text-sm">No risk history yet</p>
            )}
          </div>

          <div className="bg-white/5 backdrop-blur border border-white/10 p-6 rounded-xl mb-6">
            <h2 className="font-semibold text-white mb-4">Eligible Scholarships</h2>
            {scholarships.length > 0 ? (
              <ul className="space-y-2">
                {scholarships.map((s) => (
                  <li key={s.scholarshipId} className="border border-white/10 rounded-lg p-3">
                    <p className="font-medium text-white">{s.scholarshipName}</p>
                    <p className="text-sm text-slate-400">{s.description}</p>
                    <p className="text-xs text-slate-500 mt-1">Deadline: {s.deadline} · Status: {s.appliedStatus}</p>
                  </li>
                ))}
              </ul>
            ) : (
              <p className="text-slate-500 text-sm">No matching scholarships</p>
            )}
          </div>

          <div className="bg-white/5 backdrop-blur border border-white/10 p-6 rounded-xl">
            <h2 className="font-semibold text-white mb-4">Intervention Logs</h2>
            <form onSubmit={handleAddLog} className="flex gap-2 mb-4">
              <input
                value={note}
                onChange={(e) => setNote(e.target.value)}
                placeholder="Add a note..."
                className="flex-1 bg-white/5 border border-white/10 rounded-lg px-3 py-2 text-sm text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-violet-500"
              />
              <button type="submit" className="bg-gradient-to-r from-violet-600 to-blue-600 text-white px-4 py-2 rounded-lg text-sm hover:opacity-90">
                Add
              </button>
            </form>
            <ul className="space-y-2">
              {logs.map((log) => (
                <li key={log.id} className="text-sm border-l-2 border-violet-500/40 pl-3">
                  <p className="text-slate-200">{log.note}</p>
                  <p className="text-xs text-slate-500">{log.logDate} · {log.mentorName}</p>
                </li>
              ))}
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
}