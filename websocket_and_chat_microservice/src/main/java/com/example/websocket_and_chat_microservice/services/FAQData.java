package com.example.websocket_and_chat_microservice.services;

public class FAQData {
    public static final String[][] FAQ_LIST = {
            {"1,hello,hi", "Hello! How can I help you today?"},
            {"2,help", "I can assist you with device management and monitoring."},
            {"3,device", "You can view your devices on the dashboard."},
            {"4,energy", "Check the monitoring page for energy data."},
            {"5,alert", "You'll receive alerts when consumption exceeds limits."},
            {"6,settings", "Update your settings in the profile menu."},
            {"7,thanks,thank you", "You're welcome!"},
            {"8,bye,goodbye", "Goodbye! Have a great day!"},
            {"9,admin", "Let me connect you with an admin."},
            {"10,status", "All systems are running normally."}
    };

    public static final String FALLBACK_RESPONSE =
            "I didn't understand that. Try typing a number 1-10 or words like 'hello', 'help', 'device', etc.";
}
