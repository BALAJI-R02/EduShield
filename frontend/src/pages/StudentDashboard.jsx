import { useState, useEffect } from 'react';
import axiosClient from '../api/axiosClient';
import Navbar from '../components/Navbar';
import { CheckCircle2, AlertCircle, GraduationCap } from 'lucide-react';

const statusMessage = (level) => {
  if (level === 'HIGH') return { text: "Let's work together to get back on track", color: 'text-amber-300', bg: 'bg-amber-500/10 border border-amber-500/20', icon: <AlertCircle className="text-amber-300" size={20} /> };
  if (level === 'MEDIUM') return { text: "You're doing okay, small improvements can help", color: 'text-amber-200', bg: 'bg-amber-500/10 border border-amber-500/20', icon: <AlertCircle className="text-amber-200" size={20} /> };
  return { text: "You're doing great, keep it up!", color: 'text-emerald-300', bg: 'bg-emerald-500/10 border border-emerald-500/20', icon: <CheckCircle2 className="text-emerald-300" size={20} /> };
};

export default function StudentDashboard() {
  const [student, setStudent] = useState(null);
  const [risk, setRisk] = useState(null);
  const [scholarships, setScholarships] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const studentRes = await axiosClient.get('/students/me');
        const studentId = studentRes.data.id;

        const [riskRes, scholarshipRes] = await Promise.all([
          axiosClient.post(`/risk/compute/${studentId}`),
          axiosClient.get(`/scholarships/match/${studentId}`),
        ]);
        setStudent(studentRes.data);
        setRisk(riskRes.data);
        setScholarships(scholarshipRes.data);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  if (loading) return (
    <div className="min-h-screen bg-slate-950 flex items-center justify-center text-slate-400">Loading...</div>
  );

  const status = risk ? statusMessage(risk.riskLevel) : null;

  return (
    <div className="min-h-screen bg-slate-950 relative overflow-hidden">
      <div className="absolute top-[-10%] left-[-10%] w-[500px] h-[500px] bg-violet-600/20 rounded-full blur-3xl animate-float pointer-events-none" />
      <div className="absolute bottom-[-10%] right-[-10%] w-[500px] h-[500px] bg-blue-600/20 rounded-full blur-3xl animate-float pointer-events-none" style={{ animationDelay: '2s' }} />

      <div className="relative z-10">
        <Navbar />
        <div className="max-w-3xl mx-auto p-6">
          <div className="flex items-center gap-3 mb-6">
            <div className="bg-violet-500/10 border border-violet-500/20 p-3 rounded-full">
              <GraduationCap className="text-violet-300" size={24} />
            </div>
            <div>
              <h1 className="text-2xl font-bold text-white">Welcome, {student?.name}</h1>
              <p className="text-slate-400 text-sm">{student?.rollNo} · {student?.department} · Year {student?.year}</p>
            </div>
          </div>

          {status && (
            <div className={`${status.bg} rounded-xl p-4 flex items-center gap-3 mb-6`}>
              {status.icon}
              <p className={`font-medium ${status.color}`}>{status.text}</p>
            </div>
          )}

          <div className="grid grid-cols-2 gap-4 mb-6">
            <div className="bg-white/5 backdrop-blur border border-white/10 p-4 rounded-xl">
              <p className="text-slate-400 text-sm mb-1">Attendance</p>
              <p className="text-2xl font-bold text-white">{risk?.attendancePct ?? '-'}%</p>
            </div>
            <div className="bg-white/5 backdrop-blur border border-white/10 p-4 rounded-xl">
              <p className="text-slate-400 text-sm mb-1">Marks Average</p>
              <p className="text-2xl font-bold text-white">{risk?.marksAvg ?? '-'}%</p>
            </div>
          </div>

          <div className="bg-white/5 backdrop-blur border border-white/10 p-6 rounded-xl">
            <h2 className="font-semibold text-white mb-4">Scholarships You May Be Eligible For</h2>
            {scholarships.length > 0 ? (
              <ul className="space-y-3">
                {scholarships.map((s) => (
                  <li key={s.scholarshipId} className="border border-white/10 rounded-lg p-4">
                    <p className="font-medium text-white">{s.scholarshipName}</p>
                    <p className="text-sm text-slate-400 mt-1">{s.description}</p>
                    <p className="text-xs text-slate-500 mt-2">Deadline: {s.deadline}</p>
                  </li>
                ))}
              </ul>
            ) : (
              <p className="text-slate-500 text-sm">No matching scholarships found right now</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}