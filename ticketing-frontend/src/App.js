import React, { useState, useContext } from "react";
import ConfigureForm from "./components/ConfigureForm";
import ControlPanel from "./components/ControlPanel";
import TicketDisplay from "./components/TicketDisplay";
import TransactionLog from "./components/TransactionLog";
import RealTimeTransactions from "./components/RealTimeTransactions";
import { WebSocketProvider } from "./context/WebSocketContext";
import axios from "axios";

function App() {
  const [systemStatus, setSystemStatus] = useState("Stopped");
  const baseURL = "http://localhost:8080/api/start-service";
  const [maximumTickets, setMaximumTickets] = useState(0);
  const [configData, setConfigData] = useState({});

  const handleSubmit = async (data) => {
    const { totalTickets, releaseRate, retrievalRate } = data;
    setMaximumTickets(totalTickets);
    const dataObject = {
      batchSize: releaseRate,
      ticketCapacity: totalTickets,
      consumeBatchSize: retrievalRate,
    };
    setConfigData(dataObject);
  };
  const handleStart = async () => {
    try {
      const response = await axios.post(baseURL, configData, {
        headers: {
          "Content-Type": "application/json",
        },
      });
      console.log(response.data);
    } catch (error) {
      console.error("Error triggering vendors:", error);
    }
  };
  const handleStop = () => setSystemStatus("Stopped");
  return (
    <div className="app-container">
      <header>
        <h1>🎟️ Event Ticketing System</h1>
        {/* Start, Stop, and Restart Buttons */}
        <div className="controls">
          <button
            onClick={handleStart}
            disabled={systemStatus === "Running"}
            className="start"
          >
            Start
          </button>
          <button
            onClick={handleStop}
            disabled={systemStatus === "Stopped"}
            className="stop"
          >
            Stop
          </button>
        </div>
      </header>
      <main>
        <WebSocketProvider>
          <ControlPanel
            activeVendors={2}
            activeCustomers={2}
            vipCustomers={1}
          />
          <TicketDisplay totalTickets={maximumTickets} />
          <ConfigureForm
            systemConfig={{
              totalTickets: 1000,
              releaseRate: 5,
              retrievalRate: 3,
              maxCapacity: 800,
            }}
            updateConfig={(data) => {
              handleSubmit(data);
            }}
          />
          <TransactionLog />
        </WebSocketProvider>
      </main>
    </div>
  );
}

export default App;
