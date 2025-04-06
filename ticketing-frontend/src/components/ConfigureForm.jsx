import React, { useState } from "react";

function ConfigureForm({ systemConfig, updateConfig }) {
  const [formData, setFormData] = useState(systemConfig);
  const [successMessage, setSuccessMessage] = useState("");
  const [errors, setErrors] = useState({}); // State for validation errors

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: Number(value),
    });
  };

  const validateFields = () => {
    const newErrors = {};
    if (!formData.totalTickets || formData.totalTickets <= 0) {
      newErrors.totalTickets = "Total tickets must be greater than 0.";
    }
    if (!formData.releaseRate || formData.releaseRate <= 0) {
      newErrors.releaseRate = "Release rate must be greater than 0.";
    }
    if (!formData.retrievalRate || formData.retrievalRate <= 0) {
      newErrors.retrievalRate = "Retrieval rate must be greater than 0.";
    }
    return newErrors;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const fieldErrors = validateFields();
    if (Object.keys(fieldErrors).length > 0) {
      setErrors(fieldErrors);
      return; // Stop form submission if there are validation errors
    }

    setErrors({});
    updateConfig(formData);
    setSuccessMessage("Configuration saved successfully!");
    setTimeout(() => setSuccessMessage(""), 3000);
  };

  return (
    <>
      <form className="configure-form" onSubmit={handleSubmit}>
        <h3>System Configuration</h3>

        {/* Total Tickets */}
        <label>
          Total System Tickets:
          <input
            type="number"
            name="totalTickets"
            value={formData.totalTickets}
            onChange={handleChange}
          />
        </label>
        {errors.totalTickets && (
          <div style={{ color: "red", fontSize: "12px" }}>
            {errors.totalTickets}
          </div>
        )}

        {/* Release Rate */}
        <label>
          Ticket Release Rate:
          <input
            type="number"
            name="releaseRate"
            value={formData.releaseRate}
            onChange={handleChange}
          />
        </label>
        {errors.releaseRate && (
          <div style={{ color: "red", fontSize: "12px" }}>
            {errors.releaseRate}
          </div>
        )}

        {/* Retrieval Rate */}
        <label>
          Customer Retrieval Rate:
          <input
            type="number"
            name="retrievalRate"
            value={formData.retrievalRate}
            onChange={handleChange}
          />
        </label>
        {errors.retrievalRate && (
          <div style={{ color: "red", fontSize: "12px" }}>
            {errors.retrievalRate}
          </div>
        )}

        <button type="submit">Save Configuration</button>
      </form>

      {/* Success Message */}
      {successMessage && (
        <div
          className="success-message"
          style={{ color: "green", marginTop: "10px" }}
        >
          {successMessage}
        </div>
      )}
    </>
  );
}

export default ConfigureForm;
