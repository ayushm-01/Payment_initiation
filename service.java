import React, { useState, useEffect } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { Card, Table, Form, Button, Alert } from "react-bootstrap";
import axios from "axios";

export default function SubmitApproval({ token, role, userId }) {
  const [batch, setBatch] = useState(null);
  const [comments, setComments] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  // Determine API endpoint for fetching next batch
  const getNextBatchUrl = () => {
    if (role === "JrManager") return "/jr-manager/next-batch";
    if (role === "SrManager") return "/sr-manager/next-batch";
    if (role === "Director") return "/director/next-batch";
    return "";
  };

  // Determine API endpoint for submitting decision
  const getDecisionUrl = () => {
    if (role === "JrManager") return "/jr-manager/decision";
    if (role === "SrManager") return "/sr-manager/decision";
    if (role === "Director") return "/director/decision";
    return "";
  };

  // Determine param key for the logged-in user
  const getUserIdParam = () => {
    if (role === "JrManager") return { jrManagerId: userId };
    if (role === "SrManager") return { srManagerId: userId };
    if (role === "Director") return { directorId: userId };
    return {};
  };

  // Fetch next batch
  useEffect(() => {
    const fetchNextBatch = async () => {
      try {
        const res = await axios.get(
          `http://localhost:8080/api${getNextBatchUrl()}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        setBatch(res.data);
      } catch (err) {
        console.error(err);
        setMessage("Failed to fetch batch!");
      }
    };
    fetchNextBatch();
  }, [token]);

  // Handle Approve/Reject
  const handleDecision = async (approved) => {
    if (!password) return setMessage("Please enter your password!");

    try {
      const res = await axios.post(
        `http://localhost:8080/api${getDecisionUrl()}`,
        null,
        {
          params: { batchIds: [batch.id], approved, password, comment: comments, ...getUserIdParam() },
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setMessage(res.data);
      setBatch(null); // clear batch after decision
      setComments("");
      setPassword("");
    } catch (err) {
      console.error(err);
      setMessage("Failed to submit decision!");
    }
  };

  if (!batch) return <div className="text-center mt-5">No pending batches.</div>;

  return (
    <div className="container mt-4">
      {message && <Alert variant="info">{message}</Alert>}

      {/* Batch Info */}
      <Card className="mb-3 shadow-sm">
        <Card.Header className="bg-primary text-white">Batch Information</Card.Header>
        <Card.Body>
          <Table striped bordered hover responsive>
            <tbody>
              <tr><th>Reference</th><td>{batch.reference}</td></tr>
              <tr><th>Name</th><td>{batch.name}</td></tr>
              <tr><th>Created By</th><td>{batch.createdBy}</td></tr>
              <tr><th># Payments</th><td>{batch.numPayments}</td></tr>
              <tr><th>Total Amount</th><td>{batch.totalAmount}</td></tr>
              <tr><th>Currency</th><td>{batch.currency}</td></tr>
              <tr><th>Execution Date</th><td>{batch.executionDate}</td></tr>
            </tbody>
          </Table>
        </Card.Body>
      </Card>

      {/* Batch Summary */}
      <Card className="mb-3 shadow-sm">
        <Card.Header className="bg-primary text-white">Batch Summary</Card.Header>
        <Card.Body>
          <Table striped bordered hover responsive>
            <thead>
              <tr>
                <th>Country</th>
                <th>Payment Type</th>
                <th># Payments</th>
                <th>Amount</th>
              </tr>
            </thead>
            <tbody>
              {batch.summary.map((item, idx) => (
                <tr key={idx}>
                  <td>{item.country}</td>
                  <td>{item.paymentType}</td>
                  <td>{item.numPayments}</td>
                  <td>{item.amount}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        </Card.Body>
      </Card>

      {/* Approval Form */}
      <Card className="mb-3 shadow-sm">
        <Card.Header className="bg-primary text-white">Approval Details</Card.Header>
        <Card.Body>
          <Form>
            <Form.Group className="mb-2">
              <Form.Label>Comments (optional)</Form.Label>
              <Form.Control
                as="textarea"
                rows={2}
                value={comments}
                onChange={(e) => setComments(e.target.value)}
              />
            </Form.Group>
            <Form.Group className="mb-2">
              <Form.Label>Password</Form.Label>
              <Form.Control
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </Form.Group>
          </Form>
        </Card.Body>
      </Card>

      {/* Action Buttons */}
      <div className="d-flex justify-content-end gap-2 mb-4">
        <Button variant="success" onClick={() => handleDecision(true)} disabled={!password}>
          Approve
        </Button>
        <Button variant="danger" onClick={() => handleDecision(false)} disabled={!password}>
          Reject
        </Button>
      </div>
    </div>
  );
}
