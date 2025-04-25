package com.ohgiraffers.demo_church.common;



public class SheetResponse {
    private boolean success;
    private String message;
    private String updatedRange; // 쓰여진 범위

    public SheetResponse(boolean success, String message, String updatedRange) {
        this.success = success;
        this.message = message;
        this.updatedRange = updatedRange;
    }


    public SheetResponse(boolean success, String updatedRange) {
        this.success = success;
        this.message = "API 요청 성공";
        this.updatedRange = updatedRange;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getUpdatedRange() {
        return updatedRange;
    }
}



