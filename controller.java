// ========== ENTITIES ==========

@Entity
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double totalAmount;
    private String status;
    
    public Batch() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role;
    
    public User() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

@Entity
public class Approver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Batch batch;
    
    @ManyToOne
    private User user;
    
    private boolean approved;
    private String comment;
    private int level;
    
    public Approver() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Batch getBatch() { return batch; }
    public void setBatch(Batch batch) { this.batch = batch; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
}

// ========== REPOSITORIES ==========

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
    List<Batch> findByStatus(String status);
}

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

@Repository
public interface ApproverRepository extends JpaRepository<Approver, Long> {
    List<Approver> findByBatch(Batch batch);
}

// ========== CONTROLLER ==========

@RestController
public class ApprovalController {
    
    @Autowired
    private ApprovalService approvalService;
    
    @Autowired
    private UserRepository userRepo;
    
    @GetMapping("/approver/batches")
    public List<Batch> getPendingBatches() {
        return approvalService.getPendingBatchesForApprover();
    }
    
    @PostMapping("/approver/decision")
    public String approverDecision(@RequestParam List<Long> batchIds, 
                                   @RequestParam Long approverId, 
                                   @RequestParam boolean approved) {
        User approver = userRepo.findById(approverId).get();
        approvalService.approverDecision(batchIds, approver, approved);
        return approved ? "Approved!" : "Rejected!";
    }
    
    @GetMapping("/manager/{managerId}/next-batch")
    public Batch getNextBatch(@PathVariable Long managerId) {
        User manager = userRepo.findById(managerId).get();
        return approvalService.getNextBatchForManager(manager);
    }
    
    @PostMapping("/manager/decision")
    public String managerDecision(@RequestParam Long batchId,
                                  @RequestParam Long managerId,
                                  @RequestParam String password,
                                  @RequestParam boolean approved,
                                  @RequestParam String comment) {
        User manager = userRepo.findById(managerId).get();
        approvalService.managerDecision(batchId, manager, password, approved, comment);
        return approved ? "Approved!" : "Rejected!";
    }
}
