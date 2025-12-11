import "../styles/LoginPage.css";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "../axios.ts";
import TextField from "@mui/material/TextField";
import { jwtDecode } from "jwt-decode";

function LoginPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    interface CustomJwtPayload {
        sub: string;
        roles: string;
        userId: string;
    }

    const handleLogin = async () => {
        try {
            const response = await axios.post("/auth/login", {
                username: username,
                password: password
            });
            const token = response.data;
            localStorage.setItem("token", token);
            const decoded = jwtDecode<CustomJwtPayload>(token);
            const roles = decoded.roles;
            console.log("Login successful");

            if (roles.includes("ROLE_ADMIN")) {
                navigate("/admin/dash");
            } else {
                navigate("/user/dash");
            }

        } catch (error) {
            console.error("Login failed:", error);
            alert("Login failed! Please check your credentials.");
        }
    }

    const handleRegister = () => {
        navigate("/register");
    }

    return (
        <div className="main-div">
            <div className="square">
                <div>
                    <h1>Login</h1>
                </div>

                <div className="form">
                    <div>
                        <TextField label="username" variant="outlined" margin="normal" color="secondary" value={username} focused onChange={(val) => setUsername(val.target.value)} />
                    </div>
                    <div>
                        <TextField label="password" variant="outlined" margin="normal" color="secondary" value={password} type="password" focused onChange={(val) => setPassword(val.target.value)} />
                    </div>
                    <div>
                        <button className="button" onClick={handleLogin}>Login</button>
                        <button className="button" onClick={handleRegister}>Register</button>
                    </div>
                </div>
            </div>
        </div >
    );
}

export default LoginPage;
