import React, { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";

function ProgressTracker({role}) {
 
 const steps = ["Batching", "Approver 1", "Approver 2", "Released to Bank"];
const roleToIndex = {
    batching: 0,
    approver1: 1,
    approver2: 2,
    release: 3,
  };

 const currStep = roleToIndex[role] ?? 0;

  return (
    <div className="d-flex justify-content-center my-4">
      {steps.map((step, index) => {
        const isCompleted = index < currStep;
        const isActive = index === currStep;

        return (
          <div key={index} className="d-flex align-items-center">

            <div
              className="rounded-circle d-flex align-items-center justify-content-center"
              style={{
                width: 45,
                height: 45,
                background: isCompleted
                  ? "linear-gradient(135deg, #28a745, #218838)"
                  : isActive
                  ? "linear-gradient(135deg, #0d6efd, #0a58ca)"
                  : "#dee2e6",
                color: isCompleted || isActive ? "white" : "#6c757d",
                fontWeight: "bold",
                transition: "0.3s ease",
                boxShadow: isActive
                  ? "0 0 10px rgba(13, 110, 253, 0.6)"
                  : "none",
              }}
            >
              {index + 1}
            </div>

        
            <div
              className="ms-2 fw-semibold"
              style={{
                color: isCompleted || isActive ? "#000" : "#6c757d",
              }}
            >
              {step}
            </div>

         
            {index < steps.length - 1 && (
              <div
                style={{
                  height: 3,
                  width: 60,
                  backgroundColor: isCompleted ? "#28a745" : "#dee2e6",
                  margin: "0 12px",
                  transition: "0.3s ease",
                }}
              />
            )}
          </div>
        );
      })}
    </div>
  );
}

export default ProgressTracker;
