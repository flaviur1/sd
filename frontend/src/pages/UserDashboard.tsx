import "../styles/UserDashboard.css"
import { useEffect, useState } from "react";
import ChatWidget from '../components/ChatWidget';
import { useNavigate } from "react-router-dom";
import axios from "../axios.ts";
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableContainer from '@mui/material/TableContainer';
import Paper from '@mui/material/Paper';
import TableCell from '@mui/material/TableCell';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Button from '@mui/material/Button';
import { jwtDecode } from "jwt-decode";

interface Device {
    id: string,
    manufacturer: string,
    model: string,
    maxConsVal: number
}

interface CustomJwtPayload {
    sub: string;
    roles: string;
    userId: string;
}

function UserDashboard() {
    const [deviceList, setDeviceList] = useState<Device[]>([]);
    const navigate = useNavigate();

    const getUserDevices = async () => {
        try {
            var userId = "";
            const token = localStorage.getItem("token");
            if (!token) return null;

            try {
                const decoded: CustomJwtPayload = jwtDecode(token);
                userId = decoded.userId;
            } catch (error) {
                console.error("Invalid token", error);
                return null;
            }
            const response = await axios.get("/devices/getFor/" + userId);
            setDeviceList(response.data);
        }
        catch (error) {
            console.log("Getting devices failed:", error);
        }
    }

    useEffect(() => {
        getUserDevices();
    }, []);

    const handleLogout = () => {
        navigate("/");
        localStorage.removeItem("token");
    }

    return (
        <div className="userDashboard">

            <button className="logout-button" onClick={handleLogout}>Logout</button>

            <div className="device-table">
                <TableContainer component={Paper} sx={{ backgroundColor: 'transparent', boxShadow: 'none' }}>
                    <Table sx={{ minWidth: 650 }} aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell align="left" sx={{ color: 'white' }}>ID</TableCell>
                                <TableCell align="left" sx={{ color: 'white' }}>Manufacturer</TableCell>
                                <TableCell align="left" sx={{ color: 'white' }}>Model</TableCell>
                                <TableCell align="left" sx={{ color: 'white' }}>Max Consumption</TableCell>
                                <TableCell align="center" sx={{ color: 'white' }}>Actions</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {deviceList.map((row) => (
                                <TableRow
                                    key={row.id}
                                    sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                                >
                                    <TableCell align="left" sx={{ color: 'white' }}>{row.id}</TableCell>
                                    <TableCell align="left" sx={{ color: 'white' }}>{row.manufacturer}</TableCell>
                                    <TableCell align="left" sx={{ color: 'white' }}>{row.model}</TableCell>
                                    <TableCell align="left" sx={{ color: 'white' }}>{row.maxConsVal}</TableCell>
                                    <TableCell align="center">
                                        <Button variant="contained" size="small" onClick={() => navigate(`/monitoring/${row.id}`)}>
                                            View Graph
                                        </Button>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </div>
            
            <ChatWidget />
        </div >
    );
}

export default UserDashboard;
