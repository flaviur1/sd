import { Navigate, Outlet } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

interface ProtectedRouteProps {
    requiredRole?: string;
}

interface CustomJwtPayload {
    sub: string,
    roles: string,
    userId: string,
    exp?: number
}

function ProtectedRoute({ requiredRole }: ProtectedRouteProps) {
    const token = localStorage.getItem("token");

    if (!token) {
        return <Navigate to="/" replace />;
    }

    let decoded: CustomJwtPayload;

    try {
        decoded = jwtDecode<CustomJwtPayload>(token);
    } catch {
        return <Navigate to="/" replace />;
    }

    if (decoded.exp && decoded.exp * 1000 < Date.now()) {
        localStorage.removeItem("token");
        return <Navigate to="/" replace />;
    }

    const roles = decoded.roles;
    if (requiredRole && !roles.includes(requiredRole)) {
        return <Navigate to="/" replace />;
    }

    return <Outlet />;
}

export default ProtectedRoute;