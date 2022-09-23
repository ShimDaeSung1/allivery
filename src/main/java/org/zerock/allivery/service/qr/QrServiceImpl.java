package org.zerock.allivery.service.qr;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.allivery.entity.qr.Qr;
import org.zerock.allivery.entity.qr.QrRepository;
import org.zerock.allivery.entity.user.User;
import org.zerock.allivery.exception.CQrAuthenticationFailedException;
import org.zerock.allivery.exception.CQrNotFoundException;
import org.zerock.allivery.exception.handler.ErrorCode;

@Service
@RequiredArgsConstructor
@Log4j2
public class QrServiceImpl implements QrService{

    private final QrRepository qrRepository;

    //주문시 큐알코드 생성
    @Override
    public Long register(User user) {
        log.info("user.getSerialNum:"+user.getSerialNum());

        Qr qr = Qr.builder()
                .serialNum(user.getSerialNum())
                .build();

        Qr id = qrRepository.save(qr);
        log.info("qr ::"+id.getQrId());
        return id.getQrId();
    }

    @Override
    public String open(Long qrId, User user) {
        String door = "close";

        Qr qr = qrRepository.findById(qrId)
                .orElseThrow(() -> new CQrNotFoundException("이미 사라진 QR코드입니다.", ErrorCode.NOT_FOUND));

        if (!(qr.getSerialNum().equals(user.getSerialNum()))) {
            throw new CQrAuthenticationFailedException("QR코드 인증 결과 일치하지 않습니다.", ErrorCode.FORBIDDEN);
        } else if (qr.getSerialNum().equals(user.getSerialNum())) {
            door = "open";
        }
        return door;
    }


}
