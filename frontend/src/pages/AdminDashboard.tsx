import "../styles/AdminDashboard.css";
import { useState } from "react";
import ClientOperations from "./ClientOperations.tsx";
import DeviceOperations from "./DeviceOperations.tsx";
import AdminChatPanel from "../components/AdminChatPanel.tsx";
import { useNavigate } from "react-router-dom";

function AdminDashboard() {
    const [selectedView, setSelectedView] = useState<'devices' | 'clients' | 'chat'>('devices');
    const navigate = useNavigate();

    const handleLogout = () => {
        navigate("/");
        localStorage.removeItem("token");
    }

    return (
        <div className="adminDashboard">
            <div className="switch">
                <span 
                    className={selectedView === 'devices' ? 'active' : ''} 
                    onClick={() => setSelectedView('devices')}
                    style={{ cursor: 'pointer' }}
                >
                    Device Operations
                </span>
                <span style={{ margin: '0 1rem' }}>|</span>
                <span 
                    className={selectedView === 'clients' ? 'active' : ''} 
                    onClick={() => setSelectedView('clients')}
                    style={{ cursor: 'pointer' }}
                >
                    Client Operations
                </span>
                <span style={{ margin: '0 1rem' }}>|</span>
                <span 
                    className={selectedView === 'chat' ? 'active' : ''} 
                    onClick={() => setSelectedView('chat')}
                    style={{ cursor: 'pointer' }}
                >
                    Chat Support
                </span>
            </div>
            
            <button className="logout-button" onClick={handleLogout}>Logout</button>

            <div className="operations-div">
                {selectedView === 'clients' && <ClientOperations />}
                {selectedView === 'devices' && <DeviceOperations />}
                {selectedView === 'chat' && <AdminChatPanel />}
            </div>


        </div >
    );
}

export default AdminDashboard;

