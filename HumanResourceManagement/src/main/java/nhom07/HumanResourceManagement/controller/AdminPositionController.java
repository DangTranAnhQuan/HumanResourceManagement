package nhom07.HumanResourceManagement.controller;

import nhom07.HumanResourceManagement.entity.Position;
import nhom07.HumanResourceManagement.service.AdminPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/positions")
public class AdminPositionController {

    @Autowired
    private AdminPositionService positionService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(positionService.getAllPositions());
    }

    @PostMapping
    public ResponseEntity<?> create(Authentication auth, @RequestBody Position position) {
        try {
            Position created = positionService.createPosition(auth.getName(), position);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(Authentication auth, @PathVariable Integer id, @RequestBody Position position) {
        try {
            Position updated = positionService.updatePosition(auth.getName(), id, position);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(Authentication auth, @PathVariable Integer id) {
        try {
            positionService.deletePosition(auth.getName(), id);
            return ResponseEntity.ok(Map.of("message", "Đã xóa chức vụ thành công."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}