package org.zerock.allivery.controller.order;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.zerock.allivery.annotation.CurrentUser;
import org.zerock.allivery.entity.user.User;
import org.zerock.allivery.entity.user.UserAccount;
import org.zerock.allivery.entity.user.UserRepository;
import org.zerock.allivery.service.qr.QrService;

import java.security.Principal;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Log4j2
public class QrController {

    private final QrService qrService;

    //주문버튼 클릭 시 qr코드 생성됨
    @PostMapping("/")
    public ResponseEntity<Long> register(@AuthenticationPrincipal User user){

        log.info("token ???");
        log.info("asdads : "+user.getSerialNum());
        Long qrId = qrService.register(user);

        return ResponseEntity.ok(qrId);
    }

//    QR코드 인증, 상자 문 열기
    @GetMapping("/open")
    public ResponseEntity<String> open(@RequestParam("qr")Long qrId, @AuthenticationPrincipal User user){

        String door = qrService.open(qrId, user);

        return ResponseEntity.ok(door);
    }




}
