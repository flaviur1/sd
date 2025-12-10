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
    userId: string
}

function DeviceOperations() {
    const [deviceList, setDeviceList] = useState<Device[]>([]);

    const [manufacturerAdd, setManufacturerAdd] = useState("");
    const [modelAdd, setModelAdd] = useState("");
    const [maxConsValAdd, setMaxConsValAdd] = useState("");

    const [idDelete, setIdDelete] = useState("");

    const [idUpdate, setIdUpdate] = useState("");
    const [manufacturerUpdate, setManufacturerUpdate] = useState("");
    const [modelUpdate, setModelUpdate] = useState("");
    const [maxConsValUpdate, setMaxConsValUpdate] = useState("");

    const [idGet, setIdGet] = useState("");
    const [receivedDevice, setReceivedDevice] = useState<Device>();


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
            await axios.post("/devices", {
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
            console.error("Device add failed: ", error);
        }
    }

    const handleDeviceDelete = async () => {
        try {
            await axios.delete("/devices/" + idDelete);
            getDeviceList();
            setIdDelete("");
        }
        catch (error) {
            console.error("Delete device failed:", error);
        }
    }

    const handleDeviceUpdate = async () => {
        try {
            await axios.put("/devices/" + idUpdate, {
                manufacturer: manufacturerUpdate,
                model: modelUpdate,
                maxConsVal: maxConsValUpdate
            });

            getDeviceList();
            setIdUpdate("");
            setManufacturerUpdate("");
            setModelUpdate("");
            setMaxConsValUpdate("");
        }
        catch (error) {
            console.error("Update device failed: ", error);
        }
    }

    const handleGetDeviceById = async () => {
        try {
            const response = await axios.get("/devices/" + idGet);
            console.log(response.data);
            setReceivedDevice(response.data);
            alert(
                "ID: " + response.data.id + "\n" +
                "Manufacturer: " + response.data.manufacturer + "\n" +
                "Model: " + response.data.model + "\n" +
                "Max Consumption: " + response.data.maxConsVal
            );
            setIdGet("");
        }
        catch (error) {
            console.error("Get Device by Id failed: ", error);
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
            <div className="device-delete">
                <TextField className="input" label="id" variant="outlined" margin="normal" value={idDelete} sx={whiteInputStyle} onChange={(val) => setIdDelete(val.target.value)} />
                <button className="button" onClick={handleDeviceDelete}>Delete Device</button>
            </div>
            <div className="device-update">
                <TextField className="input" label="id" variant="outlined" margin="normal" value={idUpdate} sx={whiteInputStyle} onChange={(val) => setIdUpdate(val.target.value)} />
                <TextField className="input" label="manufacturer" variant="outlined" margin="normal" value={manufacturerUpdate} sx={whiteInputStyle} onChange={(val) => setManufacturerUpdate(val.target.value)} />
                <TextField className="input" label="model" variant="outlined" margin="normal" value={modelUpdate} sx={whiteInputStyle} onChange={(val) => setModelUpdate(val.target.value)} />
                <TextField className="input" label="max consumption" variant="outlined" margin="normal" value={maxConsValUpdate} sx={whiteInputStyle} onChange={(val) => setMaxConsValUpdate(val.target.value)} />
                <button className="button" onClick={handleDeviceUpdate}>Update Device</button>
            </div>

            <div className="device-get">
                <TextField className="input" label="id" variant="outlined" margin="normal" value={idGet} sx={whiteInputStyle} onChange={(val) => setIdGet(val.target.value)} />
                <button className="button" onClick={handleGetDeviceById}>Get Device</button>
            </div>
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
