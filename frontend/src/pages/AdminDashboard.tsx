import "../styles/AdminDashboard.css";
import { useState } from "react";
import Switch from '@mui/material/Switch';
import ClientOperations from "./ClientOperations.tsx";
import DeviceOperations from "./DeviceOperations.tsx";

function AdminDashboard() {
    const [isClientOperations, setIsClientOperations] = useState(false);

    const handleToggle = () => {
        if (isClientOperations)
            setIsClientOperations(false);
        else
            setIsClientOperations(true);
    }

    return (
        <div className="adminDashboard">
            <div className="switch">
                Device Operations
                <Switch color="secondary" onChange={handleToggle} checked={isClientOperations} />
                Client Operations
            </div>

            <div className="operations-div">
                {isClientOperations ? (<ClientOperations />) : (<DeviceOperations />)}
            </div>


        </div >
    );
}

export default AdminDashboard;
