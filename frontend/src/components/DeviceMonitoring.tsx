import { useEffect, useState, useRef } from "react";
import { LineChart } from "@mui/x-charts/LineChart";
import { Box, Typography, Paper } from "@mui/material";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import dayjs, { Dayjs } from "dayjs";
import { Client, StompConfig } from "@stomp/stompjs";
import axios from "../axios.ts";
import "../styles/DeviceMonitoring.css";

interface DeviceMonitoringProps {
    deviceId: string;
}

interface HourlyData {
    hour: number;
    average: number;
}

interface EnergyReading {
    deviceId: string;
    timestamp: string;
    consumptionValue: number;
    hour: number;
}

const DeviceMonitoring = ({ deviceId }: DeviceMonitoringProps) => {
    const [hourlyData, setHourlyData] = useState<HourlyData[]>([]);
    const [selectedDate, setSelectedDate] = useState<Dayjs>(dayjs());
    const stompClientRef = useRef<Client | null>(null);
    const readingsRef = useRef<Record<number, number[]>>({});
    const selectedDateRef = useRef<Dayjs>(selectedDate);

    useEffect(() => {
        const fetchInitialData = async () => {
            try {
                const dateString = selectedDate.format('YYYY-MM-DD');
                console.log("Fetching data for date:", dateString);

                const response = await axios.get(`/monitor/${deviceId}/${dateString}`);

                const initialData: HourlyData[] = [];
                for (let hour = 0; hour < 24; hour++) {
                    initialData.push({
                        hour: hour,
                        average: 0
                    });
                }

                response.data.forEach((item: { hour: number, averageConsumption: number }) => {
                    initialData[item.hour] = {
                        hour: item.hour,
                        average: item.averageConsumption || 0
                    };

                    readingsRef.current[item.hour] = [item.averageConsumption];
                });

                setHourlyData(initialData);
            } catch (error) {
                console.error("Error fetching initial data:", error);
            }
        };

        if (deviceId) {
            fetchInitialData();
        }
    }, [deviceId, selectedDate]);

    useEffect(() => {
        selectedDateRef.current = selectedDate;
    }, [selectedDate]);

    useEffect(() => {
        const stompConfig: StompConfig = {
            brokerURL: "ws://localhost/api/ws/monitoring",
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
        };

        const client = new Client(stompConfig);
        client.debug = () => { };

        client.onConnect = () => {
            console.log("WebSocket connected");

            client.subscribe("/topic/energy-readings", (message) => {
                const reading: EnergyReading = JSON.parse(message.body);

                if (reading.deviceId === deviceId) {
                    handleNewReading(reading);
                }
            });
        };

        client.onStompError = (error) => {
            console.error("STOMP error:", error);
        };

        client.activate();
        stompClientRef.current = client;

        return () => {
            if (stompClientRef.current) {
                stompClientRef.current.deactivate();
                console.log("WebSocket disconnected");
            }
        };
    }, [deviceId]);

    const handleNewReading = (reading: EnergyReading) => {
        const today = dayjs();
        if (!selectedDateRef.current.isSame(today, 'day')) {
            return;
        }
        const hour = reading.hour;
        const consumption = reading.consumptionValue;

        if (!readingsRef.current[hour]) {
            readingsRef.current[hour] = [];
        }
        readingsRef.current[hour].push(consumption);

        let sum = 0;
        for (let i = 0; i < readingsRef.current[hour].length; i++) {
            sum += readingsRef.current[hour][i];
        }
        const average = sum / readingsRef.current[hour].length;

        setHourlyData(prevData => {
            const newData = [...prevData];
            newData[hour] = {
                hour,
                average: parseFloat(average.toFixed(2))
            };
            return newData;
        });
    };

    return (
        <LocalizationProvider dateAdapter={AdapterDayjs}>
            <Paper elevation={3} className="device-monitoring-paper">
                <Box className="device-monitoring-header">
                    <Typography variant="h5" component="h2">
                        Real-Time Energy Consumption
                    </Typography>
                    <DatePicker
                        label="Select Date"
                        value={selectedDate}
                        onChange={(newValue: Dayjs | null) => {
                            if (newValue) {
                                setSelectedDate(newValue);
                            }
                        }}
                        slotProps={{
                            textField: {
                                size: "small",
                                className: "device-monitoring-date-picker"
                            }
                        }}
                    />
                </Box>

                <LineChart
                    xAxis={[
                        {
                            data: hourlyData.map(d => d.hour),
                            label: "Hour of Day",
                            scaleType: "linear",
                            min: 0,
                            max: 23,
                        }
                    ]}
                    series={[
                        {
                            data: hourlyData.map(d => d.average > 0 ? d.average : null),
                            label: "Average Consumption",
                            color: "#1976d2",
                            showMark: true,
                            curve: "linear"
                        }
                    ]}
                    height={400}
                    margin={{ top: 20, right: 20, bottom: 50, left: 70 }}
                    grid={{ vertical: true, horizontal: true }}
                />
            </Paper>
        </LocalizationProvider>
    );
};

export default DeviceMonitoring;
