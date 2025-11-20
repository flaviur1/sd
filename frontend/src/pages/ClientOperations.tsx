import "../styles/ClientOperations.css";
import { useState, useEffect } from "react";
//import { useNavigate } from "react-router-dom";
import axios from "../axios.ts";
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
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

    useEffect(() => {
        const getClientList = async () => {
            try {
                const response = await axios.get("/users/");
                setClientList(response.data);
            } catch (error) {
                console.error("Getting clients failed:", error);
            }
        };

        getClientList();
    }, []);

    const handleClientAdd = async () => {
        try {
            await axios.post("/auth/registerByAdmin", {
                username: usernameAdd,
                password: passwordAdd,
                address: addressAdd,
                age: parseInt(ageAdd, 10),
                roles: rolesAdd
            });
        }
        catch (error) {
            console.error("Registration failed:", error);
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

            </div>

            <div className="client-table">
                <Table sx={{ minWidth: 650 }} aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <TableCell align="right">ID</TableCell>
                            <TableCell align="right">Name</TableCell>
                            <TableCell align="right">Address</TableCell>
                            <TableCell align="right">Age</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {clientList.map((row) => (
                            <TableRow
                                key={row.id}
                                sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                            >
                                <TableCell component="th" scope="row">
                                    {row.id}
                                </TableCell>
                                <TableCell align="right">{row.id}</TableCell>
                                <TableCell align="right">{row.name}</TableCell>
                                <TableCell align="right">{row.address}</TableCell>
                                <TableCell align="right">{row.age}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </div>
        </div>
    );
}

export default ClientOperations;
