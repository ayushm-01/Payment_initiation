@Service
public class ApprovalService {
    
    @Autowired private BatchRepository batchRepo;
    @Autowired private ApproverRepository approverRepo;
    
    // Get batches for approver to see
    public List<Batch> getPendingBatchesForApprover() {
        return batchRepo.findByStatus(BatchStatus.PENDING);
    }
    
    // Approver decision on selected batches (single or multiple)
    // batchIds comes from frontend - user selects checkboxes in table
    public void approverDecision(List<Long> batchIds, User approver, boolean approved) {
        for (Long batchId : batchIds) {
            Batch batch = batchRepo.findById(batchId)
                    .orElseThrow(() -> new RuntimeException("Batch not found"));
            
            // Check if approver already acted
            List<Approver> existing = approverRepo.findByBatch(batch);
            for (Approver a : existing) {
                if (a.getUser().getId().equals(approver.getId()) && a.getLevel() == 1) {
                    throw new RuntimeException("Already acted on batch: " + batchId);
                }
            }
            
            // Save approver action
            Approver app = new Approver();
            app.setBatch(batch);
            app.setUser(approver);
            app.setApproved(approved);
            app.setLevel(1);
            approverRepo.save(app);
            
            // Update batch status
            if (approved) {
                batch.setStatus(BatchStatus.UNDER_MANAGER_REVIEW);
            } else {
                batch.setStatus(BatchStatus.REJECTED);
            }
            batchRepo.save(batch);
        }
    }
    
    // Get next batch for manager
    public Batch getNextBatchForManager(User manager) {
        List<Batch> pendingBatches = batchRepo.findByStatus(BatchStatus.UNDER_MANAGER_REVIEW);
        
        for (Batch batch : pendingBatches) {
            // Check if this manager already acted on this batch
            List<Approver> existing = approverRepo.findByBatch(batch);
            boolean alreadyActed = false;
            
            for (Approver a : existing) {
                if (a.getUser().getId().equals(manager.getId()) && a.getLevel() >= 2) {
                    alreadyActed = true;
                    break;
                }
            }
            
            if (!alreadyActed) {
                return batch; // Return first batch this manager hasn't acted on
            }
        }
        return null; // No batches for this manager
    }
    
    // Manager decision
    public void managerDecision(Long batchId, User manager, String password, boolean approved, String comment) {
        // Check password
        if (!password.equals(manager.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        Batch batch = batchRepo.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));
        
        // Check if manager already acted
        List<Approver> existing = approverRepo.findByBatch(batch);
        for (Approver a : existing) {
            if (a.getUser().getId().equals(manager.getId()) && a.getLevel() >= 2) {
                throw new RuntimeException("Manager already acted on this batch");
            }
        }
        
        // Save manager action
        Approver app = new Approver();
        app.setBatch(batch);
        app.setUser(manager);
        app.setApproved(approved);
        app.setComment(comment);
        app.setLevel(2);
        approverRepo.save(app);
        
        if (!approved) {
            batch.setStatus(BatchStatus.REJECTED);
            batchRepo.save(batch);
            return;
        }
        
        // Count manager approvals
        int managerApprovals = 0;
        List<Approver> allApprovers = approverRepo.findByBatch(batch);
        for (Approver a : allApprovers) {
            if (a.isApproved() && a.getLevel() >= 2) {
                managerApprovals++;
            }
        }
        
        int requiredManagers = getRequiredManagers(batch.getTotalAmount());
        if (managerApprovals >= requiredManagers) {
            batch.setStatus("APPROVED");
        }
        batchRepo.save(batch);
    }
    
    // Simple method to calculate required managers
    private int getRequiredManagers(Double amount) {
        if (amount < 100000) return 1;      // < 1 Lakh
        else if (amount < 1000000) return 2; // < 10 Lakh  
        else return 3;                      // >= 10 Lakh
    }
}
