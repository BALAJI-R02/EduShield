import StudentDashboard from './pages/StudentDashboard';
import MentorDashboard from './pages/MentorDashboard';
import StudentDetail from './pages/StudentDetail';
import AdminDashboard from './pages/AdminDashboard';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Login from './pages/Login';
import Landing from './pages/Landing';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Landing />} />
          <Route path="/login" element={<Login />} />
          <Route path="/admin" element={
            <ProtectedRoute allowedRoles={['ADMIN']}>
              <AdminDashboard />
            </ProtectedRoute>
          } />
          <Route path="/mentor" element={
            <ProtectedRoute allowedRoles={['MENTOR', 'ADMIN']}>
              <MentorDashboard />
            </ProtectedRoute>
          } />
          <Route path="/mentor/student/:id" element={
            <ProtectedRoute allowedRoles={['MENTOR', 'ADMIN']}>
              <StudentDetail />
            </ProtectedRoute>
          } />
          <Route path="/student" element={
            <ProtectedRoute allowedRoles={['STUDENT', 'MENTOR', 'ADMIN']}>
              <StudentDashboard />
            </ProtectedRoute>
          } />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;