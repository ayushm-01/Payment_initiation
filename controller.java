import React, { useState, useEffect } from "react";
import { Table, Form, Button, Card } from "react-bootstrap";
import axios from "axios";

export default function BatchTable({ batches, setBatches }) {
  const [selected, setSelected] = useState([]);

  // ✅ Fetch batches
  useEffect(() => {
    const token = localStorage.getItem("token");
    axios
      .get("http://localhost:8080/api/approver/batches", {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => setBatches(res.data))
      .catch((err) => console.error("Error fetching batches:", err));
  }, [setBatches]);

  // ✅ Select / Deselect
  const toggleSelect = (id) => {
    setSelected((prev) =>
      prev.includes(id) ? prev.filter((i) => i !== id) : [...prev, id]
    );
  };

  // ✅ Approve / Reject with confirmation
  const handleDecision = async (approved) => {
    if (!selected.length) return;

    const action = approved ? "approve" : "reject";
    const confirmAction = window.confirm(
      `Are you sure you want to ${action} batches: ${selected.join(", ")}?`
    );
    if (!confirmAction) return;

    const token = localStorage.getItem("token");
    const approverId = localStorage.getItem("approverId");

    try {
      await axios.post(
        "http://localhost:8080/api/approver/decision",
        null,
        {
          params: {
            batchIds: selected.join(","),
            approverid: approverId,
            approved: approved,
          },
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      alert(`${approved ? "✅ Approved" : "❌ Rejected"} batches successfully!`);
      setBatches((prev) => prev.filter((b) => !selected.includes(b.batchId)));
      setSelected([]);
    } catch (err) {
      console.error("Error submitting decision:", err);
      alert("Failed to update batches");
    }
  };

  return (
    <Card className="p-3">
      <h4>Pending Batches</h4>
      <Table striped bordered hover responsive>
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
            <tr key={batch.batchId}>
              <td>
                <Form.Check
                  type="checkbox"
                  checked={selected.includes(batch.batchId)}
                  onChange={() => toggleSelect(batch.batchId)}
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
        disabled={!selected.length}
        className="me-2"
      >
        Approve
      </Button>
      <Button
        variant="danger"
        onClick={() => handleDecision(false)}
        disabled={!selected.length}
      >
        Reject
      </Button>
    </Card>
  );
}
