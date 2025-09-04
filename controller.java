@Service
public class ApproverService {
    
    @Autowired private BatchRepository batchRepo;
    @Autowired private ApproverRepository approverRepo;
    
    // Get all pending batches for approver
    public List<Batch> getPendingBatches() {
        return batchRepo.findByStatus("PENDING");
    }
    
    // Approver decision on multiple batches
    public void approverDecision(List<Long> batchIds, User approver, boolean approved) {
        for (Long batchId : batchIds) {
            Batch batch = batchRepo.findById(batchId)
                    .orElseThrow(() -> new RuntimeException("Batch not found"));
            
            // Check if approver already acted
            if (approverRepo.findByBatchAndUser(batch, approver) != null) {
                throw new RuntimeException("Already acted on batch: " + batchId);
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
                batch.setStatus("UNDER_JR_MANAGER_REVIEW");
            } else {
                batch.setStatus("REJECTED");
            }
            batchRepo.save(batch);
        }
    }
    
    // Get next batch for Jr Manager
    public Batch getNextBatchForJrManager() {
        List<Batch> batches = batchRepo.findByStatus("UNDER_JR_MANAGER_REVIEW");
        return batches.isEmpty() ? null : batches.get(0);
    }
    
    // Jr Manager decision
    public void jrManagerDecision(Long batchId, User jrManager, boolean approved) {
        Batch batch = batchRepo.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));
        
        // Check if already acted
        if (approverRepo.findByBatchAndUser(batch, jrManager) != null) {
            throw new RuntimeException("Already acted on this batch");
        }
        
        // Save Jr Manager action
        Approver app = new Approver();
        app.setBatch(batch);
        app.setUser(jrManager);
        app.setApproved(approved);
        app.setLevel(2);
        approverRepo.save(app);
        
        if (!approved) {
            batch.setStatus("REJECTED");
        } else {
            // Check if needs Sr Manager approval
            if (batch.getTotalAmount() >= 100000) { // 1 Lakh or more
                batch.setStatus("UNDER_SR_MANAGER_REVIEW");
            } else {
                batch.setStatus("APPROVED");
            }
        }
        batchRepo.save(batch);
    }
    
    // Get next batch for Sr Manager
    public Batch getNextBatchForSrManager() {
        List<Batch> batches = batchRepo.findByStatus("UNDER_SR_MANAGER_REVIEW");
        return batches.isEmpty() ? null : batches.get(0);
    }
    
    // Sr Manager decision
    public void srManagerDecision(Long batchId, User srManager, boolean approved) {
        Batch batch = batchRepo.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));
        
        // Check if already acted
        if (approverRepo.findByBatchAndUser(batch, srManager) != null) {
            throw new RuntimeException("Already acted on this batch");
        }
        
        // Save Sr Manager action
        Approver app = new Approver();
        app.setBatch(batch);
        app.setUser(srManager);
        app.setApproved(approved);
        app.setLevel(3);
        approverRepo.save(app);
        
        if (!approved) {
            batch.setStatus("REJECTED");
        } else {
            batch.setStatus("APPROVED");
        }
        batchRepo.save(batch);
    }
}

@Repository
public interface ApproverRepository extends JpaRepository<Approver, Long> {
    Approver findByBatchAndUser(Batch batch, User user);
}

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
    List<Batch> findByStatus(String status);
}

@RestController
@RequestMapping("/api/approval")
public class ApprovalController {
    
    @Autowired
    private ApproverService approverService;
    
    @Autowired
    private UserRepository userRepository;
    
    // ===== APPROVER ENDPOINTS =====
    
    @GetMapping("/approver/batches")
    public List<Batch> getPendingBatches() {
        return approverService.getPendingBatches();
    }
    
    @PostMapping("/approver/decision")
    public String approverDecision(@RequestParam List<Long> batchIds, 
                                  @RequestParam Long approverId,
                                  @RequestParam boolean approved) {
        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new RuntimeException("Approver not found"));
        
        approverService.approverDecision(batchIds, approver, approved);
        return approved ? "Batches approved" : "Batches rejected";
    }
    
    // ===== JR MANAGER ENDPOINTS =====
    
    @GetMapping("/jr-manager/next-batch")
    public Batch getNextBatchForJrManager() {
        return approverService.getNextBatchForJrManager();
    }
    
    @PostMapping("/jr-manager/decision")
    public String jrManagerDecision(@RequestParam Long batchId, 
                                   @RequestParam Long jrManagerId,
                                   @RequestParam boolean approved) {
        User jrManager = userRepository.findById(jrManagerId)
                .orElseThrow(() -> new RuntimeException("Jr Manager not found"));
        
        approverService.jrManagerDecision(batchId, jrManager, approved);
        return approved ? "Batch approved by Jr Manager" : "Batch rejected by Jr Manager";
    }
    
    // ===== SR MANAGER ENDPOINTS =====
    
    @GetMapping("/sr-manager/next-batch")
    public Batch getNextBatchForSrManager() {
        return approverService.getNextBatchForSrManager();
    }
    
    @PostMapping("/sr-manager/decision")
    public String srManagerDecision(@RequestParam Long batchId, 
                                   @RequestParam Long srManagerId,
                                   @RequestParam boolean approved) {
        User srManager = userRepository.findById(srManagerId)
                .orElseThrow(() -> new RuntimeException("Sr Manager not found"));
        
        approverService.srManagerDecision(batchId, srManager, approved);
        return approved ? "Batch approved by Sr Manager" : "Batch rejected by Sr Manager";
    }
}
