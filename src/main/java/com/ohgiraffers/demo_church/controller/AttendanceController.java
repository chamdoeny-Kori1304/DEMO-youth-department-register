package com.ohgiraffers.demo_church.controller;


import com.ohgiraffers.demo_church.common.SheetResponse;
import com.ohgiraffers.demo_church.dto.UpdateAttendanceDTO;
import com.ohgiraffers.demo_church.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private  final AttendanceRepository attendanceRepository;

    @GetMapping("/{date}")
    public ResponseEntity<?> getNamesByDate(@RequestParam String date){
        List<String> res  = attendanceRepository.getNamesByDate(date);

        return ResponseEntity.ok(res);
    }

    @PutMapping("")
    public ResponseEntity<?>  updateAttendance(@RequestBody UpdateAttendanceDTO dto){

       SheetResponse res = attendanceRepository.updateAttendance(dto.getNames(), dto.getDate().toString());

         return ResponseEntity.ok(res.getUpdateCells());
    }


}
