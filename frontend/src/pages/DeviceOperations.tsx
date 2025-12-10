import "../styles/DeviceOperations.css";
import { useState, useEffect } from "react";
//import { useNavigate } from "react-router-dom";
import axios from "../axios.ts";
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableContainer from '@mui/material/TableContainer';
import Paper from '@mui/material/Paper';
import TableCell from '@mui/material/TableCell';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import TextField from "@mui/material/TextField"

interface Device {
    id: string,
    manufacturer: string;
    model: string;
    maxConsVal: number;
}

function DeviceOperations() {
    const [deviceList, setDeviceList] = useState<Device[]>([]);

    const [manufacturerAdd, setManufacturerAdd] = useState("");
    const [modelAdd, setModelAdd] = useState("");
    const [maxConsValAdd, setMaxConsValAdd] = useState("");


    const getDeviceList = async () => {
        try {
            const response = await axios.get("/devices");
            setDeviceList(response.data);
        } catch (error) {
            console.error("Getting devices failed:", error);
        }
    };

    useEffect(() => {
        getDeviceList();
    }, []);

    const handleDeviceAdd = async () => {
        try {
            await axios.post("/devices/", {
                manufacturer: manufacturerAdd,
                model: modelAdd,
                maxConsVal: parseInt(maxConsValAdd, 10)
            });

            getDeviceList();
            setManufacturerAdd("");
            setModelAdd("");
            setMaxConsValAdd("");
        }
        catch (error) {
            console.log("Device add failed: ", error);
        }
    }


    const whiteInputStyle = {
        "& .MuiInputBase-input": {
            color: "white",
        },
        "& .MuiInputLabel-root": {
            color: "white",
        },
        "& .MuiInputLabel-root.Mui-focused": {
            color: "white",
        },
        "& .MuiOutlinedInput-root": {
            "& fieldset": {
                borderColor: "white",
            },
            "&:hover fieldset": {
                borderColor: "white",
            },
            "&.Mui-focused fieldset": {
                borderColor: "white",
            }
        }
    };

    return (
        <div>
            <div className="device-add">
                <TextField className="input" label="manufacturer" variant="outlined" margin="normal" value={manufacturerAdd} sx={whiteInputStyle} onChange={(val) => setManufacturerAdd(val.target.value)} />
                <TextField className="input" label="model" variant="outlined" margin="normal" value={modelAdd} sx={whiteInputStyle} onChange={(val) => setModelAdd(val.target.value)} />
                <TextField className="input" label="max consumption" variant="outlined" margin="normal" value={maxConsValAdd} sx={whiteInputStyle} onChange={(val) => setMaxConsValAdd(val.target.value)} />
                <button className="button" onClick={handleDeviceAdd}>Add Device</button>
            </div>
            <div className="device-delete"></div>
            <div className="device-update"></div>
            <div className="device-add"></div>
            <div className="device-add"></div>

            <div className="device-table">
                <TableContainer component={Paper} sx={{ backgroundColor: 'transparent', boxShadow: 'none' }}>
                    <Table sx={{ minWidth: 650 }} aria-label="simple table">
                        <TableHead>
                            <TableRow>  
                                <TableCell align="left" sx={{ color: 'white' }}>ID</TableCell>
                                <TableCell align="left" sx={{ color: 'white' }}>Manufacturer</TableCell>
                                <TableCell align="left" sx={{ color: 'white' }}>Model</TableCell>
                                <TableCell align="left" sx={{ color: 'white' }}>Max Consumption</TableCell>
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
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </div>
        </div>
    );
}

export default DeviceOperations;
