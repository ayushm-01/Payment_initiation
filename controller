@Service
public class ApprovalService {

    @Autowired private BatchRepository batchRepo;
    @Autowired private ApproverRepository approverRepo;

    // Get batches pending for approver
    public List<Batch> getPendingBatchesForApprover() {
        return batchRepo.findByStatus(BatchStatus.PENDING);
    }

    // Approver action
    public void approverDecision(Long batchId, User approver, boolean approved) {
        Batch batch = batchRepo.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        // Prevent same approver acting twice
        List<Approver> existing = approverRepo.findByBatch(batch);
        for (Approver a : existing) {
            if (a.getUser().getId().equals(approver.getId()) && a.getLevel() == 1) {
                throw new RuntimeException("This approver already acted on this batch");
            }
        }

        // Record approver action
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

    // Manager action
    public void managerDecision(Long batchId, User manager, String password, boolean approved, String comment) {
        Batch batch = batchRepo.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        // Password verification
        if (!manager.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        // Prevent same manager acting twice
        List<Approver> existing = approverRepo.findByBatch(batch);
        for (Approver a : existing) {
            if (a.getUser().getId().equals(manager.getId()) && a.getLevel() >= 2) {
                throw new RuntimeException("This manager already acted on this batch");
            }
        }

        // Record manager action
        Approver app = new Approver();
        app.setBatch(batch);
        app.setUser(manager);
        app.setApproved(approved);
        app.setComment(comment);  // optional
        app.setLevel(2);          // manager level
        approverRepo.save(app);

        if (!approved) {
            batch.setStatus(BatchStatus.REJECTED);
            batchRepo.save(batch);
            return;
        }

        // Count how many managers approved this batch
        int managerApprovals = 0;
        for (Approver a : approverRepo.findByBatch(batch)) {
            if (a.isApproved() && a.getLevel() >= 2) {
                managerApprovals++;
            }
        }

        int requiredManagers = calculateRequiredManagers(batch.getTotalAmount());

        if (managerApprovals >= requiredManagers) {
            batch.setStatus(BatchStatus.APPROVED);
        }

        batchRepo.save(batch);
    }

    // Determine number of managers required based on batch amount
    private int calculateRequiredManagers(Double amount) {
        if (amount < 100000) return 1;
        else if (amount < 1000000) return 2;
        else return 3;
    }
}
