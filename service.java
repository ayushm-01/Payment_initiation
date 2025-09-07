import React, { useState, useEffect } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { Card, Table, Form, Button, Spinner } from "react-bootstrap";
import axios from "axios";

export default function SubmitApproval() {
  const [batch, setBatch] = useState(null);
  const [comments, setComments] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(true);

  // Replace with the Jr Manager's token
  const token = localStorage.getItem("token"); // or set manually for now

  // Fetch the next batch for Jr Manager
  useEffect(() => {
    const fetchNextBatch = async () => {
      try {
        const res = await axios.get(
          "http://localhost:8080/api/jr-manager/next-batch",
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setBatch(res.data);
      } catch (err) {
        console.error("Error fetching batch:", err);
        alert("Failed to fetch batch!");
      } finally {
        setLoading(false);
      }
    };
    fetchNextBatch();
  }, [token]);

  const handleDecision = async (approved) => {
    if (!password) {
      alert("Please enter your password!");
      return;
    }

    try {
      const res = await axios.post(
        "http://localhost:8080/api/jr-manager/decision",
        null,
        {
          params: {
            batchIds: [batch.id],
            jrManagerId: 1, // replace with actual logged-in Jr Manager ID
            approved,
            password,
            comment: comments,
          },
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      alert(res.data);
      setBatch(null); // clear batch after decision
    } catch (err) {
      console.error("Error submitting decision:", err);
      alert("Failed to submit decision!");
    }
  };

  if (loading) return <Spinner animation="border" />;

  if (!batch) return <div className="text-center mt-5">No pending batches.</div>;

  return (
    <div className="container mt-4">
      {/* Batch Info */}
      <Card className="mb-4 shadow-sm border-0">
        <Card.Header className="fw-bold bg-primary text-white">Batch Information</Card.Header>
        <Card.Body>
          <Table striped bordered hover responsive>
            <tbody>
              <tr>
                <th>Batch Reference</th>
                <td>{batch.reference}</td>
              </tr>
              <tr>
                <th>Batch Name</th>
                <td>{batch.name}</td>
              </tr>
              <tr>
                <th>Created By</th>
                <td>{batch.createdBy}</td>
              </tr>
              <tr>
                <th># Payments</th>
                <td>{batch.numPayments}</td>
              </tr>
              <tr>
                <th>Total Amount</th>
                <td>{batch.totalAmount}</td>
              </tr>
              <tr>
                <th>Currency</th>
                <td>{batch.currency}</td>
              </tr>
              <tr>
                <th>Execution Date</th>
                <td>{batch.executionDate}</td>
              </tr>
            </tbody>
          </Table>
        </Card.Body>
      </Card>

      {/* Batch Summary */}
      <Card className="mb-4 shadow-sm border-0">
        <Card.Header className="fw-bold bg-primary text-white">Batch Summary</Card.Header>
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
      <Card className="mb-4 shadow-sm border-0">
        <Card.Header className="fw-bold bg-primary text-white">Approval Details</Card.Header>
        <Card.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Comments</Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                value={comments}
                onChange={(e) => setComments(e.target.value)}
                placeholder="Enter your comments (optional)"
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Password Validation</Form.Label>
              <Form.Control
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Enter your password"
              />
            </Form.Group>
          </Form>
        </Card.Body>
      </Card>

      {/* Action Buttons */}
      <div className="d-flex justify-content-end gap-3">
        <Button variant="success" className="px-4" onClick={() => handleDecision(true)}>
          Approve Batch
        </Button>
        <Button variant="danger" className="px-4" onClick={() => handleDecision(false)}>
          Reject Batch
        </Button>
      </div>
    </div>
  );
}
