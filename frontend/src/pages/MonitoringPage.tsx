import { useParams } from "react-router-dom";
import { Container, Typography, Box } from "@mui/material";
import DeviceMonitoring from "../components/DeviceMonitoring";
import "../styles/MonitoringPage.css";

const MonitoringPage = () => {
    const { deviceId } = useParams<{ deviceId: string }>();

    if (!deviceId) {
        return (
            <Container maxWidth="lg" className="monitoring-page-container">
                <Box className="monitoring-error-container">
                    <Typography variant="h5" className="monitoring-error-text">
                        No device ID provided
                    </Typography>
                </Box>
            </Container>
        );
    }

    return (
        <Container maxWidth="lg" className="monitoring-page-container">
            <DeviceMonitoring deviceId={deviceId} />
        </Container>
    );
};

export default MonitoringPage;
