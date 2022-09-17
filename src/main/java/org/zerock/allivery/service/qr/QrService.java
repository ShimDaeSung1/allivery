package org.zerock.allivery.service.qr;

import org.zerock.allivery.dto.qr.OrderDTO;
import org.zerock.allivery.entity.user.User;

public interface QrService {

    void register(User user);

    String open(Long qrId, User user);
}
