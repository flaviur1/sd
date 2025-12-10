import "../styles/AdminDashboard.css";
import { useState } from "react";
import Switch from '@mui/material/Switch';
import ClientOperations from "./ClientOperations.tsx";
import DeviceOperations from "./DeviceOperations.tsx";
import { useNavigate } from "react-router-dom";

function AdminDashboard() {
    const [isClientOperations, setIsClientOperations] = useState(false);
    const navigate = useNavigate();

    const handleToggle = () => {
        if (isClientOperations)
            setIsClientOperations(false);
        else
            setIsClientOperations(true);
    }

    const handleLogout = () => {
        navigate("/");
        localStorage.setItem("token", "");
    }

    return (
        <div className="adminDashboard">
            <div className="switch">
                Device Operations
                <Switch color="secondary" onChange={handleToggle} checked={isClientOperations} />
                Client Operations
            </div>
            
            <button className="logout-button" onClick={handleLogout}>Logout</button>

            <div className="operations-div">
                {isClientOperations ? (<ClientOperations />) : (<DeviceOperations />)}
            </div>


        </div >
    );
}

export default AdminDashboard;
