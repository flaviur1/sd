import "../styles/RegisterPage.css";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "../axios.ts";
import TextField from "@mui/material/TextField";

function RegisterPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [address, setAddress] = useState("");
    const [age, setAge] = useState("");
    const navigate = useNavigate();

    const handleLogin = () => {
        navigate("/login");
    }

    const handleRegister = async () => {
        try {
            await axios.post("/auth/registerByForm", {
                username: username,
                password: password,
                address: address,
                age: parseInt(age, 10),
                roles: "ROLE_USER"
            });
            alert("Registration successful! Please log in.");
            navigate("/login");
        } catch (error) {
            console.error("Registration failed:", error);
            alert("Registration failed! Please check your inputs.");
        }
    }

    return (
        <div className="main-div">
            <div className="square">
                <div>
                    <h1>Register</h1>
                </div>

                <div className="form">
                    <div>
                        <TextField label="username" variant="outlined" margin="normal" color="secondary" value={username} focused onChange={(val) => setUsername(val.target.value)} />
                    </div>
                    <div>
                        <TextField label="password" variant="outlined" margin="normal" color="secondary" value={password} type="password" focused onChange={(val) => setPassword(val.target.value)} />
                    </div>
                    <div>
                        <TextField label="address" variant="outlined" margin="normal" color="secondary" value={address} focused onChange={(val) => setAddress(val.target.value)} />
                    </div>
                    <div>
                        <TextField label="age" variant="outlined" margin="normal" color="secondary" value={age} focused onChange={(val) => setAge(val.target.value)} />
                    </div>
                    <div>
                        <button className="button" onClick={handleLogin}>Back to Login</button>
                        <button className="button" onClick={handleRegister}>Create Account</button>
                    </div>
                </div>
            </div>
        </div >
    );
}

export default RegisterPage;
