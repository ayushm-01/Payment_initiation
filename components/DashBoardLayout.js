import React from "react";

export default function DashboardLayout({ children }) {
  return (
    <div className="d-flex">

      <div className="flex-grow-1">
   
        <div className="p-4">
          {children}
        </div>
      </div>
    </div>
  );
}
