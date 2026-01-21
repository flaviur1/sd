import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import UserDashboard from './pages/UserDashboard';
import AdminDashboard from './pages/AdminDashboard';
import MonitoringPage from './pages/MonitoringPage';
import ProtectedRoute from "./components/ProtectedRoute";
import { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";
import { Snackbar, Alert } from "@mui/material";

interface OverconsumptionAlert {
  deviceId: string;
  userId: string;
  timestamp: string;
  actualConsumption: number;
  threshold: number;
}

function App() {
  const [alertOpen, setAlertOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState("");

  useEffect(() => {
    const client = new Client({
      brokerURL: "ws://localhost/api/ws/monitoring",
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    client.debug = () => { };

    client.onConnect = () => {
      console.log("WebSocket for alerts connected");

      client.subscribe("/topic/overconsumption-alerts", (message) => {
        const alert: OverconsumptionAlert = JSON.parse(message.body);
        console.log("Received overconsumption alert:", alert);
        
        const token = localStorage.getItem("token");
        let loggedInUserId = null;
        
        if (token) {
          try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            loggedInUserId = payload.userId;
          } catch (e) {
            console.error("Error decoding token:", e);
          }
        }
        
        if (alert.userId && loggedInUserId && alert.userId === loggedInUserId) {
          setAlertMessage(
            `Device ${alert.deviceId} exceeded limit! Actual: ${alert.actualConsumption} kWh, Threshold: ${alert.threshold} kWh`
          );
          setAlertOpen(true);
        }
      });
    };

    client.onStompError = (frame) => {
      console.error("STOMP error for alerts:", frame);
    };

    client.activate();

    return () => {
      client.deactivate();
    };
  }, []);

  const handleAlertClose = () => {
    setAlertOpen(false);
  };

  return (
    <>
      <Snackbar
        open={alertOpen}
        autoHideDuration={5000}
        onClose={handleAlertClose}
        anchorOrigin={{ vertical: "top", horizontal: "right" }}
      >
        <Alert onClose={handleAlertClose} severity="warning" sx={{ width: "100%" }}>
          {alertMessage}
        </Alert>
      </Snackbar>

    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        <Route element={<ProtectedRoute requiredRole="ROLE_USER" />}>
          <Route path="/user/dash" element={<UserDashboard />} />

        </Route>

        <Route element={<ProtectedRoute requiredRole="ROLE_ADMIN" />}>
          <Route path="/admin/dash" element={<AdminDashboard />} />
        </Route>

        <Route path="/monitoring/:deviceId" element={<MonitoringPage />} />
      </Routes>
    </BrowserRouter>
    </>
  )
}

export default App