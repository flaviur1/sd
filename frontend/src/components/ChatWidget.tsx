import { useState, useEffect, useRef } from 'react';
import '@chatscope/chat-ui-kit-styles/dist/default/styles.min.css';
import '../styles/ChatWidget.css';
import {
    MainContainer,
    ChatContainer,
    MessageList,
    Message,
    MessageInput,
    TypingIndicator,
} from '@chatscope/chat-ui-kit-react';
import { Client } from '@stomp/stompjs';
import axios from '../axios.ts';

interface ChatMessage {
    message: string;
    sentTime: string;
    sender: string;
    direction: 'incoming' | 'outgoing';
    position: 'single' | 'first' | 'normal' | 'last' | 0 | 1 | 2 | 3;
}

function ChatWidget() {
    const [messages, setMessages] = useState<ChatMessage[]>([
        {
            message: "Hello! I'm here to help you with your energy monitoring system. How can I assist you today?",
            sentTime: new Date().toISOString(),
            sender: "Bot",
            direction: "incoming",
            position: "single"
        }
    ]);
    const [isTyping, setIsTyping] = useState(false);
    const [isOpen, setIsOpen] = useState(false);
    const clientRef = useRef<Client | null>(null);
    const [isConnected, setIsConnected] = useState(false);

    useEffect(() => {
        loadChatHistory();

        const client = new Client({
            brokerURL: 'ws://localhost/api/ws/monitoring',
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
        });

        client.onConnect = () => {
            console.log('Chat WebSocket connected');
            setIsConnected(true);

            client.subscribe('/topic/chat-responses', (message) => {
                const response = JSON.parse(message.body);
                console.log('Parsed response:', response);
                
                const myUserId = getUserIdFromToken();
                
                if (response.userId !== myUserId) {
                    return;
                }

                const botMessage: ChatMessage = {
                    message: response.message,
                    sentTime: response.timestamp,
                    sender: response.isAdmin ? "Admin" : "Bot",
                    direction: "incoming",
                    position: "single"
                };
                setMessages(prev => [...prev, botMessage]);
                setIsTyping(false);
            });
        };

        client.onStompError = (frame) => {
            console.error('Chat WebSocket error:', frame);
            setIsConnected(false);
        };

        client.activate();
        clientRef.current = client;

        return () => {
            client.deactivate();
        };
    }, []);

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

    const loadChatHistory = async () => {
        const userId = getUserIdFromToken();
        if (!userId) return;

        try {
            const sessionsResponse = await axios.get('/chat/sessions');
            const userSession = sessionsResponse.data.find((s: any) => s.userId === userId);
            
            if (userSession) {
                const historyResponse = await axios.get(`/chat/messages/${userSession.sessionId}`);
                
                const historyMessages: ChatMessage[] = historyResponse.data.map((msg: any) => ({
                    message: msg.message,
                    sentTime: msg.timestamp,
                    sender: msg.sender === 'USER' ? 'You' : (msg.sender === 'ADMIN' ? 'Admin' : 'Bot'),
                    direction: msg.sender === 'USER' ? 'outgoing' : 'incoming',
                    position: 'single'
                }));

                if (historyMessages.length > 0) {
                    setMessages(historyMessages);
                }
            }
        } catch (error) {
            console.log('No chat history or error loading:', error);
        }
    };

    const handleSend = async (message: string) => {
        const userId = getUserIdFromToken();
        
        if (!userId) {
            console.error("No userId found in token");
            return;
        }

        if (!isConnected || !clientRef.current) {
            console.error("WebSocket not connected");
            return;
        }

        const newMessage: ChatMessage = {
            message,
            sentTime: new Date().toISOString(),
            sender: "You",
            direction: "outgoing",
            position: "single"
        };

        setMessages(prev => [...prev, newMessage]);

        if (message.trim().toUpperCase() !== "CONNECT") {
            setIsTyping(true);
        }

        const payload = {
            message,
            userId
        };        
        clientRef.current.publish({
            destination: '/app/chat/send',
            body: JSON.stringify(payload)
        });
    
    };

    return (
        <div className="chat-widget-container">
            <button
                className="chat-toggle-button"
                onClick={() => setIsOpen(!isOpen)}
            >
                {isOpen ? 'close chat' : 'open chat'}
            </button>

            {isOpen && (
                <div className="chat-window">
                    <MainContainer>
                        <ChatContainer>
                            <MessageList
                                scrollBehavior="smooth"
                                typingIndicator={isTyping ? <TypingIndicator content="Bot is typing" /> : null}
                            >
                                {messages.map((msg, index) => (
                                    <Message
                                        key={index}
                                        model={{
                                            message: msg.message,
                                            sentTime: msg.sentTime,
                                            sender: msg.sender,
                                            direction: msg.direction,
                                            position: msg.position,
                                        }}
                                    />
                                ))}
                            </MessageList>
                            <MessageInput placeholder="Type your message..." onSend={handleSend} />
                        </ChatContainer>
                    </MainContainer>
                </div>
            )}
        </div>
    );
}

export default ChatWidget;
