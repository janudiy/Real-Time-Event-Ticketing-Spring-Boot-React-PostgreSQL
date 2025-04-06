// src/context/WebSocketContext.js
import React, { createContext, useState, useEffect } from "react";

export const WebSocketContext = createContext();

export const WebSocketProvider = ({ children }) => {
  const [logs, setLogs] = useState([]);
  const [connectionStatus, setConnectionStatus] = useState("Connecting...");

  useEffect(() => {
    const socket = new WebSocket("ws://localhost:8080/logs");

    socket.onopen = () => {
      const connectionInfo = {
        status: "Connected",
        timestamp: new Date().toISOString(),
        message: "WebSocket connection established successfully.",
      };
      setConnectionStatus(JSON.stringify(connectionInfo));
    };

    socket.onmessage = (event) => {
      const logMessage = JSON.parse(event.data); // Parse JSON messages
      setLogs((prevLogs) => [...prevLogs, logMessage]); // Add new log to state
    };

    socket.onerror = (error) => {
      console.error("WebSocket error:", error);
      setConnectionStatus("Error");
    };

    socket.onclose = () => {
      setConnectionStatus("Disconnected");
    };

    // Cleanup on unmount
    return () => {
      socket.close();
    };
  }, []);

  return (
    <WebSocketContext.Provider value={{ logs, connectionStatus }}>
      {children}
    </WebSocketContext.Provider>
  );
};
