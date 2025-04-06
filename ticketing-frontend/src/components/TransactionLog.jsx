import React, { useContext } from "react";
import { WebSocketContext } from "../context/WebSocketContext";

function TransactionLog() {
  const { logs, connectionStatus } = useContext(WebSocketContext);
  console.log("LOGS: ", logs, connectionStatus);
  return (
    <div className="transaction-log">
      <h3>Transaction Log</h3>
      {/* <p>Status: {connectionStatus}</p> */}
      <ul>
        {logs.map((log, index) => (
          <li key={index}>
            <code>{log.message}</code>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default TransactionLog;
