import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import UserDashboard from './pages/UserDashboard';
import AdminDashboard from './pages/AdminDashboard';


function App() {
  localStorage.setItem("token", "");
  localStorage.setItem("userId", "");
  localStorage.setItem("roles", "");
  localStorage.setItem("username", "");
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/user/dash" element={<UserDashboard />} />
        <Route path="/admin/dash" element={<AdminDashboard />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App