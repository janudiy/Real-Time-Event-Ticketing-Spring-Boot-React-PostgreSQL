import React, { useEffect, useRef, useState } from "react";
import { Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";

// Register required chart components
ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

const RealTimeTransactions = () => {
  const [chartData, setChartData] = useState({
    labels: ["00:00", "00:01", "00:02", "00:03", "00:04"], // Initial time points
    datasets: [
      {
        label: "Tickets Released",
        data: [10, 20, 30, 40, 50], // Initial dataset
        borderColor: "#4a90e2",
        backgroundColor: "rgba(74, 144, 226, 0.2)",
        tension: 0.4,
      },
      {
        label: "Tickets Purchased",
        data: [5, 15, 25, 35, 45],
        borderColor: "#e24a4a",
        backgroundColor: "rgba(226, 74, 74, 0.2)",
        tension: 0.4,
      },
    ],
  });

  const [time, setTime] = useState(5); // Simulates time intervals
  const chartRef = useRef(null);

  useEffect(() => {
    // Simulate real-time updates
    const interval = setInterval(() => {
      const newTime = `00:${String(time).padStart(2, "0")}`; // Format time

      // Generate random ticket data for demonstration
      const newReleased = Math.floor(Math.random() * 20) + 40;
      const newPurchased = Math.floor(Math.random() * 20) + 30;

      // Update the chart data
      setChartData((prevData) => ({
        labels: [...prevData.labels.slice(-9), newTime], // Keep only the last 10 timestamps
        datasets: [
          {
            ...prevData.datasets[0],
            data: [...prevData.datasets[0].data.slice(-9), newReleased], // Update "Tickets Released"
          },
          {
            ...prevData.datasets[1],
            data: [...prevData.datasets[1].data.slice(-9), newPurchased], // Update "Tickets Purchased"
          },
        ],
      }));

      // Increment time for the next interval
      setTime((prev) => prev + 1);
    }, 1000);

    return () => clearInterval(interval); // Cleanup on unmount
  }, [time]);

  return (
    <div className="real-time-transactions">
      <h3>Real-Time Transactions</h3>
      <Line
        ref={chartRef}
        data={chartData}
        options={{
          responsive: true,
          plugins: {
            legend: { position: "top" },
            title: {
              display: true,
              text: "Tickets Released vs Tickets Purchased",
            },
          },
          scales: {
            x: { title: { display: true, text: "Time (HH:MM)" } },
            y: { title: { display: true, text: "Number of Tickets" } },
          },
        }}
      />
    </div>
  );
};

export default RealTimeTransactions;
