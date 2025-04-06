import React, { useContext } from "react";
import { WebSocketContext } from "../context/WebSocketContext";

function TicketDisplay({ totalTickets }) {
  const { logs, connectionStatus } = useContext(WebSocketContext);
  console.log("LOGS IN PROGRESS: ", logs);
  const filteredProductionLogs = logs.filter(
    (obj) => obj.type === "PRODUCTION"
  );
  console.log("filteredProductionLogs: ", filteredProductionLogs);
  const releasedTicketsCount = filteredProductionLogs.length
    ? filteredProductionLogs[filteredProductionLogs.length - 1]
    : 0;
  console.log(releasedTicketsCount.releasedTicketCount);
  const progress = (releasedTicketsCount.releasedTicketCount / 10) * 100;

  return (
    <div className="ticket-display">
      <h3>Available Tickets</h3>
      <p>{`${releasedTicketsCount.releasedTicketCount} / ${totalTickets}`}</p>
      <progress
        value={releasedTicketsCount.releasedTicketCount}
        max={totalTickets}
      ></progress>
      <p>
        <strong>Total Released: </strong>
        {releasedTicketsCount.releasedTicketCount}
      </p>
    </div>
  );
}

export default TicketDisplay;
