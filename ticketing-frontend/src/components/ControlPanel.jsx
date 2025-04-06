import React from "react";

function ControlPanel({ activeVendors, activeCustomers, vipCustomers }) {
  return (
    <div className="control-panel">
      <div className="stat">
        <p>Active Vendors</p>
        <h2>{activeVendors}</h2>
      </div>
      <div className="stat">
        <p>Active Customers</p>
        <h2>{activeCustomers}</h2>
      </div>
      <div className="stat">
        <p>VIP Customers</p>
        <h2>{vipCustomers}</h2>
      </div>
    </div>
  );
}

export default ControlPanel;
