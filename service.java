import React, { useState, useEffect } from "react";
import { Table, Form, Button } from "react-bootstrap";
import axios from "axios";

export default function BatchTable() {
  const [batches, setBatches] = useState([]);
  const [selected, setSelected] = useState([]);

  // Fetch pending batches
  useEffect(() => {
    const token = localStorage.getItem("token");
    axios
      .get("http://localhost:8080/api/approver/batches", {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => setBatches(res.data))
      .catch((err) => console.error("Error fetching batches:", err));
  }, []);

  // Select / Deselect
  const toggleSelect = (id) => {
    setSelected((prev) =>
      prev.includes(id) ? prev.filter((i) => i !== id) : [...prev, id]
    );
  };

  // Approve / Reject with confirmation
  const handleDecision = async (approved) => {
    if (selected.length === 0) return;

    const action = approved ? "approve" : "reject";
    const confirm = window.confirm(
      `Are you sure you want to ${action} the selected batches: ${selected.join(", ")}?`
    );
    if (!confirm) return;

    const token = localStorage.getItem("token");
    const approverId = localStorage.getItem("approverId");

    for (const id of selected) {
      try {
        await axios.post(
          "http://localhost:8080/api/approver/decision",
          { approverId, batchId: id, approved },
          { headers: { Authorization: `Bearer ${token}` } }
        );
      } catch (err) {
        console.error("Error approving/rejecting:", err);
      }
    }

    // Refresh table after action
    setBatches((prev) => prev.filter((b) => !selected.includes(b.id)));
    setSelected([]);
  };

  return (
    <div>
      <h3>Pending Batches</h3>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Select</th>
            <th>Batch Reference</th>
            <th>Name</th>
            <th># Payments</th>
            <th>Amount</th>
            <th>Currency</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {batches.map((batch) => (
            <tr key={batch.id}>
              <td>
                <Form.Check
                  type="checkbox"
                  checked={selected.includes(batch.id)}
                  onChange={() => toggleSelect(batch.id)}
                />
              </td>
              <td>{batch.yourRef}</td>
              <td>{batch.batchName}</td>
              <td>{batch.numOfPayments}</td>
              <td>₹{batch.totAmt}</td>
              <td>{batch.currency}</td>
              <td>{batch.status}</td>
            </tr>
          ))}
        </tbody>
      </Table>

      <Button
        variant="success"
        onClick={() => handleDecision(true)}
        disabled={selected.length === 0}
      >
        Approve
      </Button>{" "}
      <Button
        variant="danger"
        onClick={() => handleDecision(false)}
        disabled={selected.length === 0}
      >
        Reject
      </Button>
    </div>
  );
}
