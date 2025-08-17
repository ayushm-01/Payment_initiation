import './App.css';
import HeaderRow from './components/HeaderRow';
import ProgressTracker from './components/ProgressTracker';
import DashboardLayout from './components/DashBoardLayout';
import FilterImplementation from './components/FilterImplementation';
import Dashboard from './components/Dashboard';
import HeaderRowApprover from './components/SubmitPayroll/HeaderRowApprover';
import SubmitApproval from './components/SubmitPayroll/SubmitApproval';

function App() {
  const role = "approver2";  

  return (
    <div className="App">
      <Dashboard role={role}>
        {role === "approver1" || "approver2" ? <HeaderRowApprover /> : <HeaderRow />}
        <ProgressTracker role={role} />
        {role === "batching" ? (
          <FilterImplementation />
        ) : (
          <SubmitApproval />
        )}
      </Dashboard>
    </div>
  );
}

export default App;
