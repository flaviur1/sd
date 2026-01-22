import { useState, useEffect, useRef } from 'react';
import '@chatscope/chat-ui-kit-styles/dist/default/styles.min.css';
import '../styles/AdminChatPanel.css';
import {
    MainContainer,
    ChatContainer,
    MessageList,
    Message,
    MessageInput,
} from '@chatscope/chat-ui-kit-react';
import { Client } from '@stomp/stompjs';
import axios from '../axios.ts';

interface ChatMessage {
    id?: string;
    sessionId?: string;
    message: string;
    sender: string;
    timestamp: string;
}

interface ChatSessionDTO {
    sessionId: string;
    userId: string;
    createdAt: string;
    lastMessage: string;
}

function AdminChatPanel() {
    const [sessions, setSessions] = useState<ChatSessionDTO[]>([]);
    const [selectedSession, setSelectedSession] = useState<ChatSessionDTO | null>(null);
    const [messages, setMessages] = useState<ChatMessage[]>([]);
    const clientRef = useRef<Client | null>(null);
    const [isConnected, setIsConnected] = useState(false);

    useEffect(() => {
        fetchActiveSessions();
        
        const client = new Client({
            brokerURL: 'ws://localhost/api/ws/monitoring',
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
        });

        client.onConnect = () => {
            console.log('Admin WebSocket connected');
            setIsConnected(true);

            client.subscribe('/topic/admin/new-session', (message) => {
                const newSession: ChatSessionDTO = JSON.parse(message.body);
                setSessions(prev => [newSession, ...prev]);
            });
        };

        client.onStompError = (frame) => {
            console.error('Admin WebSocket error:', frame);
            setIsConnected(false);
        };

        client.activate();
        clientRef.current = client;

        return () => {
            client.deactivate();
        };
    }, []);

    useEffect(() => {
        if (selectedSession && clientRef.current && isConnected) {
            const subscription = clientRef.current.subscribe(
                `/topic/admin/messages/${selectedSession.sessionId}`,
                (message) => {
                    const newMessage: ChatMessage = JSON.parse(message.body);
                    setMessages(prev => [...prev, newMessage]);
                }
            );

            return () => {
                subscription.unsubscribe();
            };
        }
    }, [selectedSession, isConnected]);

    const fetchActiveSessions = async () => {
        try {
            const response = await axios.get('/chat/sessions');
            if (Array.isArray(response.data)) {
                setSessions(response.data);
            } else {
                console.error('Sessions response is not an array:', response.data);
                setSessions([]);
            }
        } catch (error) {
            console.error('Error fetching sessions:', error);
            setSessions([]);
        }
    };

    const fetchChatHistory = async (sessionId: string) => {
        try {
            const response = await axios.get(`/chat/messages/${sessionId}`);
            if (Array.isArray(response.data)) {
                setMessages(response.data);
            } else {
                console.error('Messages response is not an array:', response.data);
                setMessages([]);
            }
        } catch (error) {
            console.error('Error fetching chat history:', error);
            setMessages([]);
        }
    };

    const handleSessionSelect = (session: ChatSessionDTO) => {
        setSelectedSession(session);
        fetchChatHistory(session.sessionId);
    };

    const handleSendMessage = async (message: string) => {
        if (!selectedSession || !clientRef.current || !isConnected) {
            return;
        }

        const adminId = getUserIdFromToken();
        
        const payload = {
            sessionId: selectedSession.sessionId,
            message,
            adminId
        };

        clientRef.current.publish({
            destination: '/app/chat/admin/send',
            body: JSON.stringify(payload)
        });

        const adminMessage: ChatMessage = {
            message,
            sender: "ADMIN",
            timestamp: new Date().toISOString()
        };
        setMessages(prev => [...prev, adminMessage]);
    };

    const getUserIdFromToken = (): string | null => {
        const token = localStorage.getItem("token");
        if (!token) return null;

        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return payload.userId;
        } catch (e) {
            console.error("Error decoding token:", e);
            return null;
        }
    };

    return (
        <div className="admin-chat-panel">
            <div className="sessions-list">
                <h3>Active Chat Sessions</h3>
                {sessions.length === 0 ? (
                    <p className="no-sessions">No active sessions</p>
                ) : (
                    sessions.map((session) => (
                        <div
                            key={session.sessionId}
                            className={`session-item ${selectedSession?.sessionId === session.sessionId ? 'selected' : ''}`}
                            onClick={() => handleSessionSelect(session)}
                        >
                            <div className="session-user">User: {session.userId.substring(0, 8)}...</div>
                            <div className="session-preview">{session.lastMessage}</div>
                            <div className="session-time">
                                {new Date(session.createdAt).toLocaleTimeString()}
                            </div>
                        </div>
                    ))
                )}
            </div>

            <div className="chat-area">
                {selectedSession ? (
                    <MainContainer>
                        <ChatContainer>
                            <MessageList scrollBehavior="smooth">
                                {messages.map((msg, index) => (
                                    <Message
                                        key={index}
                                        model={{
                                            message: msg.message,
                                            sentTime: msg.timestamp,
                                            sender: msg.sender,
                                            direction: msg.sender === 'ADMIN' ? 'outgoing' : 'incoming',
                                            position: 'single',
                                        }}
                                    />
                                ))}
                            </MessageList>
                            <MessageInput 
                                placeholder="Type your response..." 
                                onSend={handleSendMessage} 
                            />
                        </ChatContainer>
                    </MainContainer>
                ) : (
                    <div className="no-selection">
                        <p>Select a chat session to start responding</p>
                    </div>
                )}
            </div>
        </div>
    );
}

export default AdminChatPanel;
