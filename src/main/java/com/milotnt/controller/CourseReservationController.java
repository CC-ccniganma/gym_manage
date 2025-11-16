package com.milotnt.controller;

import com.milotnt.pojo.CourseReservation;
import com.milotnt.service.CourseReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/course-reservation")
public class CourseReservationController {

    @Autowired
    private CourseReservationService courseReservationService;

    @GetMapping("/list")
    public ResponseEntity<List<CourseReservation>> getAllReservations() {
        return ResponseEntity.ok(courseReservationService.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<Boolean> addReservation(@RequestBody CourseReservation courseReservation) {
        if (courseReservationService.isReservationExists(
                courseReservation.getMemberAccount(),
                courseReservation.getCoachAccount(),
                courseReservation.getReservationDate(),
                courseReservation.getPeriod())) {
            return ResponseEntity.badRequest().body(false);
        }
        return ResponseEntity.ok(courseReservationService.addReservation(courseReservation));
    }

    @GetMapping("/member/{memberAccount}")
    public ResponseEntity<List<CourseReservation>> getMemberReservations(@PathVariable Integer memberAccount) {
        return ResponseEntity.ok(courseReservationService.findByMemberAccount(memberAccount));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteReservation(@RequestBody CourseReservation courseReservation) {
        return ResponseEntity.ok(courseReservationService.deleteReservation(courseReservation));
    }

    @GetMapping("/coach")
    public ResponseEntity<List<CourseReservation>> getCoachReservations(
            @RequestParam Integer coachAccount,
            @RequestParam Date reservationDate) {
        return ResponseEntity.ok(courseReservationService.findByCoachAndDate(coachAccount, reservationDate));
    }
} 