import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { ShieldCheck, TrendingUp, GraduationCap, Users, ArrowRight, AlertTriangle } from 'lucide-react';

const fadeUp = {
  hidden: { opacity: 0, y: 24 },
  visible: (i = 0) => ({
    opacity: 1,
    y: 0,
    transition: { delay: i * 0.15, duration: 0.6, ease: 'easeOut' },
  }),
};

export default function Landing() {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-slate-950 text-white overflow-hidden relative">
      {/* Background glow blobs */}
      <div className="absolute top-[-10%] left-[-10%] w-[500px] h-[500px] bg-violet-600/30 rounded-full blur-3xl animate-float" />
      <div className="absolute bottom-[-10%] right-[-10%] w-[500px] h-[500px] bg-blue-600/30 rounded-full blur-3xl animate-float" style={{ animationDelay: '2s' }} />
      <div className="absolute top-[30%] right-[20%] w-[300px] h-[300px] bg-fuchsia-500/20 rounded-full blur-3xl animate-float" style={{ animationDelay: '4s' }} />

      <div className="relative z-10">
        {/* Nav */}
        <nav className="max-w-6xl mx-auto px-6 py-5 flex justify-between items-center">
          <div className="flex items-center gap-2">
            <ShieldCheck className="text-violet-400" size={26} />
            <span className="font-bold text-xl">EduShield</span>
          </div>
          <button
            onClick={() => navigate('/login')}
            className="bg-white text-slate-900 px-5 py-2 rounded-lg font-medium hover:bg-slate-100 transition"
          >
            Sign In
          </button>
        </nav>

        {/* Hero */}
        <div className="max-w-6xl mx-auto px-6 pt-20 pb-24 text-center">
          <motion.div
            initial="hidden" animate="visible" custom={0} variants={fadeUp}
            className="inline-flex items-center gap-2 bg-white/10 backdrop-blur border border-white/10 text-violet-200 px-4 py-1.5 rounded-full text-sm font-medium mb-6"
          >
            <AlertTriangle size={14} /> Early intervention saves students
          </motion.div>

          <motion.h1
            initial="hidden" animate="visible" custom={1} variants={fadeUp}
            className="text-5xl md:text-6xl font-bold mb-4 leading-tight bg-gradient-to-r from-white via-violet-200 to-blue-200 bg-clip-text text-transparent animate-gradient"
          >
            Catch students at risk<br />before they drop out
          </motion.h1>

          <motion.p
            initial="hidden" animate="visible" custom={2} variants={fadeUp}
            className="text-lg text-slate-400 max-w-xl mx-auto mb-8"
          >
            EduShield tracks attendance, marks, and fee status to flag at-risk students early —
            then automatically matches them with scholarships that could keep them in school.
          </motion.p>

          <motion.button
            initial="hidden" animate="visible" custom={3} variants={fadeUp}
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.97 }}
            onClick={() => navigate('/login')}
            className="inline-flex items-center gap-2 bg-gradient-to-r from-violet-600 to-blue-600 text-white px-6 py-3 rounded-lg font-medium text-lg shadow-lg shadow-violet-600/30"
          >
            Get Started <ArrowRight size={20} />
          </motion.button>
        </div>

        {/* Features */}
        <div className="max-w-6xl mx-auto px-6 pb-24 grid grid-cols-1 md:grid-cols-3 gap-6">
          {[
            {
              icon: <TrendingUp className="text-rose-300" size={22} />,
              bg: 'bg-rose-500/10',
              title: 'Risk Scoring',
              desc: "A weighted formula combining attendance, marks, and fee status flags students before problems become irreversible.",
            },
            {
              icon: <GraduationCap className="text-emerald-300" size={22} />,
              bg: 'bg-emerald-500/10',
              title: 'Scholarship Matching',
              desc: "At-risk students are automatically matched with scholarships they're eligible for — turning a flag into a real solution.",
            },
            {
              icon: <Users className="text-blue-300" size={22} />,
              bg: 'bg-blue-500/10',
              title: 'Mentor Dashboard',
              desc: "Mentors get a clear view of every assigned student's risk trend and can log interventions directly.",
            },
          ].map((f, i) => (
            <motion.div
              key={f.title}
              initial="hidden" whileInView="visible" viewport={{ once: true }} custom={i} variants={fadeUp}
              whileHover={{ y: -6 }}
              className="bg-white/5 backdrop-blur border border-white/10 rounded-2xl p-6 transition"
            >
              <div className={`${f.bg} w-12 h-12 rounded-xl flex items-center justify-center mb-4`}>
                {f.icon}
              </div>
              <h3 className="font-semibold text-white text-lg mb-2">{f.title}</h3>
              <p className="text-slate-400 text-sm">{f.desc}</p>
            </motion.div>
          ))}
        </div>

        {/* Footer */}
        <div className="border-t border-white/10 py-6 text-center text-slate-500 text-sm">
          Built for final year project · EduShield
        </div>
      </div>
    </div>
  );
}