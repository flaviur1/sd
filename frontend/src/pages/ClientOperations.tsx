import "../styles/ClientOperations.css";
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

interface Client {
    id: string;
    name: string;
    address: string;
    age: number;
}

function ClientOperations() {
    const [clientList, setClientList] = useState<Client[]>([]);
    const [usernameAdd, setUsernameAdd] = useState("");
    const [passwordAdd, setPasswordAdd] = useState("");
    const [addressAdd, setAddressAdd] = useState("");
    const [ageAdd, setAgeAdd] = useState("");
    const [rolesAdd, setRolesAdd] = useState("");

    const [idPut, setIdPut] = useState("");
    const [usernamePut, setUsernamePut] = useState("");
    const [addressPut, setAddressPut] = useState("");
    const [agePut, setAgePut] = useState("");

    const getClientList = async () => {
        try {
            const response = await axios.get("/users");
            setClientList(response.data);
        } catch (error) {
            console.error("Getting clients failed:", error);
        }
    };

    useEffect(() => {
        getClientList();
    }, []);

    useEffect(() => {
        console.log("Updated Client List:", clientList);
    }, [clientList]);

    const handleClientAdd = async () => {
        try {
            await axios.post("/auth/registerByAdmin", {
                username: usernameAdd,
                password: passwordAdd,
                address: addressAdd,
                age: parseInt(ageAdd, 10),
                roles: rolesAdd
            });

            getClientList();
            setUsernameAdd("");
            setPasswordAdd("");
            setAddressAdd("");
            setAgeAdd("");
            setRolesAdd("");
        }
        catch (error) {
            console.error("Registration failed:", error);
        }
    }

    const handleClientUpdate = async () => {
        try {
            await axios.put("/users/" + idPut, {
                name: usernamePut,
                address: addressPut,
                age: agePut
            });

            setIdPut("");
            setUsernamePut("");
            setAddressPut("");
            setAgePut("");
        }
        catch (error) {
            console.error("Update failed:", error);
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
            <div className="client-add">
                <TextField className="input" label="username" variant="outlined" margin="normal" value={usernameAdd} sx={whiteInputStyle} onChange={(val) => setUsernameAdd(val.target.value)} />
                <TextField className="input" label="password" variant="outlined" margin="normal" value={passwordAdd} type="password" sx={whiteInputStyle} onChange={(val) => setPasswordAdd(val.target.value)} />
                <TextField className="input" label="address" variant="outlined" margin="normal" value={addressAdd} sx={whiteInputStyle} onChange={(val) => setAddressAdd(val.target.value)} />
                <TextField className="input" label="age" variant="outlined" margin="normal" value={ageAdd} sx={whiteInputStyle} onChange={(val) => setAgeAdd(val.target.value)} />
                <TextField className="input" label="roles" variant="outlined" margin="normal" value={rolesAdd} sx={whiteInputStyle} onChange={(val) => setRolesAdd(val.target.value)} />
                <button className="button" onClick={handleClientAdd}>Add User</button>
            </div>

            <div className="client-delete">

            </div>

            <div className="client-update">
                <TextField className="input" label="id" variant="outlined" margin="normal" value={idPut} sx={whiteInputStyle} onChange={(val) => setIdPut(val.target.value)} />
                <TextField className="input" label="username" variant="outlined" margin="normal" value={usernamePut} sx={whiteInputStyle} onChange={(val) => setUsernamePut(val.target.value)} />
                <TextField className="input" label="address" variant="outlined" margin="normal" value={addressPut} sx={whiteInputStyle} onChange={(val) => setAddressPut(val.target.value)} />
                <TextField className="input" label="age" variant="outlined" margin="normal" value={agePut} sx={whiteInputStyle} onChange={(val) => setAgePut(val.target.value)} />
                <button className="button" onClick={handleClientUpdate}>Update User</button>
            </div>

            <div className="client-table">
                <TableContainer component={Paper} sx={{ backgroundColor: 'transparent', boxShadow: 'none' }}>
                    <Table sx={{ minWidth: 650 }} aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell align="left" sx={{ color: 'white' }}>ID</TableCell>
                                <TableCell align="left" sx={{ color: 'white' }}>Name</TableCell>
                                <TableCell align="left" sx={{ color: 'white' }}>Age</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {clientList.map((row) => (
                                <TableRow
                                    key={row.id}
                                    sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                                >
                                    <TableCell align="left" sx={{ color: 'white' }}>{row.id}</TableCell>
                                    <TableCell align="left" sx={{ color: 'white' }}>{row.name}</TableCell>
                                    <TableCell align="left" sx={{ color: 'white' }}>{row.age}</TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </div>
        </div>
    );
}

export default ClientOperations;
